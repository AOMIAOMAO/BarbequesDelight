package com.mao.barbequesdelight.common.block.blockentity;

import com.mao.barbequesdelight.common.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.registry.BBQDEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class IngredientsBasinBlockEntity extends BlockEntity implements BlockEntityInv{
    public final DefaultedList<ItemStack> items;

    public IngredientsBasinBlockEntity(BlockPos pos, BlockState state) {
        super(BBQDEntityTypes.INGREDIENTS_BASIN, pos, state);
        this.items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    }

    public boolean skewer(PlayerEntity user, int slot){
        ItemStack stack = user.getMainHandStack();
        ItemStack basin = getStack(slot);
        ItemStack garnishes = user.getOffHandStack();

        Optional<SkeweringRecipe> optional = Objects.requireNonNull(getWorld()).getRecipeManager().getFirstMatch(SkeweringRecipe.Type.INSTANCE, new SimpleInventory(basin), getWorld());
        if (optional.isEmpty()) {
            return false;
        }
        SkeweringRecipe recipe = optional.get();
        if (stack.isOf(recipe.getTool().getItem()) && garnishes.isOf(recipe.getSideDishes().getItem()) && garnishes.getCount() >= recipe.getSideDishes().getCount()) {
            if (world != null) {
                ItemStack result = world.getRecipeManager().getAllMatches(SkeweringRecipe.Type.INSTANCE, new SimpleInventory(basin), world).stream().map(Srecipe -> Srecipe.craft(this, world.getRegistryManager())).findAny().orElse(basin);

                basin.decrement(recipe.getIngredientCount());
                garnishes.decrement(recipe.getSideDishes().getCount());
                stack.decrement(1);
                user.getInventory().offerOrDrop(result);
                return true;
            }
            return false;
        }
        return false;
    }

    public Vec2f getBasinItemOffset(int index) {
        final float xOffset = .2f;
        final float yOffset = .0f;
        final Vec2f[] offsets = {new Vec2f(xOffset, yOffset), new Vec2f(-xOffset, yOffset)};

        return offsets[index];
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getMaxCountPerStack() {
        return 64;
    }

    public void inventoryChanged() {
        this.markDirty();
        if (world != null) {
            world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.items);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.items.clear();
        Inventories.readNbt(nbt, this.items);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.items, true);
        return nbtCompound;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
