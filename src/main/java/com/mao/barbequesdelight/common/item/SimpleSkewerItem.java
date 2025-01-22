package com.mao.barbequesdelight.common.item;

import com.mao.barbequesdelight.common.util.BBQDSeasoning;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.List;

public class SimpleSkewerItem extends ConsumableItem {
    public SimpleSkewerItem(FoodComponent foodComponent, boolean hasTooltip) {
        super(new Settings().food(foodComponent).recipeRemainder(Items.STICK), hasTooltip);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
        if (seasoning != null) return seasoning.pepper(super.getMaxUseTime(stack));
        return super.getMaxUseTime(stack);
    }

    @Override
    public void affectConsumer(ItemStack stack, World world, LivingEntity le) {
        super.affectConsumer(stack, world, le);
        BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
        if (seasoning != null) seasoning.onFinish(le);
    }

    @Override
    public Text getName(ItemStack stack) {
        BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
        if (seasoning != null)return Text.translatable("item.barbequesdelight."+ seasoning.getName() +".title").append(Text.translatable(stack.getTranslationKey()));
        return super.getName(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World level, List<Text> tooltip, TooltipContext isAdvanced) {
        super.appendTooltip(stack, level, tooltip, isAdvanced);
        BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
        if (seasoning != null){
            MutableText text = Text.translatable("item.barbequesdelight.skewers.tooltip").formatted(Formatting.GOLD).append(Text.translatable("item.barbequesdelight.flavor." + seasoning.getName()).formatted(seasoning.getColor()));
            tooltip.add(1, text);
        }
    }
}
