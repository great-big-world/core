package dev.creoii.greatbigworld.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HollowLogBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock, AdjacentCollision {
    private static final VoxelShape X_SHAPE = Shapes.or(Block.box(0d, 0d, 0d, 16d, 16d, 3d), Block.box(0d, 13d, 0d, 16d, 16d, 16d), Block.box(0d, 0d, 13d, 16d, 16d, 16d), Block.box(0d, 0d, 0d, 16d, 3d, 16d));
    private static final VoxelShape Y_SHAPE = Shapes.or(Block.box(0d, 0d, 0d, 16d, 16d, 3d), Block.box(0d, 0d, 0d, 3d, 16d, 16d), Block.box(0d, 0d, 13d, 16d, 16d, 16d), Block.box(13d, 0d, 0d, 16d, 16d, 16d));
    private static final VoxelShape Z_SHAPE = Shapes.or(Block.box(13d, 0d, 0d, 16d, 16d, 16d), Block.box(0d, 0d, 0d, 3d, 16d, 16d), Block.box(0d, 13d, 0d, 16d, 16d, 16d), Block.box(0d, 0d, 0d, 16d, 3d, 16d));
    private static final VoxelShape X_COLLISION_SHAPE = Shapes.or(Block.box(0d, 0d, 0d, 16d, 16d, 2d), Block.box(0d, 13.75d, 0d, 16d, 16d, 16d), Block.box(0d, 0d, 14d, 16d, 16d, 16d), Block.box(0d, 0d, 0d, 16d, 2.25d, 16d));
    private static final VoxelShape Y_COLLISION_SHAPE = Shapes.or(Block.box(0d, 0d, 0d, 16d, 16d, 2.25d), Block.box(0d, 0d, 0d, 2.25d, 16d, 16d), Block.box(0d, 0d, 13.75d, 16d, 16d, 16d), Block.box(13.75d, 0d, 0d, 16d, 16d, 16d));
    private static final VoxelShape Z_COLLISION_SHAPE = Shapes.or(Block.box(14d, 0d, 0d, 16d, 16d, 16d), Block.box(0d, 0d, 0d, 2d, 16d, 16d), Block.box(0d, 13.75d, 0d, 16d, 16d, 16d), Block.box(0d, 0d, 0d, 16d, 2.25d, 16d));
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public HollowLogBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(AXIS, Direction.Axis.Y).setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> X_SHAPE;
            case Y -> Y_SHAPE;
            case Z -> Z_SHAPE;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AXIS)) {
            case X -> X_COLLISION_SHAPE;
            case Y -> Y_COLLISION_SHAPE;
            case Z -> Z_COLLISION_SHAPE;
        };
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(AXIS, ctx.getClickedFace().getAxis()).setValue(WATERLOGGED, ctx.getLevel().getFluidState(ctx.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    public void onAdjacentEntityCollision(Entity entity, BlockState state, BlockPos pos) {
        if (!entity.isSprinting() || entity.isVehicle() || entity.isPassenger() || entity.isVisuallyCrawling())
            return;
        BlockPos difference = pos.subtract(entity.blockPosition());
        if (Math.abs(difference.getY()) > .15d || difference.equals(BlockPos.ZERO))
            return;
        Vec3 vecDifference = pos.getBottomCenter().subtract(entity.position());
        boolean canEnter = switch (state.getValue(AXIS)) {
            case Y -> false;
            case X -> Math.abs(vecDifference.z()) < .1d && difference.getX() == entity.getDirection().getStepX();
            case Z -> Math.abs(vecDifference.x()) < .1d && difference.getZ() == entity.getDirection().getStepZ();
        };

        if (canEnter) {
            BlockPos entrancePos = BlockPos.containing(entity.position()).relative(state.getValue(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X, 0);
            VoxelShape shape = entity.level().getBlockState(entrancePos).getCollisionShape(entity.level(), entrancePos);
            final double y = shape.isEmpty() ? 0d : shape.max(Direction.Axis.Y);
            if (y >= .375d)
                return;

            Vec3 target = pos.getBottomCenter();
            Vec3 direction = entity.position().subtract(target).normalize().scale(.5d);
            Vec3 destination = target.add(direction.x, Math.min(.2d, y), direction.z);

            entity.setPose(Pose.SWIMMING);
            entity.setSwimming(true);
            entity.setPosRaw(destination.x, destination.y, destination.z);
        }
    }
}
