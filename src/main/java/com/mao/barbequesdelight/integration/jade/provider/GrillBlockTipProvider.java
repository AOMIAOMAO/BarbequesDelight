package com.mao.barbequesdelight.integration.jade.provider;

import com.google.common.collect.Lists;
import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.common.block.blockentity.GrillBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Accessor;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.view.*;

import java.util.List;

public class GrillBlockTipProvider implements IServerExtensionProvider<Object, ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {

    public static final GrillBlockTipProvider INSTANCE = new GrillBlockTipProvider();

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> list) {
        return ClientViewGroup.map(list, (stack) -> {
            String text = null;
            if (stack.getNbt() != null && stack.getNbt().contains("barbecuing")) {
                text = IThemeHelper.get().seconds(stack.getNbt().getInt("barbecuing")).getString();
            }
            return new ItemView(stack, text);
        }, null);
    }

    @Override
    public @Nullable List<ViewGroup<ItemStack>> getGroups(ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld, Object o, boolean b) {
        if (o instanceof GrillBlockEntity grill) {
            List<ItemStack> list = Lists.newArrayList();

            for(int i = 0; i < grill.barbecuingTimes.length; ++i) {
                ItemStack stack = grill.getStack(i);
                if (!stack.isEmpty()) {
                    stack = stack.copy();
                    stack.getOrCreateNbt().putInt("barbecuing", grill.barbecuingTimes[i]);
                    list.add(stack);
                }
            }

            return List.of(new ViewGroup<>(list));
        } else {
            return null;
        }
    }

    @Override
    public Identifier getUid() {
        return BarbequesDelight.asID("grill");
    }
}
