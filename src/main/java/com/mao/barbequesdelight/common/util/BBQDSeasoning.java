package com.mao.barbequesdelight.common.util;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;

import java.util.Locale;

public enum BBQDSeasoning {
    CUMIN(Formatting.YELLOW),
    PEPPER(Formatting.GRAY),
    CHILLI(Formatting.DARK_RED),
    BUFFALO(Formatting.RED),
    HONEY_MUSTARD(Formatting.YELLOW),
    BARBECUE(Formatting.GOLD);

    public final Formatting color;
    public final String name;

    BBQDSeasoning(Formatting color) {
        this.color = color;
        this.name = name().toLowerCase(Locale.ROOT);
    }

    public Formatting getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public int pepper(int time){
        return (this == PEPPER) ? (time / 2) : time;
    }

    public void onFinish(LivingEntity le) {
        if (this == CHILLI) {
            le.damage(le.getDamageSources().inFire(), 2);
            if (le instanceof PlayerEntity player)player.getHungerManager().add(2, 0.5f);
        }

        if (this == CUMIN) le.heal(2);

        if (this == BUFFALO){
            le.getStatusEffects().forEach(effect -> {
                int d = effect.getDuration();
                int a = effect.getAmplifier();
                StatusEffect e = effect.getEffectType();
                le.getStatusEffects().clear();
                le.addStatusEffect(new StatusEffectInstance(e, d/2, a + 1));
            });
        }
    }

    @Nullable
    public static BBQDSeasoning matching(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt != null && !nbt.getString("seasoning").isEmpty()) {
            String str = nbt.getString("seasoning");
            try {
                return Enum.valueOf(BBQDSeasoning.class, str);
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
