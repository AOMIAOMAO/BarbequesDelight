package com.mao.barbequesdelight.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface FinishUsingItemCallback {

    Event<FinishUsingItemCallback> EVENT = EventFactory.createArrayBacked(FinishUsingItemCallback.class, listeners -> (stack, world, entity) -> {
        for (FinishUsingItemCallback listener : listeners) {
            listener.finishUseing(stack, world, entity);
        }

        return stack;
    });

    ItemStack finishUseing(ItemStack stack, World world, LivingEntity entity);
}
