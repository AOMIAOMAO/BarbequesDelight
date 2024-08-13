package com.mao.barbequesdelight.common.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class SkeweringRecipe implements Recipe<Inventory> {

    private final Identifier id;
    private final DefaultedList<Ingredient> ingredients;
    private final ItemStack tool;
    private final ItemStack result;
    private final int ingredientCount;

    public SkeweringRecipe(Identifier id, DefaultedList<Ingredient> ingredients, ItemStack tool, ItemStack result, int ingredientCount) {
        this.id = id;
        this.ingredients = ingredients;

        if (!tool.isEmpty()) {
            this.tool = tool;
        } else if (result.getItem().getRecipeRemainder() != null) {
            this.tool = new ItemStack(result.getItem().getRecipeRemainder());
        } else {
            this.tool = ItemStack.EMPTY;
        }

        this.result = result;
        this.ingredientCount = ingredientCount;
    }

    public ItemStack getTool() {
        return tool;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        RecipeMatcher stackedContents = new RecipeMatcher();
        int i = 0;

        for (int j = 0; j < 2; ++j) {
            ItemStack itemstack = inventory.getStack(j);
            if (!itemstack.isEmpty()) {
                ++i;
                stackedContents.addInput(itemstack, 1);
            }
        }
        return i == this.ingredients.size() && stackedContents.match(this, null) && ItemStack.areItemsEqual(tool, inventory.getStack(2));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        inventory.getStack(2).decrement(1);

        for (int j = 0; j < inventory.size()-1; ++j) {
            ItemStack itemstack = inventory.getStack(j);
            if (!itemstack.isEmpty()) {
                itemstack.decrement(getIngredientCount());
            }
        }

        return this.getOutput(registryManager).copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return result;
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SkeweringRecipeSerializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<SkeweringRecipe> {
        public static final SkeweringRecipe.Type INSTANCE = new SkeweringRecipe.Type();
    }
}
