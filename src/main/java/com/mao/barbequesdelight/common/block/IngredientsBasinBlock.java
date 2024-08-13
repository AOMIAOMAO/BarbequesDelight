package com.mao.barbequesdelight.common.block;

import com.mao.barbequesdelight.common.block.blockentity.IngredientsBasinBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
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

public class IngredientsBasinBlock extends BlockWithEntity {
    public static final VoxelShape OUTER, SHAPE_X, SHAPE_Z;

    public IngredientsBasinBlock() {
        super(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS));
        BlockState blockState = this.stateManager.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH);
        this.setDefaultState(blockState);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IngredientsBasinBlockEntity basin) {
            int slot = basin.getSlotForHitting(hit, world);
            if (slot < basin.size()) {
                ItemStack itemInBasin = basin.getStack(slot);
                ItemStack stack = player.getStackInHand(hand);

                if (itemInBasin.isEmpty() && !stack.isEmpty()) {
                    if (!basin.skewer(player, slot, hand)) {
                        basin.setStack(slot, stack.split(stack.getCount()));
                        basin.inventoryChanged();
                        world.playSound(null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        return ActionResult.success(world.isClient());
                    }
                    world.playSound(null, pos, SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                } else if (basin.skewer(player, slot, hand)) {
                    world.playSound(null, pos, SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                } else {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    player.getInventory().offerOrDrop(itemInBasin.split(itemInBasin.getCount()));
                }
                basin.inventoryChanged();
                return ActionResult.success(world.isClient());
            }
        }
        return ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HorizontalFacingBlock.FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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
        return facing == Direction.DOWN && !stateIn.canPlaceAt(world, currentPos)
                ? Blocks.AIR.getDefaultState()
                : super.getStateForNeighborUpdate(stateIn, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockPos floorPos = pos.down();
        return Block.hasTopRim(world, floorPos) || Block.sideCoversSmallSquare(world, floorPos, Direction.UP);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HorizontalFacingBlock.FACING).getAxis() == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IngredientsBasinBlockEntity) {
            if (world instanceof ServerWorld) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
            }
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new IngredientsBasinBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    static {
        OUTER = Block.createCuboidShape(1, 0, 1, 15, 4, 15);
        SHAPE_Z = VoxelShapes.combineAndSimplify(OUTER,
                VoxelShapes.union(Block.createCuboidShape(2, 1, 2, 7.5, 4, 14),
                        Block.createCuboidShape(8.5, 1, 2, 14, 4, 14)),
                BooleanBiFunction.ONLY_FIRST);

        SHAPE_X = VoxelShapes.combineAndSimplify(OUTER,
                VoxelShapes.union(Block.createCuboidShape(2, 1, 2, 14, 4, 7.5),
                        Block.createCuboidShape(2, 1, 8.5, 14, 4, 14)),
                BooleanBiFunction.ONLY_FIRST);
    }
}
