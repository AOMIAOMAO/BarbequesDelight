package com.mao.barbequesdelight.common.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeasoningItem extends Item {

    private final String seasoning;

    public SeasoningItem(String seasoning) {
        super(new Settings().maxDamage(64));
        this.seasoning = seasoning;
    }

    public String getSeasoning() {
        return seasoning;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.barbequesdelight." + seasoning + ".tooltip").formatted(Formatting.YELLOW));
    }

    public void sprinkle(ItemStack skewer, PlayerEntity player){
        NbtCompound nbt = new NbtCompound();
        nbt.putString("seasoning", getSeasoning());
        skewer.setNbt(nbt);
        player.playSound(SoundEvents.BLOCK_SAND_BREAK, 0.7f, 1.0f);
    }
}
