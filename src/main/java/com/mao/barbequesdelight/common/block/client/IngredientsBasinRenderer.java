package com.mao.barbequesdelight.common.block.client;

import com.mao.barbequesdelight.common.block.blockentity.IngredientsBasinBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;

import java.util.Objects;
import java.util.Random;

public class IngredientsBasinRenderer implements BlockEntityRenderer<IngredientsBasinBlockEntity> {

    private final Random random = new Random();

    public IngredientsBasinRenderer(BlockEntityRendererFactory.Context context){}

    @Override
    public void render(IngredientsBasinBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        DefaultedList<ItemStack> inventory = entity.getItems();
        int intPos = (int) entity.getPos().asLong();

        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack stack = entity.getStack(i);
            Direction direction = entity.getCachedState().get(HorizontalFacingBlock.FACING).getOpposite();
            int seed = stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getCount();
            this.random.setSeed(seed);

            for (int j = 0; j < getModelCount(stack); ++j){
                matrices.push();

                float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.2F;
                float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.2F;
                float angle = -direction.asRotation();
                renderPose(direction, j, xOffset, zOffset, matrices);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
                Vec2f itemOffset = entity.getBasinItemOffset(i);
                matrices.translate(itemOffset.x, itemOffset.y, 0.0);
                matrices.scale(0.375f, 0.375f, 0.375f);
                int lightAbove = WorldRenderer.getLightmapCoordinates(Objects.requireNonNull(entity.getWorld()), entity.getPos().up());
                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, lightAbove, overlay, matrices, vertexConsumers, entity.getWorld(), intPos + i);

                matrices.pop();
            }
        }
    }

    protected int getModelCount(ItemStack stack) {
        if (stack.getCount() > 48) {
            return 12;
        } else if (stack.getCount() > 32) {
            return 10;
        } else if (stack.getCount() > 16) {
            return 8;
        } else if (stack.getCount() > 8) {
            return 6;
        } else {
            return stack.getCount() > 1 ? 2 : 1;
        }
    }

    protected static void renderPose(Direction direction, int count, float xOffset, float zOffset, MatrixStack matrices){
        switch (direction.getOpposite()){
            case SOUTH -> {
                matrices.translate(0.5 + xOffset, 0.2, 0.15 + (double) count / 20);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) -(20 + count * 4)));
            }
            case NORTH -> {
                matrices.translate(0.5 + xOffset, 0.2, 0.85 - (double) count / 20);
                matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees((float) -(20 + count * 4)));
            }
            case EAST -> {
                matrices.translate(0.15 + (double) count / 20, 0.2, 0.5 + zOffset);
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) (20 + count * 4)));
            }
            case WEST -> {
                matrices.translate(0.85 - (double) count / 20, 0.2, 0.5 + zOffset);
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) -(20 + count * 4)));
            }
        }
    }
}
