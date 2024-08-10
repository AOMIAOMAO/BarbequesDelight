package com.mao.barbequesdelight.registry;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.common.block.blockentity.GrillBlockEntity;
import com.mao.barbequesdelight.common.block.blockentity.IngredientsBasinBlockEntity;
import com.mao.barbequesdelight.common.block.blockentity.TrayBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BBQDEntityTypes {
    public static final BlockEntityType<GrillBlockEntity> GRILL = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            BarbequesDelight.asID("grill_block_entity"),
            BlockEntityType.Builder.create(GrillBlockEntity::new, BBQDBlocks.GRILL).build(null)
    );

    public static final BlockEntityType<IngredientsBasinBlockEntity> INGREDIENTS_BASIN = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            BarbequesDelight.asID("ingredients_basin_block_entity"),
            BlockEntityType.Builder.create(IngredientsBasinBlockEntity::new, BBQDBlocks.INGREDIENTS_BASIN).build(null)
    );

    public static final BlockEntityType<TrayBlockEntity> TRAY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            BarbequesDelight.asID("tray_block_entity"),
            BlockEntityType.Builder.create(TrayBlockEntity::new, BBQDBlocks.TRAY).build(null)
    );

    public static void registerBBQDEntityTypes(){
        BarbequesDelight.LOGGER.debug("Register BBQD EntityTypes For" + BarbequesDelight.MODID);
    }
}
