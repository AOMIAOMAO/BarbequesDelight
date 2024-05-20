package com.mao.barbequesdelight.common.block.blockentity;

import com.mao.barbequesdelight.common.recipe.BarbecuingRecipe;
import com.mao.barbequesdelight.registry.BBQDEntityTypes;
import com.mao.barbequesdelight.registry.BBQDItems;
import net.minecraft.block.Block;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;

import java.util.List;
import java.util.Optional;

public class GrillBlockEntity extends BlockEntity implements BlockEntityInv, HeatableBlockEntity {

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final int[] barbecuingTimes;
    private final int[] barbecuingTimesTotal;
    private final boolean[] flipped;
    private final boolean[] burnt;

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(BBQDEntityTypes.GRILL_BLOCK_ENTITY, pos, state);
        this.barbecuingTimes = new int[2];
        this.barbecuingTimesTotal = new int[2];
        this.burnt = new boolean[2];
        this.flipped = new boolean[2];
    }

    public void setBarbecuing(int i, int time){
        this.barbecuingTimes[i] = 0;
        this.barbecuingTimesTotal[i] = time;
        this.flipped[i] = false;
        this.setBurnt(i, false);
    }

    protected void barbecuing(){
        boolean flag = false;
        for (int i = 0; i < items.size(); ++i) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()){
                ++barbecuingTimes[i];
                if (barbecuingTimes[i] == barbecuingTimesTotal[i]){
                    if (world != null){
                        Inventory inventory = new SimpleInventory(stack);
                        ItemStack result = world.getRecipeManager().getAllMatches(BarbecuingRecipe.Type.INSTANCE, inventory, world).stream().map(recipe -> recipe.craft(inventory, world.getRegistryManager())).findAny().orElse(stack);
                        if (getFlipped(i)){
                            this.setStack(i, result);
                        }else {
                            this.setStack(i, BBQDItems.BURNT_FOOD.getDefaultStack());
                            setBurnt(i, true);
                        }

                        flag = true;
                    }
                } else if (barbecuingTimes[i] >=( barbecuingTimesTotal[i] * 2) && !getBurnt(i)) {
                    this.setStack(i, BBQDItems.BURNT_FOOD.getDefaultStack());
                    setBurnt(i, true);
                    flag = true;
                }
            }
            if (flag){
                inventoryChanged();
            }
        }
    }

    private void fadeBarbecuing() {
        boolean flag = false;
        for (int i = 0; i < items.size(); ++i) {
            if (barbecuingTimes[i] > 0) {
                flag = true;
                barbecuingTimes[i] = MathHelper.clamp(barbecuingTimes[i] - 2, 0, barbecuingTimesTotal[i]);
            }
        }
        if (flag){
            markDirty();
        }
    }

    public boolean flip(int i){
        if (canFlip(i)){
            setFlipped(i, true);
            this.barbecuingTimes[i] = 0;
            sendUpdatePacket(this);
            return true;
        }
        return false;
    }

    public boolean canFlip(int i){
        return isBarbecuing() && !getFlipped(i) && !getBurnt(i) && barbecuingTimes[i] >= (barbecuingTimesTotal[i] /2);
    }

    public void setFlipped(int i, boolean flipped){
        this.flipped[i] = flipped;
    }

    public boolean getFlipped(int i) {
        return flipped[i];
    }

    public boolean getBurnt(int i) {
        return burnt[i];
    }

    public void setBurnt(int i, boolean burnt){
        this.burnt[i] = burnt;
    }

    public static void tick(World world, BlockPos blockPos, BlockState blockState, GrillBlockEntity grill) {
        if (grill.isHeated()){
            grill.barbecuing();
        }else {
            grill.fadeBarbecuing();
        }
    }

    public static void animationTick(World world, BlockPos pos, BlockState blockState, GrillBlockEntity grill){
        if (grill.isBarbecuing()){
            grill.addParticles();
            Random random = world.random;
            if (random.nextFloat() < 0.2F) {
                double x = (double) pos.getX() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                double y = (double) pos.getY() + 1.1D;
                double z = (double) pos.getZ() + 0.5D + (random.nextDouble() * 0.4D - 0.2D);
                double motionY = random.nextBoolean() ? 0.015D : 0.005D;
                world.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0D, motionY, 0.0D);
            }
        }
    }

    public boolean isHeated() {
        return world != null && this.isHeated(this.world, this.pos);
    }

    public boolean isBarbecuing(){
        return world != null && isHeated() && !getStack(getStack(0).isEmpty() ? 1 : 0).isEmpty();
    }

    public Optional<BarbecuingRecipe> findMatchingRecipe(ItemStack itemStack) {
        return this.world != null && this.items.stream().anyMatch(ItemStack::isEmpty) ? this.world.getRecipeManager().getFirstMatch(BarbecuingRecipe.Type.INSTANCE, new SimpleInventory(itemStack), this.world) : Optional.empty();
    }

    public Vec2f getGrillItemOffset(int index) {
        final float xOffset = .2f;
        final float yOffset = .0f;
        final Vec2f[] offsets = {new Vec2f(xOffset, yOffset), new Vec2f(-xOffset, yOffset)};

        return offsets[index];
    }

    public void inventoryChanged() {
        this.markDirty();
        if (world != null) {
            world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    private void addParticles() {
        if (world == null) return;

        for (int i = 0; i < items.size(); ++i) {
            if (!items.get(i).isEmpty() && world.random.nextFloat() < 0.2F) {
                Vec2f grillItemOffset = getGrillItemOffset(i);
                Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
                int directionIndex = direction.getHorizontal();
                Vec2f offset = directionIndex % 2 == 0 ? grillItemOffset : new Vec2f(grillItemOffset.y, grillItemOffset.x);

                double x = ((double) pos.getX() + 0.5D) - (direction.getOffsetX() * offset.x) + (direction.rotateYClockwise().getOffsetX() * offset.x);
                double y = (double) pos.getY() + 1.0D;
                double z = ((double) pos.getZ() + 0.5D) - (direction.getOffsetZ() * offset.y) + (direction.rotateYClockwise().getOffsetZ() * offset.y);
                for (int k = 0; k < 3; ++k) {
                    world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
                }
            }
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    //NBT And Server

    public static void sendUpdatePacket(BlockEntity entity) {
        Packet<ClientPlayPacketListener> packet = entity.toUpdatePacket();
        if(packet != null) {
            sendUpdatePacket(entity.getWorld(), entity.getPos(), packet);
        }
    }

    private static void sendUpdatePacket(World world, BlockPos pos, Packet<ClientPlayPacketListener> packet) {
        if(world instanceof ServerWorld server) {
            List<ServerPlayerEntity> players = server.getChunkManager().threadedAnvilChunkStorage.getPlayersWatchingChunk(new ChunkPos(pos), false);
            players.forEach(player -> player.networkHandler.sendPacket(packet));
        }
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        this.writeBurnt(nbtCompound);
        this.writeFlipped(nbtCompound);
        Inventories.writeNbt(nbtCompound, this.items, true);
        return nbtCompound;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, items, true);
        this.writeBurnt(nbt);
        this.writeFlipped(nbt);
        nbt.putIntArray("barbecuingTimes", this.barbecuingTimes);
        nbt.putIntArray("barbecuingTimesTotal", this.barbecuingTimesTotal);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.items.clear();
        Inventories.readNbt(nbt, items);
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

        if (nbt.contains("barbecuingTimes", NbtElement.INT_ARRAY_TYPE)) {
            int[] arrayCookingTimes = nbt.getIntArray("barbecuingTimes");
            System.arraycopy(arrayCookingTimes, 0, barbecuingTimes, 0, Math.min(barbecuingTimesTotal.length, arrayCookingTimes.length));
        }
        if (nbt.contains("barbecuingTimesTotal", NbtElement.INT_ARRAY_TYPE)) {
            int[] arrayCookingTimesTotal = nbt.getIntArray("barbecuingTimesTotal");
            System.arraycopy(arrayCookingTimesTotal, 0, barbecuingTimesTotal, 0, Math.min(barbecuingTimesTotal.length, arrayCookingTimesTotal.length));
        }

    }

    private void writeFlipped(NbtCompound compound) {
        byte[] flipped = new byte[this.flipped.length];
        for(int i = 0; i < this.flipped.length; i++) {
            flipped[i] = (byte) (this.flipped[i] ? 1 : 0);
        }
        compound.putByteArray("Flipped", flipped);
    }

    private void writeBurnt(NbtCompound compound) {
        byte[] burnt = new byte[this.burnt.length];
        for(int i = 0; i < this.burnt.length; i++) {
            burnt[i] = (byte) (this.burnt[i] ? 1 : 0);
        }
        compound.putByteArray("Burnt", burnt);
    }
}
