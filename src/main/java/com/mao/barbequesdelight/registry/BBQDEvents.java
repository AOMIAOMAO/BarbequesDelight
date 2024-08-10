package com.mao.barbequesdelight.registry;


import com.mao.barbequesdelight.common.event.FinishingUsingItemCallback;
import com.mao.barbequesdelight.common.item.SeasoningItem;
import com.mao.barbequesdelight.common.util.BBQDSeasoning;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

public class BBQDEvents {
    public static void registerBBQDEvents(){
        FinishingUsingItemCallback.EVENT.register((world, stack, entity) -> {
            BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
            if (seasoning != null){
                seasoning.other(entity);
            }
            return stack;
        });

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

    public static void registerBBQDClientEvents(){
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
            if (seasoning != null){
                MutableText text = Text.translatable("item.barbequesdelight.skewers.tooltip").formatted(Formatting.GOLD).append(Text.translatable("item.barbequesdelight.flavor." + seasoning.getName()).formatted(seasoning.getColor()));
                lines.add(1, text);
            }
        });
    }
}
