package com.mao.barbequesdelight.integration.rei.barbecuing;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.integration.rei.BBQDREIPlugin;
import com.mao.barbequesdelight.registry.BBQDBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Arrow;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class GrillCategory implements DisplayCategory<GrillDisplay> {

    private static final Identifier GUI_TEXTURE = BarbequesDelight.asID("textures/gui/grill_rei.png");

    @Override
    public CategoryIdentifier<? extends GrillDisplay> getCategoryIdentifier() {
        return BBQDREIPlugin.GRILL_DISPLAY_CATEGORY;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("barbequesdelight.rei.barbecuing");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BBQDBlocks.GRILL);
    }

    @Override
    public List<Widget> setupDisplay(GrillDisplay display, Rectangle bounds) {
        Point origin = bounds.getLocation();
        final List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));
        Rectangle bgBounds = BBQDREIPlugin.centeredIntoRecipeBase(new Point(origin.x, origin.y), 110, 37);
        widgets.add(Widgets.createTexturedWidget(GUI_TEXTURE, new Rectangle(bgBounds.x, bgBounds.y, 110, 37), 4, 11));
        widgets.add(Widgets.createSlot(new Point(bgBounds.x + 12, bgBounds.y + 11)).entries(display.getInputEntries().get(0)).markInput().disableBackground());
        Arrow arrow = Widgets.createArrow(new Point(bgBounds.x + 43, bgBounds.y + 10)).animationDurationTicks(display.getBarbecuingTime());
        widgets.add(arrow);
        widgets.add(Widgets.createSlot(new Point(bgBounds.x + 81, bgBounds.y + 11)).entries(display.getOutputEntries().get(0)).markOutput().disableBackground());
        widgets.add(Widgets.createLabel(new Point(arrow.getBounds().x + arrow.getBounds().width / 2, arrow.getBounds().y - 8), Text.literal((display.getBarbecuingTime() / 20) + " s")).noShadow().centered().tooltip(Text.literal("Seconds")).color(Formatting.DARK_GRAY.getColorValue(), Formatting.GRAY.getColorValue()));

        return widgets;
    }
}
