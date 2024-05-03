package com.mao.barbequesdelight.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class SkewerItem extends ConsumableItem {

    public SkewerItem(FoodComponent foodComponent, boolean hasFoodEffectTooltip) {
        super(new Settings().food(foodComponent).recipeRemainder(Items.STICK), hasFoodEffectTooltip);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        int time = super.getMaxUseTime(stack);
        return hasSeasoning(stack, "pepper_powder") ? time / 2 : time;
    }

    @Override
    public Text getName(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        MutableText text = super.getName(stack).copy();
        if (nbt != null && !nbt.getString("seasoning").isEmpty()){
            switch (nbt.getString("seasoning")){
                case "chilli_powder" -> {
                  return text.formatted(Formatting.RED);
                }
                case "pepper_powder" -> {
                    return text.copy().formatted(Formatting.GRAY);
                }
                case "cumin_powder" -> {
                    return text.copy().formatted(Formatting.YELLOW);
                }
            }
        }
        return text;
    }

    @Override
    public void affectConsumer(ItemStack stack, World world, LivingEntity user) {
        NbtCompound nbt = stack.getNbt();
        PlayerEntity player = (PlayerEntity) user;
        if (nbt != null && !nbt.getString("seasoning").isEmpty()){
            switch (nbt.getString("seasoning")){
                case "chilli_powder" -> {
                    player.getHungerManager().add(2, 0.01f);
                    user.damage(user.getDamageSources().hotFloor(), 1);
                }
                case "cumin_powder" -> player.setHealth(player.getHealth() + 2);
            }
        }
    }

    protected static boolean hasSeasoning(ItemStack stack, String seasoning){
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.getString("seasoning").equals(seasoning);
    }
}
