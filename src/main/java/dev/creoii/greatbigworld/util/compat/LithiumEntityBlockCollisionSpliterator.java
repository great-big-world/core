package dev.creoii.greatbigworld.util.compat;

import com.google.common.collect.AbstractIterator;
import dev.creoii.greatbigworld.util.EntityBlockCollisionSpliterator;
import net.caffeinemc.mods.lithium.common.block.BlockCountingSection;
import net.caffeinemc.mods.lithium.common.block.BlockStateFlags;
import net.caffeinemc.mods.lithium.common.shapes.VoxelShapeCaster;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class LithiumEntityBlockCollisionSpliterator extends AbstractIterator<VoxelShape> {
    public static final Map<TagKey<EntityType<?>>, Predicate<EntityBlockCollisionSpliterator.EntityBlockCollisionContext>> INTERACTIONS = new HashMap<>();
    private final BlockPos.MutableBlockPos pos;
    private final AABB box;
    private final Entity entity;
    private final VoxelShape shape;
    private final Level world;
    private final CollisionContext context;
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
    private ChunkAccess cachedChunk;
    private LevelChunkSection cachedChunkSection;
    private final Predicate<EntityBlockCollisionSpliterator.EntityBlockCollisionContext> contextPredicate;

    public LithiumEntityBlockCollisionSpliterator(Level world, @Nullable Entity entity, AABB box, boolean hideLastCollision, Predicate<EntityBlockCollisionSpliterator.EntityBlockCollisionContext> contextPredicate) {
        pos = new BlockPos.MutableBlockPos();
        this.entity = entity;
        this.box = box;
        shape = Shapes.create(box);
        context = entity == null ? CollisionContext.empty() : CollisionContext.of(entity);
        this.world = world;
        minX = Mth.floor(box.minX - 1e-7);
        maxX = Mth.floor(box.maxX + 1e-7);
        minY = Mth.clamp(Mth.floor(box.minY - 1e-7), world.getMinY(), world.getMaxY());
        maxY = Mth.clamp(Mth.floor(box.maxY + 1e-7), world.getMinY(), world.getMaxY());
        minZ = Mth.floor(box.minZ - 1e-7);
        maxZ = Mth.floor(box.maxZ + 1e-7);
        chunkX = SectionPos.blockToSectionCoord(expandMin(minX));
        chunkZ = SectionPos.blockToSectionCoord(expandMin(minZ));
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
            if (this.cachedChunk != null && this.chunkYIndex < world.getSectionsCount() - 1 && this.chunkYIndex < SectionPos.blockToSectionCoord(expandMax(this.maxY)) - world.getMinSectionY()) {
                ++this.chunkYIndex;
                this.cachedChunkSection = this.cachedChunk.getSections()[this.chunkYIndex];
            } else {
                if (this.chunkX < SectionPos.blockToSectionCoord(expandMax(this.maxX))) {
                    ++this.chunkX;
                } else {
                    if (this.chunkZ >= SectionPos.blockToSectionCoord(expandMax(this.maxZ))) {
                        return false;
                    }

                    this.chunkX = SectionPos.blockToSectionCoord(expandMin(this.minX));
                    ++this.chunkZ;
                }

                this.cachedChunk = this.world.getChunk(this.chunkX, this.chunkZ, ChunkStatus.FULL, false);
                if (this.cachedChunk != null) {
                    this.chunkYIndex = Mth.clamp(SectionPos.blockToSectionCoord(expandMin(minY)) - world.getMinSectionY(), 0, world.getSectionsCount() - 1);
                    this.cachedChunkSection = this.cachedChunk.getSections()[this.chunkYIndex];
                }
            }

            if (this.cachedChunk != null && this.cachedChunkSection != null && !this.cachedChunkSection.hasOnlyAir()) {
                this.sectionOversizedBlocks = hasChunkSectionOversizedBlocks(this.cachedChunk, this.chunkYIndex);
                int sizeExtension = this.sectionOversizedBlocks ? 1 : 0;
                this.cEndX = Math.min(this.maxX + sizeExtension, 15 + SectionPos.sectionToBlockCoord(chunkX));
                int cEndY = Math.min(this.maxY + sizeExtension, 15 + SectionPos.sectionToBlockCoord(chunkYIndex + world.getMinSectionY()));
                this.cEndZ = Math.min(this.maxZ + sizeExtension, 15 + SectionPos.sectionToBlockCoord(chunkZ));
                this.cStartX = Math.max(this.minX - sizeExtension, SectionPos.sectionToBlockCoord(chunkX));
                int cStartY = Math.max(this.minY - sizeExtension, SectionPos.sectionToBlockCoord(chunkYIndex + world.getMinSectionY()));
                this.cStartZ = Math.max(this.minZ - sizeExtension, SectionPos.sectionToBlockCoord(chunkZ));
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
                    if (collisionShape != Shapes.empty() && collisionShape != null) {
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
        return (edgesHit != 1 || state.hasLargeCollisionShape()) && (edgesHit != 2 || state.getBlock() == Blocks.MOVING_PISTON);
    }

    private static VoxelShape getCollidedShape(AABB entityBox, VoxelShape entityShape, VoxelShape shape, int x, int y, int z) {
        if (shape == Shapes.block()) {
            return entityBox.intersects(x, y, z, (double)x + (double)1.0F, (double)y + (double)1.0F, (double)z + (double)1.0F) ? shape.move((double)x, (double)y, (double)z) : null;
        } else if (shape instanceof VoxelShapeCaster) {
            return ((VoxelShapeCaster)shape).intersects(entityBox, x, y, z) ? shape.move(x, y, z) : null;
        } else {
            shape = shape.move(x, y, z);
            return Shapes.joinIsNotEmpty(shape, entityShape, BooleanOp.AND) ? shape : null;
        }
    }

    private static int expandMin(int coord) {
        return coord - 1;
    }

    private static int expandMax(int coord) {
        return coord + 1;
    }

    private static boolean hasChunkSectionOversizedBlocks(ChunkAccess chunk, int chunkY) {
        if (!BlockStateFlags.ENABLED) {
            return true;
        } else {
            LevelChunkSection section = chunk.getSections()[chunkY];
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