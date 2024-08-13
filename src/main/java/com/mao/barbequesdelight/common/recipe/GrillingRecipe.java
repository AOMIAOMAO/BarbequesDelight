package com.mao.barbequesdelight.common.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class GrillingRecipe implements Recipe<Inventory> {

    protected final Ingredient ingredient;
    protected final ItemStack output;
    protected final Identifier id;
    protected final int grillingtime;

    public GrillingRecipe(Ingredient ingredient, ItemStack output, Identifier id, int grillingtime) {
        this.ingredient = ingredient;
        this.output = output;
        this.id = id;
        this.grillingtime = grillingtime;
    }

    public int getGrillingtime() {
        return grillingtime;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return ingredient.test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.ingredient);
        return list;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return GrillingRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<GrillingRecipe> {
        public static final Type INSTANCE = new Type();
    }
}
