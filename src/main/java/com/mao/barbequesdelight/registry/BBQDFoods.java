package com.mao.barbequesdelight.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class BBQDFoods {
    public static final FoodComponent GRILLED_COD_SKEWER = foods(7, 0.5f, false).build();
    public static final FoodComponent GRILLED_SALMON_SKEWER = foods(7, 0.5f, false).build();
    public static final FoodComponent GRILLED_CHICKEN_SKEWER = foods(7, 0.5f, false).build();
    public static final FoodComponent SKEWER_SANDWICH = foods(14, 0.8f, false).build();
    // effect
    public static final FoodComponent BURNT_FOOD = effect(4, 0, true, StatusEffects.NAUSEA, 200, 0, 1.0f).build();
    public static final FoodComponent GRILLED_LAMB_SKEWER = effect(12, 0.6f, false, StatusEffects.REGENERATION, 20 * 60, 0, 0.5f).build();
    public static final FoodComponent GRILLED_RABBIT_SKEWER = effect(10, 0.5f, false, StatusEffects.JUMP_BOOST, 20 * 60, 0, 1.0f).build();
    public static final FoodComponent GRILLED_PORK_SAUSAGE_SKEWER = effect(8, 0.8f, false, StatusEffects.RESISTANCE, 20 * 90, 0, 0.5f).build();
    public static final FoodComponent GRILLED_POTATO_SKEWER = effect(6, 0.6f, false, ModEffects.NOURISHMENT.get(), 20 * 90, 0, 0.5f).build();
    public static final FoodComponent SKEWER_WRAP = effect(9, 0.8f, false, ModEffects.NOURISHMENT.get(), 20 * 60, 0, 0.5f).build();
    public static final FoodComponent GRILLED_BEEF_SKEWER = effect(10, 0.6f, false, StatusEffects.STRENGTH, 20 * 60, 0, 0.5f).build();
    public static final FoodComponent BIBIMBAP = effect(16, 0.8f, false, ModEffects.COMFORT.get(), 20 *90, 0, 1.0f).build();
    public static final FoodComponent GRILLED_FRUIT_AND_VEGETABLE_SKEWER =effect(8, 0.8F, false, StatusEffects.REGENERATION, 20 * 120, 0, 0.5f).build();


    private static FoodComponent.Builder foods(int hunger, float saturation, boolean alwaysEdible){
        FoodComponent.Builder foodComponent = new FoodComponent.Builder();
        if (alwaysEdible) foodComponent.alwaysEdible();
        return foodComponent.hunger(hunger).saturationModifier(saturation);
    }

    private static FoodComponent.Builder effect(int hunger, float saturation, boolean alwaysEdible, StatusEffect effects, int duration, int amplifier, float chance){
        return foods(hunger, saturation, alwaysEdible).statusEffect(new StatusEffectInstance(effects, duration, amplifier), chance);
    }
}
