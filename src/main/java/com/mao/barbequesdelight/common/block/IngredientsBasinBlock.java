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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class IngredientsBasinBlock extends BlockWithEntity {

    public IngredientsBasinBlock() {
        super(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS));
        BlockState blockState = this.stateManager.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH);
        this.setDefaultState(blockState);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof IngredientsBasinBlockEntity basin){
            int i = basin.getSlotForHitting(hit, world);
            if (i != basin.size()){
                ItemStack itemInBasin = basin.getStack(i);
                ItemStack stack = player.getStackInHand(hand);

                if (itemInBasin.isEmpty() && !stack.isEmpty() && !basin.skewer(player, i)){
                    basin.setStack(i, stack.split(stack.getCount()));
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    return ActionResult.SUCCESS;
                }else if (basin.skewer(player, i)){
                    world.playSound(null, pos, SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    return ActionResult.SUCCESS;
                }else {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    player.getInventory().offerOrDrop(itemInBasin.split(itemInBasin.getCount()));
                    return ActionResult.SUCCESS;
                }
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.CONSUME;
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0, 0.0625, 0.9375, 0.0625, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.875, 0.0625, 0.125, 0.9375, 0.25, 0.875));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.0625, 0.875, 0.9375, 0.25, 0.9375));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.0625, 0.0625, 0.9375, 0.25, 0.125));
        shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.0625, 0.0625, 0.125, 0.125, 0.25, 0.875));

        return shape;
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
}
