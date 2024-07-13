package com.mao.barbequesdelight;

import com.mao.barbequesdelight.common.block.client.GrillBlockEntityRenderer;
import com.mao.barbequesdelight.common.block.client.IngredientsBasinBlockEntityRenderer;
import com.mao.barbequesdelight.registry.BBQDBlocks;
import com.mao.barbequesdelight.registry.BBQDEntityTypes;
import com.mao.barbequesdelight.registry.BBQDEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(EnvType.CLIENT)
public class BarbequesDelightClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BBQDBlocks.GRILL, RenderLayer.getCutout());
        BlockEntityRendererFactories.register(BBQDEntityTypes.GRILL_BLOCK_ENTITY, GrillBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BBQDEntityTypes.INGREDIENTS_BASIN_BLOCK_ENTITY, IngredientsBasinBlockEntityRenderer::new);
        //Event
        BBQDEvents.registerBBQDClientEvents();
    }
}
