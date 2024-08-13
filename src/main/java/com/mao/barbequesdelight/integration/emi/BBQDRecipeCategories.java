package com.mao.barbequesdelight.integration.emi;

import com.mao.barbequesdelight.BarbequesDelight;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import net.minecraft.util.Identifier;

public class BBQDRecipeCategories {
    private static final Identifier SIMPLIFIED_TEXTURES = BarbequesDelight.asID("textures/gui/emi/simplified.png");

    public static final EmiRecipeCategory GRILLING = new EmiRecipeCategory(BarbequesDelight.asID("grilling"), BBQDRecipeWorkstations.GRILLING, simplifiedRenderer(0, 0));
    public static final EmiRecipeCategory SKEWERING = new EmiRecipeCategory(BarbequesDelight.asID("skewering"), BBQDRecipeWorkstations.SKEWERING, simplifiedRenderer(16, 0));

    private static EmiRenderable simplifiedRenderer(int u, int v) {
        return (draw, x, y, delta) -> draw.drawTexture(SIMPLIFIED_TEXTURES, x, y, u, v, 16, 16, 32, 16);
    }
}
