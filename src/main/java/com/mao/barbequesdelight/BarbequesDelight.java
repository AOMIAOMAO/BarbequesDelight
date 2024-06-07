package com.mao.barbequesdelight;

import com.mao.barbequesdelight.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarbequesDelight implements ModInitializer {

    public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, asID("main"));
    public static final String MODID = "barbequesdelight";
    public static final Logger LOGGER = LoggerFactory.getLogger(BarbequesDelight.class);

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder().displayName(Text.translatable("itemGroup.barbequesdelight.main")).icon(() -> new ItemStack(BBQDItems.GRILL)).build());

        BBQDItems.registerBBQDItems();
        BBQDBlocks.registerBBQDBlocks();
        BBQDRecipes.registerBBQDRecipes();
        BBQDEntityTypes.registerBBQDEntityTypes();
        BBQDVillagerTrades.registerBBQDTrades();
        CompostingChanceRegistry.INSTANCE.add(BBQDItems.BURNT_FOOD, 0.2f);

        LOGGER.info("Barbeque's Delight is loaded");
    }

    public static Identifier asID(String id){
        return new Identifier(MODID, id);
    }
}
