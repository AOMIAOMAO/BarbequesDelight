package com.mao.barbequesdelight.registry;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.common.recipe.BarbecuingRecipe;
import com.mao.barbequesdelight.common.recipe.BarbecuingRecipeSerializer;
import com.mao.barbequesdelight.common.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.common.recipe.SkeweringRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BBQDRecipes {

    private static void recipe(RecipeSerializer<?> serializer, RecipeType<?> type, String id) {
        Registry.register(Registries.RECIPE_TYPE, BarbequesDelight.asID(id), type);
        Registry.register(Registries.RECIPE_SERIALIZER, BarbequesDelight.asID(id), serializer);
    }

    public static void registerBBQDRecipes() {
        recipe(BarbecuingRecipeSerializer.INSTANCE, BarbecuingRecipe.Type.INSTANCE, "barbecuing");
        recipe(SkeweringRecipeSerializer.INSTANCE, SkeweringRecipe.Type.INSTANCE, "skewering");
    }
}
