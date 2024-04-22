package com.mao.barbequesdelight.registry;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class BBQDVillagerTrades {
    public static void registerBBQDTrades() {
        int price = 3;

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 1, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new ItemStack(Items.EMERALD, random.nextBetween(price - 1, price + 1)),
                    new ItemStack(BBQDItems.CHILLI_POWDER),
                    12, 4, 0.05f));

            factories.add((entity, random) -> new TradeOffer(
                    new ItemStack(Items.EMERALD, random.nextBetween(price - 1, price + 1)),
                    new ItemStack(BBQDItems.CUMIN_POWDER),
                    12, 4, 0.05f));

            factories.add((entity, random) -> new TradeOffer(
                    new ItemStack(Items.EMERALD, random.nextBetween(price - 1, price + 1)),
                    new ItemStack(BBQDItems.PEPPER_POWDER),
                    12, 4, 0.05f));
            });
    }
}
