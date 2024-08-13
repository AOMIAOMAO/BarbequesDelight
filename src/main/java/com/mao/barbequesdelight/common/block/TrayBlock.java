package com.mao.barbequesdelight.common.block;

import com.mao.barbequesdelight.common.block.blockentity.TrayBlockEntity;
import com.mao.barbequesdelight.registry.BBQDBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class TrayBlock extends BlockWithEntity {
    public static final VoxelShape SHAPE;
    public static final BooleanProperty SUPPORT = BooleanProperty.of("support");

    public TrayBlock() {
        super(Settings.copy(Blocks.OAK_PLANKS));
        BlockState blockState = this.stateManager.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH).with(SUPPORT, false);
        this.setDefaultState(blockState);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (world.getBlockEntity(pos) instanceof TrayBlockEntity tray){
            if (!stack.isEmpty()){
                for (int i = 0; i < tray.size(); ++i) {
                    ItemStack stack1 = tray.getStack(i);
                    if (stack1.isEmpty()){
                        tray.setStack(i, stack.split(stack.getCount()));
                        return ActionResult.success(world.isClient());
                    }
                    if (ItemStack.areItemsEqual(stack, stack1) && stack1.getCount() < stack1.getMaxCount()){
                        stack1.setCount(stack1.getCount() + stack.split(1).getCount());
                        return ActionResult.success(world.isClient());
                    }
                }
            }else {
                for (int i = tray.size()-1; i >=0; i--) {
                    ItemStack stack1 = tray.getStack(i);
                    if (!stack1.isEmpty()){
                        int count = player.isSneaking() ? stack1.getCount() : 1;
                        player.getInventory().offerOrDrop(stack1.split(count));
                        return ActionResult.success(world.isClient());
                    }
                }
            }
            tray.inventoryChanged();
        }
        return ActionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrayBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HorizontalFacingBlock.FACING, SUPPORT);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState()
                .with(HorizontalFacingBlock.FACING, context.getHorizontalPlayerFacing().getOpposite())
                .with(SUPPORT, getTrayState(context.getWorld(), context.getBlockPos()));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos floorPos = pos.down();
        return Block.hasTopRim(world, floorPos) || Block.sideCoversSmallSquare(world, floorPos, Direction.UP) || this.getTrayState((WorldAccess) world, pos);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(HorizontalFacingBlock.FACING, rotation.rotate(state.get(HorizontalFacingBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(HorizontalFacingBlock.FACING)));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess world, BlockPos currentPos, BlockPos facingPos) {
        if (facing.getAxis().equals(Direction.Axis.Y)) {
            return stateIn.with(SUPPORT, getTrayState(world, currentPos));
        }
        return facing == Direction.DOWN && !stateIn.canPlaceAt(world, currentPos)
                ? Blocks.AIR.getDefaultState()
                : super.getStateForNeighborUpdate(stateIn, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TrayBlockEntity) {
            if (world instanceof ServerWorld) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
            }
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private boolean getTrayState(WorldAccess world, BlockPos pos) {
        BlockPos pos1 = pos.add(0, -2, 0);
        return world.getBlockState(pos.down()).getBlock() == BBQDBlocks.TRAY && world.getBlockState(pos1).getBlock() != BBQDBlocks.TRAY;
    }

    static {
        SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(0, 0, 0, 16, 3, 16),
                VoxelShapes.union(Block.createCuboidShape(1, 1, 1, 15, 3, 15)),
                BooleanBiFunction.ONLY_FIRST);
    }
}
