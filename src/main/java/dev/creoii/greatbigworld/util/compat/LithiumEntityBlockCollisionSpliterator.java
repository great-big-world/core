package dev.creoii.greatbigworld.util.compat;

import com.google.common.collect.AbstractIterator;
import dev.creoii.greatbigworld.util.EntityBlockCollisionSpliterator;
import net.caffeinemc.mods.lithium.common.block.BlockCountingSection;
import net.caffeinemc.mods.lithium.common.block.BlockStateFlags;
import net.caffeinemc.mods.lithium.common.shapes.VoxelShapeCaster;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class LithiumEntityBlockCollisionSpliterator extends AbstractIterator<VoxelShape> {
    public static final Map<TagKey<EntityType<?>>, Predicate<EntityBlockCollisionSpliterator.EntityBlockCollisionContext>> INTERACTIONS = new HashMap<>();
    private final BlockPos.Mutable pos;
    private final Box box;
    private final Entity entity;
    private final VoxelShape shape;
    private final World world;
    private final ShapeContext context;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private int chunkX;
    private int chunkYIndex;
    private int chunkZ;
    private int cStartX;
    private int cStartZ;
    private int cEndX;
    private int cEndZ;
    private int cX;
    private int cY;
    private int cZ;
    private int maxHitX;
    private int maxHitY;
    private int maxHitZ;
    private VoxelShape maxShape;
    private final boolean hideLastCollision;
    private int cTotalSize;
    private int cIterated;
    private boolean sectionOversizedBlocks;
    private Chunk cachedChunk;
    private ChunkSection cachedChunkSection;
    private final Predicate<EntityBlockCollisionSpliterator.EntityBlockCollisionContext> contextPredicate;

    public LithiumEntityBlockCollisionSpliterator(World world, @Nullable Entity entity, Box box, boolean hideLastCollision, Predicate<EntityBlockCollisionSpliterator.EntityBlockCollisionContext> contextPredicate) {
        pos = new BlockPos.Mutable();
        this.entity = entity;
        this.box = box;
        shape = VoxelShapes.cuboid(box);
        context = entity == null ? ShapeContext.absent() : ShapeContext.of(entity);
        this.world = world;
        minX = MathHelper.floor(box.minX - 1e-7);
        maxX = MathHelper.floor(box.maxX + 1e-7);
        minY = MathHelper.clamp(MathHelper.floor(box.minY - 1e-7), world.getBottomY(), world.getTopYInclusive());
        maxY = MathHelper.clamp(MathHelper.floor(box.maxY + 1e-7), world.getBottomY(), world.getTopYInclusive());
        minZ = MathHelper.floor(box.minZ - 1e-7);
        maxZ = MathHelper.floor(box.maxZ + 1e-7);
        chunkX = ChunkSectionPos.getSectionCoord(expandMin(minX));
        chunkZ = ChunkSectionPos.getSectionCoord(expandMin(minZ));
        cIterated = 0;
        cTotalSize = 0;
        maxHitX = Integer.MIN_VALUE;
        maxHitY = Integer.MIN_VALUE;
        maxHitZ = Integer.MIN_VALUE;
        maxShape = null;
        this.hideLastCollision = hideLastCollision;
        this.contextPredicate = contextPredicate;
        --chunkX;
    }

    private boolean nextSection() {
        while(true) {
            if (this.cachedChunk != null && this.chunkYIndex < world.countVerticalSections() - 1 && this.chunkYIndex < ChunkSectionPos.getSectionCoord(expandMax(this.maxY)) - world.getBottomSectionCoord()) {
                ++this.chunkYIndex;
                this.cachedChunkSection = this.cachedChunk.getSectionArray()[this.chunkYIndex];
            } else {
                if (this.chunkX < ChunkSectionPos.getSectionCoord(expandMax(this.maxX))) {
                    ++this.chunkX;
                } else {
                    if (this.chunkZ >= ChunkSectionPos.getSectionCoord(expandMax(this.maxZ))) {
                        return false;
                    }

                    this.chunkX = ChunkSectionPos.getSectionCoord(expandMin(this.minX));
                    ++this.chunkZ;
                }

                this.cachedChunk = this.world.getChunk(this.chunkX, this.chunkZ, ChunkStatus.FULL, false);
                if (this.cachedChunk != null) {
                    this.chunkYIndex = MathHelper.clamp(ChunkSectionPos.getSectionCoord(expandMin(minY)) - world.getBottomSectionCoord(), 0, world.countVerticalSections() - 1);
                    this.cachedChunkSection = this.cachedChunk.getSectionArray()[this.chunkYIndex];
                }
            }

            if (this.cachedChunk != null && this.cachedChunkSection != null && !this.cachedChunkSection.isEmpty()) {
                this.sectionOversizedBlocks = hasChunkSectionOversizedBlocks(this.cachedChunk, this.chunkYIndex);
                int sizeExtension = this.sectionOversizedBlocks ? 1 : 0;
                this.cEndX = Math.min(this.maxX + sizeExtension, 15 + ChunkSectionPos.getBlockCoord(chunkX));
                int cEndY = Math.min(this.maxY + sizeExtension, 15 + ChunkSectionPos.getBlockCoord(chunkYIndex + world.getBottomSectionCoord()));
                this.cEndZ = Math.min(this.maxZ + sizeExtension, 15 + ChunkSectionPos.getBlockCoord(chunkZ));
                this.cStartX = Math.max(this.minX - sizeExtension, ChunkSectionPos.getBlockCoord(chunkX));
                int cStartY = Math.max(this.minY - sizeExtension, ChunkSectionPos.getBlockCoord(chunkYIndex + world.getBottomSectionCoord()));
                this.cStartZ = Math.max(this.minZ - sizeExtension, ChunkSectionPos.getBlockCoord(chunkZ));
                this.cX = this.cStartX;
                this.cY = cStartY;
                this.cZ = this.cStartZ;
                this.cTotalSize = (this.cEndX - this.cStartX + 1) * (cEndY - cStartY + 1) * (this.cEndZ - this.cStartZ + 1);
                if (this.cTotalSize != 0) {
                    this.cIterated = 0;
                    return true;
                }
            }
        }
    }

    public VoxelShape computeNext() {
        while(this.cIterated < this.cTotalSize || this.nextSection()) {
            ++this.cIterated;
            int x = this.cX;
            int y = this.cY;
            int z = this.cZ;
            if (this.cX < this.cEndX) {
                ++this.cX;
            } else if (this.cZ < this.cEndZ) {
                this.cX = this.cStartX;
                ++this.cZ;
            } else {
                this.cX = this.cStartX;
                this.cZ = this.cStartZ;
                ++this.cY;
            }

            int edgesHit = this.sectionOversizedBlocks ? (x >= this.minX && x <= this.maxX ? 0 : 1) + (y >= this.minY && y <= this.maxY ? 0 : 1) + (z >= this.minZ && z <= this.maxZ ? 0 : 1) : 0;
            if (edgesHit != 3) {
                BlockState state = this.cachedChunkSection.getBlockState(x & 15, y & 15, z & 15);
                if (canInteractWithBlock(state, edgesHit)) {
                    pos.set(x, y, z);

                    EntityBlockCollisionSpliterator.EntityBlockCollisionContext collisionContext = new EntityBlockCollisionSpliterator.EntityBlockCollisionContext(world, entity, pos, state);
                    if (!contextPredicate.test(collisionContext))
                        continue;

                    VoxelShape collisionShape = this.context.getCollisionShape(state, this.world, this.pos);
                    if (collisionShape != VoxelShapes.empty() && collisionShape != null) {
                        VoxelShape collidedShape = getCollidedShape(this.box, this.shape, collisionShape, x, y, z);
                        if (collidedShape != null) {
                            if (z >= this.maxHitZ && (z > this.maxHitZ || y >= this.maxHitY && (y > this.maxHitY || x > this.maxHitX))) {
                                this.maxHitX = x;
                                this.maxHitY = y;
                                this.maxHitZ = z;
                                VoxelShape previousMaxShape = this.maxShape;
                                this.maxShape = collidedShape;
                                if (previousMaxShape == null) {
                                    continue;
                                }

                                return previousMaxShape;
                            }

                            return collidedShape;
                        }
                    }
                }
            }
        }

        if (!this.hideLastCollision && this.maxShape != null) {
            VoxelShape previousMaxShape = this.maxShape;
            this.maxShape = null;
            return previousMaxShape;
        } else {
            return this.endOfData();
        }
    }

    private static boolean canInteractWithBlock(BlockState state, int edgesHit) {
        return (edgesHit != 1 || state.exceedsCube()) && (edgesHit != 2 || state.getBlock() == Blocks.MOVING_PISTON);
    }

    private static VoxelShape getCollidedShape(Box entityBox, VoxelShape entityShape, VoxelShape shape, int x, int y, int z) {
        if (shape == VoxelShapes.fullCube()) {
            return entityBox.intersects(x, y, z, (double)x + (double)1.0F, (double)y + (double)1.0F, (double)z + (double)1.0F) ? shape.offset((double)x, (double)y, (double)z) : null;
        } else if (shape instanceof VoxelShapeCaster) {
            return ((VoxelShapeCaster)shape).intersects(entityBox, x, y, z) ? shape.offset(x, y, z) : null;
        } else {
            shape = shape.offset(x, y, z);
            return VoxelShapes.matchesAnywhere(shape, entityShape, BooleanBiFunction.AND) ? shape : null;
        }
    }

    private static int expandMin(int coord) {
        return coord - 1;
    }

    private static int expandMax(int coord) {
        return coord + 1;
    }

    private static boolean hasChunkSectionOversizedBlocks(Chunk chunk, int chunkY) {
        if (!BlockStateFlags.ENABLED) {
            return true;
        } else {
            ChunkSection section = chunk.getSectionArray()[chunkY];
            return section != null && ((BlockCountingSection)section).lithium$mayContainAny(BlockStateFlags.OVERSIZED_SHAPE);
        }
    }

    public List<VoxelShape> collectAll() {
        ArrayList<VoxelShape> collisions = new ArrayList<>();

        while (hasNext()) {
            collisions.add(next());
        }

        return collisions;
    }
}