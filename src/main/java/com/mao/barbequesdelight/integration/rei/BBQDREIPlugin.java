package com.mao.barbequesdelight.integration.rei;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.common.recipe.GrillingRecipe;
import com.mao.barbequesdelight.common.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.integration.rei.barbecuing.GrillCategory;
import com.mao.barbequesdelight.integration.rei.barbecuing.GrillDisplay;
import com.mao.barbequesdelight.integration.rei.skewering.SkeweringCategory;
import com.mao.barbequesdelight.integration.rei.skewering.SkeweringDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public class BBQDREIPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<GrillDisplay> GRILL_DISPLAY_CATEGORY = CategoryIdentifier.of(BarbequesDelight.asID("grill"));
    public static final CategoryIdentifier<SkeweringDisplay> SKEWERING_DISPLAY_CATEGORY = CategoryIdentifier.of(BarbequesDelight.asID("skewering"));

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(GrillingRecipe.class, GrillingRecipe.Type.INSTANCE, GrillDisplay::new);
        registry.registerRecipeFiller(SkeweringRecipe.class, SkeweringRecipe.Type.INSTANCE, SkeweringDisplay::new);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new GrillCategory());
        registry.add(new SkeweringCategory());
    }

    public static Rectangle centeredIntoRecipeBase(Point origin, int width, int height) {
        return centeredInto(new Rectangle(origin.x, origin.y, 150, 66), width, height);
    }

    public static Rectangle centeredInto(Rectangle origin, int width, int height) {
        return new Rectangle(origin.x + (origin.width - width) / 2, origin.y + (origin.height - height) / 2, width, height);
    }
}
