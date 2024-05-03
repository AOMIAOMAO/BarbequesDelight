package com.mao.barbequesdelight.common.block.blockentity;

import com.mao.barbequesdelight.common.recipe.BarbecuingRecipe;
import com.mao.barbequesdelight.registry.BBQDEntityTypes;
import com.mao.barbequesdelight.registry.BBQDItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;

import java.util.Optional;

public class GrillBlockEntity extends BlockEntity implements BlockEntityInv, HeatableBlockEntity {

    private final int[] barbecuingTime;
    private final int[] barbecuingTotalTime;
    private final boolean[] flipped;
    private final boolean[] burnt;
    private final DefaultedList<ItemStack> items;

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(BBQDEntityTypes.GRILL_BLOCK_ENTITY, pos, state);

        this.items = DefaultedList.ofSize(2, ItemStack.EMPTY);
        this.barbecuingTime = new int[2];
        this.barbecuingTotalTime = new int[2];
        this.burnt = new boolean[2];
        this.flipped = new boolean[2];
    }

    public static void tick(World world, BlockPos pos, BlockState state, GrillBlockEntity grill) {
        if (grill.isHeated()){
            grill.barbecuing();
            grill.addParticles();
        }else {
            grill.fadeBarbecuing();
        }
    }

    public void setBarbecuingTime(int i, int barbecuingTime) {
        this.barbecuingTime[i] = 0;
        this.barbecuingTotalTime[i] = barbecuingTime;
        this.flipped[i] = false;
        this.setBurnt(i, false);
    }

    public Optional<BarbecuingRecipe> findMatchingRecipe(ItemStack itemStack) {
        return this.world != null && this.items.stream().anyMatch(ItemStack::isEmpty) ? this.world.getRecipeManager().getFirstMatch(BarbecuingRecipe.Type.INSTANCE, new SimpleInventory(itemStack), this.world) : Optional.empty();
    }

    public boolean flip(int i){
        if (i != size() && canFlip(i)){
            setFlipped(i, true);
            this.barbecuingTime[i] = 0;
            this.inventoryChanged();
            if (this.world != null) {
                this.world.playSound(null, (float)this.pos.getX() + 0.5F, (float)this.pos.getY() + 0.5F, (float)this.pos.getZ() + 0.5F, ModSounds.BLOCK_SKILLET_ADD_FOOD.get(), SoundCategory.BLOCKS, 0.8F, 1.0F);
            }
            return true;
        }
        return false;
    }

    public boolean canFlip(int i) {
        return this.isBarbecuing() && !this.getFlipped(i) && !this.isBurnt(i) && barbecuingTime[i] >= (barbecuingTotalTime[i] / 2);
    }

    public boolean getFlipped(int i) {
        return flipped[i];
    }

    public void setFlipped(int i, boolean flipped){
        this.flipped[i] = flipped;
    }

    private void barbecuing(){
        for (int i = 0; i < items.size(); ++i) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()){
                ++barbecuingTime[i];
                if (barbecuingTime[i] >= barbecuingTotalTime[i]){
                    if (world != null){
                        Inventory inventory = new SimpleInventory(stack);
                        ItemStack result = world.getRecipeManager().getAllMatches(BarbecuingRecipe.Type.INSTANCE, inventory, world).stream().map(recipe -> recipe.craft(inventory, world.getRegistryManager())).findAny().orElse(stack);
                        if (getFlipped(i) && !(barbecuingTime[i] >= (barbecuingTotalTime[i] * 2))){
                            this.setStack(i, result);
                            this.inventoryChanged();
                        }else {
                            this.setStack(i, BBQDItems.BURNT_FOOD.getDefaultStack());
                            this.setBurnt(i, true);
                            this.inventoryChanged();
                        }
                    }
                }
            }
        }
    }

    private void fadeBarbecuing() {
        for (int i = 0; i < items.size(); ++i) {
            if (barbecuingTime[i] > 0) {
                barbecuingTime[i] = MathHelper.clamp(barbecuingTime[i] - 2, 0, barbecuingTotalTime[i]);
            }
        }
    }

    public Vec2f getGrillItemOffset(int index) {
        final float xOffset = .2f;
        final float yOffset = .0f;
        final Vec2f[] offsets = {new Vec2f(xOffset, yOffset), new Vec2f(-xOffset, yOffset)};

        return offsets[index];
    }

    public boolean isHeated() {
        return world != null && this.isHeated(this.world, this.pos);
    }

    public boolean isBarbecuing(){
        return world != null && this.isHeated() && (!this.getStack(0).isEmpty() || !this.getStack(1).isEmpty());
    }


    public boolean isBurnt(int i) {
        return burnt[i];
    }

    public void setBurnt(int i, boolean burnt) {
        this.burnt[i] = burnt;
    }

    private void addParticles() {
        World world = getWorld();

        if (world != null) {
            BlockPos blockpos = getPos();
            Random random = world.random;
            if (random.nextFloat() < 0.2F && isBarbecuing()) {
                double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                double y = (double) pos.getY() + 1.0D;
                double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                double motionY = random.nextBoolean() ? 0.015D : 0.005D;
                world.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
            }

            for (int j = 0; j < items.size(); ++j) {
                if (!getItems().get(j).isEmpty() && random.nextFloat() < 0.2f) {
                    double d0 = blockpos.getX() + .5d;
                    double d1 = blockpos.getY() + 1.d;
                    double d2 = blockpos.getZ() + .5d;
                    Vec2f v1 = getGrillItemOffset(j);

                    Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
                    int directionIndex = direction.getHorizontal();
                    Vec2f offset = directionIndex % 2 == 0 ? v1 : new Vec2f(v1.y, v1.x);

                    double d5 = d0 - (direction.getOffsetX() * offset.x) + (direction.rotateYClockwise().getOffsetX() * offset.x);
                    double d7 = d2 - (direction.getOffsetZ() * offset.y) + (direction.rotateYClockwise().getOffsetZ() * offset.y);

                    for (int k = 0; k < (canFlip(j) ? 8 : 1); ++k) {
                        world.addParticle(ParticleTypes.SMOKE, d5, d1, d7, .0d, 5.e-4d, .0d);
                    }
                }
            }
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.items);
        this.writeBurnt(nbt);
        this.writeFlipped(nbt);
        nbt.putIntArray("BarbecuingTimes", this.barbecuingTime);
        nbt.putIntArray("BarbecuingTotalTimes", this.barbecuingTotalTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.items);

        if(nbt.contains("Burnt", NbtElement.BYTE_ARRAY_TYPE)){
            byte[] burnt = nbt.getByteArray("Burnt");
            for(int i = 0; i < Math.min(this.burnt.length, burnt.length); i++) {
                this.burnt[i] = burnt[i] == 1;
            }
        }

        if(nbt.contains("Flipped", NbtElement.BYTE_ARRAY_TYPE)){
            byte[] flipped = nbt.getByteArray("Flipped");
            for(int i = 0; i < Math.min(this.flipped.length, flipped.length); i++) {
                this.flipped[i] = flipped[i] == 1;
            }
        }

        if (nbt.contains("BarbecuingTimes", NbtElement.INT_ARRAY_TYPE)) {
            int[] grillingTimeRead = nbt.getIntArray("BarbecuingTimes");
            System.arraycopy(grillingTimeRead, 0, barbecuingTime, 0, Math.min(barbecuingTotalTime.length, grillingTimeRead.length));
        }
        if (nbt.contains("BarbecuingTotalTimes",NbtElement.INT_ARRAY_TYPE)) {
            int[] grillingTotalTimeRead = nbt.getIntArray("BarbecuingTotalTimes");
            System.arraycopy(grillingTotalTimeRead, 0, barbecuingTotalTime, 0, Math.min(barbecuingTotalTime.length, grillingTotalTimeRead.length));
        }
    }


    private void writeBurnt(NbtCompound nbtCompound) {
        byte[] burnt = new byte[this.burnt.length];
        for(int i = 0; i < this.burnt.length; i++){
            burnt[i] = (byte) (this.burnt[i] ? 1 : 0);
        }
        nbtCompound.putByteArray("Burnt", burnt);
    }

    private void writeFlipped(NbtCompound nbtCompound) {
        byte[] flipped = new byte[this.flipped.length];
        for(int i = 0; i < this.flipped.length; i++){
            flipped[i] = (byte) (this.flipped[i] ? 1 : 0);
        }
        nbtCompound.putByteArray("Flipped", flipped);
    }

    private void inventoryChanged() {
        this.markDirty();
        if (this.world != null) {
            this.world.updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
        }
    }
}
