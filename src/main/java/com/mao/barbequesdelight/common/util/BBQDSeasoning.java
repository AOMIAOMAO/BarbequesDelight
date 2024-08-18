package com.mao.barbequesdelight.common.util;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Formatting;

import java.util.Locale;

public enum BBQDSeasoning {
    CUMIN(Formatting.YELLOW),
    PEPPER(Formatting.GRAY),
    CHILLI(Formatting.RED),
    TOMATO_SAUCE(Formatting.RED),
    HONEY(Formatting.YELLOW);

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

    public void other(LivingEntity entity) {
        if (this == CHILLI) {
            entity.damage(entity.getDamageSources().inFire(), 1);
            if (entity instanceof PlayerEntity player){
                player.getHungerManager().add(2, 0.1f);
            }
        }

        if (this == CUMIN) {
            entity.heal(2);
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
