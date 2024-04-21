package com.mao.barbequesdelight;

import com.mao.barbequesdelight.common.block.client.GrillBlockEntityRenderer;
import com.mao.barbequesdelight.common.block.client.IngredientsBasinBlockEntityRenderer;
import com.mao.barbequesdelight.registry.BBQDBlocks;
import com.mao.barbequesdelight.registry.BBQDEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class BarbequesDelightClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BBQDBlocks.GRILL, RenderLayer.getTranslucent());

        BlockEntityRendererFactories.register(BBQDEntityTypes.GRILL_BLOCK_ENTITY, GrillBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(BBQDEntityTypes.INGREDIENTS_BASIN_BLOCK_ENTITY, IngredientsBasinBlockEntityRenderer::new);
    }
}
