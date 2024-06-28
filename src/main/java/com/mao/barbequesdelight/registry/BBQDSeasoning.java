package com.mao.barbequesdelight.registry;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

public enum BBQDSeasoning {

    CUMIN_SEASONING("cumin_powder", Formatting.YELLOW),
    PEPPER_SEASONING("pepper_powder", Formatting.GRAY),
    CHILLI_SEASONING("chilli_powder", Formatting.RED);

    public final String seasoning;
    public final Formatting color;

    BBQDSeasoning(String seasoning, Formatting color) {
        this.seasoning = seasoning;
        this.color = color;
    }

    public Formatting getColor() {
        return color;
    }

    public String getSeasoning() {
        return seasoning;
    }

    public int pepper(int time){
        return (this == PEPPER_SEASONING) ? (time / 2) : time;
    }

    public void onFinish(PlayerEntity player) {
        if (this == CHILLI_SEASONING) {
            player.damage(player.getDamageSources().inFire(), 1);
            player.getHungerManager().add(2, 0.1f);
        }
        if (this == CUMIN_SEASONING) {
            player.heal(2);
        }
    }

    @Nullable
    public static BBQDSeasoning matching(ItemStack stack) {
        if (stack.getNbt() != null && !stack.getNbt().getString("seasoning").isEmpty()){
            switch (stack.getNbt().getString("seasoning")) {
                case "cumin_powder" -> {
                    return CUMIN_SEASONING;
                }
                case "pepper_powder" -> {
                    return PEPPER_SEASONING;
                }
                case "chilli_powder" -> {
                    return CHILLI_SEASONING;
                }
            }
        }
        return null;
    }
}
