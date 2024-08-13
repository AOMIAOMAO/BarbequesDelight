package com.mao.barbequesdelight.integration.rei.barbecuing;

import com.mao.barbequesdelight.common.recipe.GrillingRecipe;
import com.mao.barbequesdelight.integration.rei.BBQDREIPlugin;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.Collections;

public class GrillDisplay extends BasicDisplay {

    private final int grillingTime;

    public GrillDisplay(GrillingRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput(null))));
        this.grillingTime = recipe.getGrillingtime();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BBQDREIPlugin.GRILL_DISPLAY_CATEGORY;
    }

    public int getGrillingTime() {
        return grillingTime;
    }
}
