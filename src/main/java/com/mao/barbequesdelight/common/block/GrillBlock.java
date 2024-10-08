package com.mao.barbequesdelight.common.block;

import com.mao.barbequesdelight.common.block.blockentity.GrillBlockEntity;
import com.mao.barbequesdelight.common.recipe.GrillingRecipe;
import com.mao.barbequesdelight.registry.BBQDEntityTypes;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.Optional;

public class GrillBlock extends BlockWithEntity {
    private static final VoxelShape SHAPE;

    public GrillBlock() {
        super(Settings.copy(ModBlocks.SKILLET.get()));
        BlockState blockState = this.stateManager.getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH);
        this.setDefaultState(blockState);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GrillBlockEntity grill) {
            if (grill.isBarbecuing()) {
                double x = (double)pos.getX() + 0.5;
                double y = pos.getY();
                double z = (double)pos.getZ() + 0.5;
                if (random.nextInt(8) == 0) {
                    world.playSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.BLOCKS, 0.4F, random.nextFloat() * 0.2F + 0.9F, false);
                }
            }
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof GrillBlockEntity) {
            if (world instanceof ServerWorld) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
            }
            world.updateComparators(pos, this);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
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

    //BlockEntity

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof GrillBlockEntity grill){
            int i = grill.getSlotForHitting(hit, world);
            ItemStack stack = player.getStackInHand(hand);

            if (i < grill.size()) {
                ItemStack grillItems = grill.getStack(i);
                Optional<GrillingRecipe> optional = grill.findMatchingRecipe(stack);
                Optional<CampfireCookingRecipe> campfireOptional = grill.findMatchingCampfireRecipe(stack);

                if (!player.isSneaking()){
                    if (!stack.isEmpty() && grillItems.isEmpty() && (optional.isPresent() || campfireOptional.isPresent())) {
                        grill.setStack(i, stack.split(1));

                        optional.ifPresent(recipe -> grill.setBarbecuing(i, recipe.getGrillingtime()));
                        campfireOptional.ifPresent(recipe -> grill.setBarbecuing(i, recipe.getCookTime() - 20 * 10));

                        world.playSound(null, pos, SoundEvents.BLOCK_LANTERN_PLACE, SoundCategory.BLOCKS, 0.7F, 1.0F);
                        return ActionResult.success(world.isClient());
                    } else if (!grillItems.isEmpty()) {
                        player.getInventory().offerOrDrop(grillItems.split(1));
                        grill.inventoryChanged();
                        return ActionResult.success(world.isClient());
                    }
                } else if (grill.flip(i)){
                    world.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundCategory.BLOCKS, 0.8F, 1.0F);
                    return ActionResult.SUCCESS;
                }
            }
        }
        return ActionResult.PASS;
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> blockEntity) {
        return GrillBlock.checkType(world, blockEntity, BBQDEntityTypes.GRILL);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends GrillBlockEntity> expectedType) {
        return world.isClient ? GrillBlock.checkType(givenType, expectedType, GrillBlockEntity::animationTick) : GrillBlock.checkType(givenType, expectedType, GrillBlockEntity::tick);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GrillBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    static {
        SHAPE = VoxelShapes.union(
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 10.0D, 1.0D),
                Block.createCuboidShape(0.0D, 0.0D, 15.0D, 1.0D, 10.0D, 16.0D),
                Block.createCuboidShape(15.0D, 0.0D, 15.0D, 16.0D, 10.0D, 16.0D),
                Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 10.0D, 1.0D),
                Block.createCuboidShape(0.0D, 10.0D, 0.0D, 16.0D, 16.0D, 16.0D)
        );
    }
}
