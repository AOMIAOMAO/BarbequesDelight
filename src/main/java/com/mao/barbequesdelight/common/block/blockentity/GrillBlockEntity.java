package com.mao.barbequesdelight.common.block.blockentity;

import com.mao.barbequesdelight.common.recipe.GrillingRecipe;
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
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.RecipeType;
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
    protected final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    public final int[] grillingTimes;
    protected final int[] grillingTimesTotal;
    public final boolean[] flipped;

    private static final String TAG_KEY_COOKING_TOTAL_TIMES = "CookingTimes";
    private static final String TAG_KEY_COOKING_TIMES = "CookingTotalTimes";

    public GrillBlockEntity(BlockPos pos, BlockState state) {
        super(BBQDEntityTypes.GRILL, pos, state);
        this.grillingTimes = new int[2];
        this.grillingTimesTotal = new int[2];
        this.flipped = new boolean[2];
    }

    public void setBarbecuing(int i, int time){
        this.grillingTimes[i] = 0;
        this.grillingTimesTotal[i] = time;
        this.setFlipped(i, false);
        inventoryChanged();
    }

    protected void barbecuing(){
        boolean flag = false;
        for (int i = 0; i < items.size(); ++i) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()){
                ++grillingTimes[i];
                if (grillingTimes[i] == grillingTimesTotal[i]){
                    if (world != null){
                        Inventory inventory = new SimpleInventory(stack);

                        ItemStack campfire = world.getRecipeManager().getAllMatches(RecipeType.CAMPFIRE_COOKING, inventory, world).stream().map(recipe -> recipe.craft(inventory, world.getRegistryManager())).findAny().orElse(stack);
                        ItemStack result = world.getRecipeManager().getAllMatches(GrillingRecipe.Type.INSTANCE, inventory, world).stream().map(recipe -> recipe.craft(inventory, world.getRegistryManager())).findAny().orElse(campfire);

                        this.setStack(i, getFlipped(i) ? result : BBQDItems.BURNT_FOOD.getDefaultStack());
                        flag = true;
                    }
                } else if (grillingTimes[i] == ( grillingTimesTotal[i] * 2)) {
                    this.setStack(i, BBQDItems.BURNT_FOOD.getDefaultStack());
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
            if (grillingTimes[i] > 0) {
                flag = true;
                grillingTimes[i] = MathHelper.clamp(grillingTimes[i] - 2, 0, grillingTimesTotal[i]);
            }
        }
        if (flag){
            markDirty();
        }
    }

    public boolean flip(int i){
        if (canFlip(i)){
            setFlipped(i, true);
            this.grillingTimes[i] = (this.grillingTimesTotal[i] / 2);
            return true;
        }
        return false;
    }

    public void setFlipped(int i, boolean flipped){
        this.flipped[i] = flipped;
        inventoryChanged();
        writeFlipped(new NbtCompound());
        sendUpdatePacket(this);
    }

    public boolean getFlipped(int i) {
        return flipped[i];
    }

    public boolean canFlip(int i){
        return isBarbecuing() && grillingTimes[i] >= (grillingTimesTotal[i] / 2) && !getFlipped(i) && !world.isClient();
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

    public Optional<GrillingRecipe> findMatchingRecipe(ItemStack itemStack) {
        return this.world != null && this.items.stream().anyMatch(ItemStack::isEmpty) ? this.world.getRecipeManager().getFirstMatch(GrillingRecipe.Type.INSTANCE, new SimpleInventory(itemStack), this.world) : Optional.empty();
    }

    public Optional<CampfireCookingRecipe> findMatchingCampfireRecipe(ItemStack itemStack) {
        return this.world != null && this.items.stream().anyMatch(ItemStack::isEmpty) ? this.world.getRecipeManager().getFirstMatch(RecipeType.CAMPFIRE_COOKING, new SimpleInventory(itemStack), this.world) : Optional.empty();
    }

    public Vec2f getGrillItemOffset(int index) {
        final float xOffset = .2f;
        final float yOffset = .0f;
        final Vec2f[] offsets = {new Vec2f(xOffset, yOffset), new Vec2f(-xOffset, yOffset)};

        return offsets[index];
    }

    public void inventoryChanged() {
        markDirty();
        if (world != null) {
            world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_ALL);
        }
    }

    private void addParticles() {
        if (world == null) return;

        for (int i = 0; i < items.size(); ++i) {
            grillingTimes[i]++;
            if (!items.get(i).isEmpty()) {
                Vec2f grillItemOffset = getGrillItemOffset(i);
                Direction direction = getCachedState().get(HorizontalFacingBlock.FACING);
                int directionIndex = direction.getHorizontal();
                Vec2f offset = directionIndex % 2 == 0 ? grillItemOffset : new Vec2f(grillItemOffset.y, grillItemOffset.x);

                double x = ((double) pos.getX() + 0.5D) - (direction.getOffsetX() * offset.x) + (direction.rotateYClockwise().getOffsetX() * offset.x);
                double y = (double) pos.getY() + 1.0D;
                double z = ((double) pos.getZ() + 0.5D) - (direction.getOffsetZ() * offset.y) + (direction.rotateYClockwise().getOffsetZ() * offset.y);

                if (world.random.nextFloat() < 0.2f) {
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

    public static void sendUpdatePacket(BlockEntity blockEntity) {
        Packet<ClientPlayPacketListener> packet = blockEntity.toUpdatePacket();
        if (packet != null) {
            sendUpdatePacket(blockEntity.getWorld(), blockEntity.getPos(), packet);
        }
    }

    private static void sendUpdatePacket(World level, BlockPos pos, Packet<ClientPlayPacketListener> packet) {
        if (level instanceof ServerWorld server) {
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
        Inventories.writeNbt(nbtCompound, this.items, true);
        this.writeFlipped(nbtCompound);
        return nbtCompound;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, items, true);
        this.writeFlipped(nbt);
        nbt.putIntArray(TAG_KEY_COOKING_TIMES, grillingTimes);
        nbt.putIntArray(TAG_KEY_COOKING_TOTAL_TIMES, grillingTimesTotal);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.items.clear();
        Inventories.readNbt(nbt, items);

        if (nbt.contains(TAG_KEY_COOKING_TIMES, 11)) {
            int[] cookingTimeRead = nbt.getIntArray(TAG_KEY_COOKING_TIMES);
            System.arraycopy(cookingTimeRead, 0, grillingTimes, 0, Math.min(grillingTimesTotal.length, cookingTimeRead.length));
        }
        if (nbt.contains(TAG_KEY_COOKING_TOTAL_TIMES, 11)) {
            int[] cookingTotalTimeRead = nbt.getIntArray(TAG_KEY_COOKING_TOTAL_TIMES);
            System.arraycopy(cookingTotalTimeRead, 0, grillingTimesTotal, 0, Math.min(grillingTimesTotal.length, cookingTotalTimeRead.length));
        }

        if (nbt.contains("Flipped", NbtElement.BYTE_ARRAY_TYPE)) {
            byte[] flipped = nbt.getByteArray("Flipped");
            for (int i = 0; i < Math.min(this.flipped.length, flipped.length); i++) {
                this.flipped[i] = flipped[i] == 1;
            }
        }
    }

    private void writeFlipped(NbtCompound compound) {
        byte[] flipped = new byte[this.flipped.length];
        for(int i = 0; i < this.flipped.length; i++) {
            flipped[i] = (byte) (this.flipped[i] ? 1 : 0);
        }
        compound.putByteArray("Flipped", flipped);
    }
}
