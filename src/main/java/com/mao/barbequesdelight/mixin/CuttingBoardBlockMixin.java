package com.mao.barbequesdelight.mixin;

import com.mao.barbequesdelight.common.item.SeasoningItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

@Mixin(CuttingBoardBlock.class)
public abstract class CuttingBoardBlockMixin {

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void seasoning(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir){
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CuttingBoardBlockEntity be) {
            ItemStack handStack = player.getStackInHand(hand);
            ItemStack storedStack = be.getStoredItem();
            if (handStack.getItem() instanceof SeasoningItem seasoningItem && seasoningItem.canSprinkle(storedStack)) {
                seasoningItem.sprinkle(storedStack, hit.getPos(), player, handStack);
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }
}
