package com.mao.barbequesdelight.integration.emi;

import com.mao.barbequesdelight.common.recipe.GrillingRecipe;
import com.mao.barbequesdelight.common.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.integration.emi.recipe.GrillingEmiRecipe;
import com.mao.barbequesdelight.integration.emi.recipe.SkeweringEmiRecipe;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.RecipeManager;

public class BBQDEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(BBQDRecipeCategories.GRILLING);
        registry.addCategory(BBQDRecipeCategories.SKEWERING);

        registry.addWorkstation(BBQDRecipeCategories.GRILLING, BBQDRecipeWorkstations.GRILLING);
        registry.addWorkstation(BBQDRecipeCategories.SKEWERING, BBQDRecipeWorkstations.SKEWERING);

        RecipeManager manager = registry.getRecipeManager();
        for (GrillingRecipe recipe : manager.listAllOfType(GrillingRecipe.Type.INSTANCE)) {
            registry.addRecipe(new GrillingEmiRecipe(recipe.getId(), recipe.getGrillingtime(), EmiIngredient.of(recipe.getIngredients().get(0)), EmiStack.of(recipe.getOutput(null))));
        }
        for (SkeweringRecipe recipe : manager.listAllOfType(SkeweringRecipe.Type.INSTANCE)) {
            registry.addRecipe(new SkeweringEmiRecipe(recipe.getId(), recipe.getIngredientCount(), EmiStack.of(recipe.getTool()), recipe.getIngredients().stream().map(ingredient -> EmiIngredient.of(ingredient, recipe.getIngredientCount())).toList(), EmiStack.of(recipe.getOutput(MinecraftClient.getInstance().world.getRegistryManager()))));
        }
    }
}
