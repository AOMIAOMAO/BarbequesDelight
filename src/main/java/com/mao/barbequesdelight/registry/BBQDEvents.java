package com.mao.barbequesdelight.registry;


import com.mao.barbequesdelight.common.event.FinishingUsingItemCallback;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BBQDEvents {
    public static void registerBBQDEvents(){
        FinishingUsingItemCallback.EVENT.register((world, stack, entity) -> {
            BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
            if (seasoning != null && entity instanceof PlayerEntity player){
                seasoning.other(player);
            }
            return stack;
        });
    }

    public static void registerBBQDClientEvents(){
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
            if (seasoning != null){
                MutableText text = Text.translatable("item.barbequesdelight.skewers.tooltip").formatted(Formatting.YELLOW).append(Text.translatable("item.barbequesdelight." + seasoning.getSeasoning()).formatted(seasoning.getColor()));
                lines.add(1, text);
            }
        });
    }
}
