package com.mao.barbequesdelight.integration.emi.recipe;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.integration.emi.BBQDRecipeCategories;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.AnimatedTextureWidget;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrillingEmiRecipe implements EmiRecipe {
    private static final Identifier BACKGROUND = new Identifier(BarbequesDelight.MODID, "textures/gui/grill_rei.png");

    private final Identifier id;
    private final int grillingTime;
    private final EmiIngredient input;
    private final EmiStack output;

    public GrillingEmiRecipe(Identifier id, int grillingTime, EmiIngredient input, EmiStack output) {
        this.id = id;
        this.grillingTime = grillingTime;
        this.input = input;
        this.output = output;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return BBQDRecipeCategories.GRILLING;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    public int getGrillingTime() {
        return grillingTime;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 110;
    }

    @Override
    public int getDisplayHeight() {
        return 37;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(BACKGROUND, 0, 0, 110, 37, 4, 11);
        AnimatedTextureWidget widget = widgets.addAnimatedTexture(BACKGROUND, 44, 10, 22, 19, 0, 58, 1000 * 10, true, false, false);
        widgets.addText(Text.translatable("barbequesdelight.emi.grilling", getGrillingTime() /20), widget.getBounds().x(), widget.getBounds().y() - 8, Formatting.WHITE.getColorValue(), true);
        addSlot(widgets, input, 11, 10);
        addSlot(widgets, output, 80, 10).recipeContext(this);
    }

    private SlotWidget addSlot(WidgetHolder widgets, EmiIngredient ingredient, int x, int y) {
        return widgets.addSlot(ingredient, x, y).drawBack(false);
    }

}
