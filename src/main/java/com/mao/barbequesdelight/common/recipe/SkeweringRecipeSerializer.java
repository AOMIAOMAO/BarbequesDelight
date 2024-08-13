package com.mao.barbequesdelight.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.fabricators_of_create.porting_lib.util.CraftingHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

public class SkeweringRecipeSerializer implements RecipeSerializer<SkeweringRecipe> {

    public static final SkeweringRecipeSerializer INSTANCE = new SkeweringRecipeSerializer();

    @Override
    public SkeweringRecipe read(Identifier id, JsonObject json) {
        final DefaultedList<Ingredient> inputItemsIn = readIngredients(JsonHelper.getArray(json, "ingredients"));
        if (inputItemsIn.isEmpty()) {
            throw new JsonParseException("No ingredients for skewering recipe");
        } else if (inputItemsIn.size() > 2) {
            throw new JsonParseException("Too many ingredients for skewering recipe! The max is " + 2);
        }else {
            final ItemStack output = CraftingHelper.getItemStack(JsonHelper.getObject(json, "result"), true);
            ItemStack tool = JsonHelper.hasElement(json, "container") ? CraftingHelper.getItemStack(JsonHelper.getObject(json, "container"), true) : ItemStack.EMPTY;
            final int ingredientCount = JsonHelper.getInt(json, "count", 2);
            return new SkeweringRecipe(id, inputItemsIn, tool, output, ingredientCount);
        }
    }

    @Override
    public SkeweringRecipe read(Identifier id, PacketByteBuf buf) {
        int i = buf.readVarInt();
        DefaultedList<Ingredient> inputItemsIn = DefaultedList.ofSize(i, Ingredient.EMPTY);

        for (int j = 0; j < inputItemsIn.size(); ++j) {
            inputItemsIn.set(j, Ingredient.fromPacket(buf));
        }
        ItemStack output = buf.readItemStack();
        ItemStack tool = buf.readItemStack();
        int count = buf.readInt();

        return new SkeweringRecipe(id, inputItemsIn, tool, output, count);
    }

    @Override
    public void write(PacketByteBuf buf, SkeweringRecipe recipe) {
        buf.writeVarInt(recipe.getIngredients().size());
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.write(buf);
        }
        buf.writeItemStack(recipe.getOutput(null));
        buf.writeItemStack(recipe.getTool());
        buf.writeInt(recipe.getIngredientCount());
    }

    private static DefaultedList<Ingredient> readIngredients(JsonArray ingredientArray) {
        DefaultedList<Ingredient> ingredients = DefaultedList.of();

        for (int i = 0; i < ingredientArray.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }
}
