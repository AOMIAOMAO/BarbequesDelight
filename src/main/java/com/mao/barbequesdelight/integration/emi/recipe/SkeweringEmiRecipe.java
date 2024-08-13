package com.mao.barbequesdelight.integration.emi.recipe;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.integration.emi.BBQDRecipeCategories;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkeweringEmiRecipe implements EmiRecipe {
    private static final Identifier BG = new Identifier(BarbequesDelight.MODID, "textures/gui/skewering_rei.png");
    private static final Identifier NO_SIDEDISHES_BG = new Identifier(BarbequesDelight.MODID, "textures/gui/skewering_without_sidedishes_rei.png");

    private final Identifier id;
    private final int count;
    private final EmiStack stick;
    private final List<EmiIngredient> inputs;
    private final EmiStack output;

    public SkeweringEmiRecipe(Identifier id, int count, EmiStack stick, List<EmiIngredient> inputs, EmiStack output) {
        this.id = id;
        this.count = count;
        this.stick = stick;
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return BBQDRecipeCategories.SKEWERING;
    }

    @Override
    public @Nullable Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return inputs;
    }

    public int getCount() {
        return count;
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(stick);
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
        return 50;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        if (getInputs().size() > 1){
            widgets.addTexture(BG, 0, 0, 105, 50, 6, 6);
            widgets.addSlot(getInputs().get(1), 6, 25).drawBack(false);widgets.addSlot(getInputs().get(0), 6, 6).drawBack(false);
        }else {
            widgets.addTexture(NO_SIDEDISHES_BG, 0, 0, 105, 50, 6, 6);
            widgets.addSlot(getInputs().get(0), 5, 15).drawBack(false);
        }
        widgets.addSlot(stick, 26, 15).drawBack(false);
        widgets.addSlot(output, 76, 15).drawBack(false).recipeContext(this);
    }
}
