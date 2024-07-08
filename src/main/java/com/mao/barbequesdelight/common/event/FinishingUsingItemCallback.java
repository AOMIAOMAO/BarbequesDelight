package com.mao.barbequesdelight.common.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface FinishingUsingItemCallback {

    Event<FinishingUsingItemCallback> EVENT = EventFactory.createArrayBacked(FinishingUsingItemCallback.class, listeners -> (world, stack, entity) -> {
        for (FinishingUsingItemCallback listener : listeners) {
            listener.finishUsing(world, stack, entity);
        }

        return stack;
    });

    ItemStack finishUsing(World world, ItemStack stack, LivingEntity entity);
}
