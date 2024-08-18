package com.mao.barbequesdelight.registry;


import com.mao.barbequesdelight.common.item.SeasoningItem;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

public class BBQDEvents {
    public static void registerBBQDEvents(){
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
            if (blockEntity instanceof CuttingBoardBlockEntity be) {
                ItemStack handStack = player.getStackInHand(hand);
                ItemStack storedStack = be.getStoredItem();
                if (handStack.getItem() instanceof SeasoningItem seasoningItem && seasoningItem.canSprinkle(storedStack)) {
                    seasoningItem.sprinkle(storedStack, hitResult.getPos(), player, handStack);
                    return ActionResult.success(world.isClient());
                }
            }
            return ActionResult.PASS;
        });
    }
}
