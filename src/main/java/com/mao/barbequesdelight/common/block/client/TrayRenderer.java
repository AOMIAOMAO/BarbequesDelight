package com.mao.barbequesdelight.common.block.client;

import com.mao.barbequesdelight.common.block.blockentity.TrayBlockEntity;
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

public class TrayRenderer implements BlockEntityRenderer<TrayBlockEntity> {
    public TrayRenderer(BlockEntityRendererFactory.Context context){}

    @Override
    public void render(TrayBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        DefaultedList<ItemStack> inventory = entity.getItems();
        int intPos = (int) entity.getPos().asLong();

        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack = inventory.get(i);
            if (!itemStack.isEmpty()) {
                Direction direction = entity.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite();

                for (int j = 0; j < this.getModelCount(itemStack); ++j){
                    matrices.push();

                    float xOffset = direction.getAxis() == Direction.Axis.Z ? 0.8f - (j*0.2f) : 0.5f;
                    float zOffset = direction.getAxis() == Direction.Axis.X ? 0.8f - (j*0.2f) : 0.5f;
                    matrices.translate(xOffset, 0.075, zOffset);
                    float angle = -direction.asRotation();
                    matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                    Vec2f itemOffset = entity.getTrayItemOffset(i);
                    matrices.translate(itemOffset.x, itemOffset.y, 0.0);
                    matrices.scale(0.375f, 0.375f, 0.375f);
                    int lightAbove = WorldRenderer.getLightmapCoordinates(Objects.requireNonNull(entity.getWorld()), entity.getPos().up());
                    MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.FIXED, lightAbove, overlay, matrices, vertexConsumers, entity.getWorld(), intPos + i);

                    matrices.pop();
                }
            }
        }
    }

    private int getModelCount(ItemStack stack){
        if(stack.getCount() >= 64){
            return 4;
        } else if (stack.getCount() >=48) {
            return 3;
        }else{
            return stack.getCount() >=32 ? 2 : 1;
        }
    }
}
