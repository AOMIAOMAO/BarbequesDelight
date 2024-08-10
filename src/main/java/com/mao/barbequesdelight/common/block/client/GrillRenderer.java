package com.mao.barbequesdelight.common.block.client;

import com.mao.barbequesdelight.common.block.blockentity.GrillBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;

import java.util.Objects;

public class GrillRenderer implements BlockEntityRenderer<GrillBlockEntity> {
    public GrillRenderer(BlockEntityRendererFactory.Context context){}

    @Override
    public void render(GrillBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        DefaultedList<ItemStack> inventory = entity.getItems();
        int intPos = (int) entity.getPos().asLong();

        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty()) {
                Direction direction = entity.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite();

                matrices.push();

                matrices.translate(0.5, 0.96, 0.5);
                float angle = -direction.asRotation();
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                Vec2f itemOffset = entity.getGrillItemOffset(i);
                matrices.translate(itemOffset.x, itemOffset.y, 0.0);
                matrices.scale(0.4f, 0.4f, 0.4f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getFlipped(i) ? 180 : 0));
                int lightAbove = WorldRenderer.getLightmapCoordinates(Objects.requireNonNull(entity.getWorld()), entity.getPos().up());
                MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.FIXED, lightAbove, overlay, matrices, vertexConsumers, entity.getWorld(), intPos + i);

                matrices.pop();
            }
        }
    }
}
