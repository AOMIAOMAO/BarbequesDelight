package com.mao.barbequesdelight.mixin;

import com.mao.barbequesdelight.registry.BBQDSeasoning;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow public abstract int getMaxUseTime(ItemStack stack);

    @Shadow @Nullable public abstract FoodComponent getFoodComponent();

    @Inject(at = @At("HEAD"), method = "finishUsing")
    private void effect(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir){
        BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
        if (seasoning != null && user instanceof PlayerEntity player){
            seasoning.onFinish(player);
        }
    }

    @Inject(at = @At("HEAD"), method = "getMaxUseTime", cancellable = true)
    private void effect(ItemStack stack, CallbackInfoReturnable<Integer> cir){
        BBQDSeasoning seasoning = BBQDSeasoning.matching(stack);
        if (seasoning != null && stack.isFood()) {
            cir.setReturnValue(seasoning.pepper(this.getFoodComponent().isSnack() ? 16 : 32));
        }
    }

}
