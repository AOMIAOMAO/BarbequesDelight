package com.mao.barbequesdelight.common.block.blockentity;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface BlockEntityInv extends SidedInventory {

    DefaultedList<ItemStack> getItems();

    @Override
    default int size() {
        return getItems().size();
    }

    @Override
    default boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    default void setStack(int slot, ItemStack stack) {
        ItemStack itemStack = stack.copy();
        stack.setCount(1);
        getItems().set(slot, itemStack);
        markDirty();
    }

    @Override
    default ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    default void clear() {
        for (int i = 0; i < size(); i++) {
            removeStack(i);
        }
    }

    @Override
    default boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    default int[] getAvailableSlots(Direction side) {
        return new int[0];
    }

    @Override
    default boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    default boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    default int getSlotForHitting(BlockHitResult hit, World world){
        if (hit.getType() == HitResult.Type.BLOCK && hit.getSide() == Direction.UP) {
            Vec3d pos1 = hit.getPos();
            Direction facing = world.getBlockState(hit.getBlockPos()).get(HorizontalFacingBlock.FACING).getOpposite();
            BlockPos pos = hit.getBlockPos();
            boolean left = false;
            boolean right = false;
            switch (facing) {
                case NORTH -> {
                    left = pos1.x - (double) pos.getX() < 0.5D;
                    right = pos1.x - (double) pos.getX() > 0.5D;
                }
                case SOUTH -> {
                    left = pos1.x - (double) pos.getX() > 0.5D;
                    right = pos1.x - (double) pos.getX() < 0.5D;
                }
                case EAST -> {
                    left = pos1.z - (double) pos.getZ() < 0.5D;
                    right = pos1.z - (double) pos.getZ() > 0.5D;
                }
                case WEST -> {
                    left = pos1.z - (double) pos.getZ() > 0.5D;
                    right = pos1.z - (double) pos.getZ() < 0.5D;
                }
            }
            if (left) {
                return 0;
            } else if (right) {
                return 1;
            }
        }
        return size();
    }
}
