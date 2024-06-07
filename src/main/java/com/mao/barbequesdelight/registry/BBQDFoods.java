package com.mao.barbequesdelight.registry;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class BBQDFoods {
    public static final FoodComponent COOKED_COD_SKEWER = new FoodComponent.Builder().hunger(7).saturationModifier(0.5f).build();
    public static final FoodComponent COOKED_SALMON_SKEWER = new FoodComponent.Builder().hunger(7).saturationModifier(0.5f).build();
    public static final FoodComponent COOKED_CHICKEN_SKEWER = new FoodComponent.Builder().hunger(7).saturationModifier(0.5f).build();
    public static final FoodComponent COOKED_MUSHROOM_SKEWER = new FoodComponent.Builder().hunger(6).saturationModifier(0.4f).build();
    public static final FoodComponent COOKED_BEEF_SKEWER = new FoodComponent.Builder().hunger(8).saturationModifier(0.6f).build();
    public static final FoodComponent SKEWER_SANDWICH = new FoodComponent.Builder().hunger(14).saturationModifier(0.8f).build();

    // effect

    public static final FoodComponent COOKED_LAMB_SKEWER = new FoodComponent.Builder().hunger(12).saturationModifier(0.6f).statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20 * 90, 0), 0.5f).build();
    public static final FoodComponent COOKED_RABBIT_SKEWER = new FoodComponent.Builder().hunger(10).saturationModifier(0.5f).statusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20 * 60, 0), 1.0f).build();
    public static final FoodComponent GRILLED_PORK_SAUSAGE_SKEWER = new FoodComponent.Builder().hunger(8).saturationModifier(0.8f).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20 * 90, 0), 0.5f).build();
    public static final FoodComponent BAKED_POTATO_SKEWER = new FoodComponent.Builder().hunger(6).saturationModifier(0.6f).statusEffect(new StatusEffectInstance(ModEffects.NOURISHMENT.get(), 20 * 90, 0), 0.5f).build();
    public static final FoodComponent SKEWER_WRAP = new FoodComponent.Builder().hunger(9).saturationModifier(0.8f).statusEffect(new StatusEffectInstance(ModEffects.NOURISHMENT.get(), 20 * 60, 0), 0.5f).build();
}
