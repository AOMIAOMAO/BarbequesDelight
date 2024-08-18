package com.mao.barbequesdelight.common.item;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.common.util.BBQDSeasoning;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SeasoningItem extends Item {
    private final BBQDSeasoning seasoning;

    public SeasoningItem(BBQDSeasoning seasoning) {
        super(new Settings().maxDamage(64));
        this.seasoning = seasoning;
    }

    public BBQDSeasoning getSeasoning() {
        return seasoning;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.barbequesdelight." + getSeasoning().getName() + ".tooltip").formatted(Formatting.YELLOW));
    }

    public void sprinkle(ItemStack skewer, Vec3d pos, PlayerEntity player, ItemStack stackInHand){
        skewer.getOrCreateNbt().putString("seasoning", getSeasoning().name());
        player.playSound(SoundEvents.BLOCK_SAND_BREAK, 0.7f, 1.0f);
        Integer color = seasoning.color.getColorValue();
        int col = color == null ? 0 : color;
        player.getWorld().addParticle(new DustParticleEffect(Vec3d.unpackRgb(col).toVector3f(), 1.5f),
                pos.x, pos.y, pos.z, 8, 0d, 0);
        stackInHand.damage(1, player, player1 -> player1.sendToolBreakStatus(player1.getActiveHand()));
    }

    public boolean canSprinkle(ItemStack storedStack) {
        if (storedStack.isEmpty())
            return false;
        if (!(storedStack.getItem() instanceof SkewerItem))
            return false;
        return storedStack.getNbt() == null || !storedStack.getNbt().contains("seasoning");
    }
}
