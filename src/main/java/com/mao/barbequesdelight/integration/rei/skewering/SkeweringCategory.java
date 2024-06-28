package com.mao.barbequesdelight.integration.rei.skewering;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.integration.rei.BBQDREIPlugin;
import com.mao.barbequesdelight.registry.BBQDBlocks;
import com.mao.barbequesdelight.registry.BBQDItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
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

public class SkeweringCategory implements DisplayCategory<SkeweringDisplay> {

    private static final Identifier GUI_TEXTURE = BarbequesDelight.asID("textures/gui/skewering_rei.png");

    @Override
    public CategoryIdentifier<? extends SkeweringDisplay> getCategoryIdentifier() {
        return BBQDREIPlugin.SKEWERING_DISPLAY_CATEGORY;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("barbequesdelight.rei.skewering");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BBQDItems.INGREDIENTS_BASIN);
    }

    @Override
    public List<Widget> setupDisplay(SkeweringDisplay display, Rectangle bounds) {
        Point origin = bounds.getLocation();
        final List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createRecipeBase(bounds));
        Rectangle bgBounds = BBQDREIPlugin.centeredIntoRecipeBase(new Point(origin.x, origin.y), 105, 50);
        Slot slot = Widgets.createSlot(new Point(bgBounds.x + 7, bgBounds.y + 7)).entries(display.getInputEntries().get(0)).markInput().disableBackground();
        widgets.add(slot);
        widgets.add(Widgets.createTexturedWidget(GUI_TEXTURE, new Rectangle(bgBounds.x, bgBounds.y, 110, 50), 6, 6));widgets.add(Widgets.createSlot(new Point(bgBounds.x + 7, bgBounds.y + 26)).entries(display.getSideDishes()).markInput().disableBackground());
        widgets.add(Widgets.createSlot(new Point(bgBounds.x + 27, bgBounds.y + 16)).entries(display.getTool()).markInput().disableBackground());
        widgets.add(Widgets.createSlot(new Point(bgBounds.x + 77, bgBounds.y + 16)).entries(display.getOutputEntries().get(0)).markOutput().disableBackground());
        widgets.add(Widgets.createLabel(new Point(slot.getBounds().x + slot.getBounds().width / 2, slot.getBounds().y - 8), Text.literal("count:" + display.getCount())).noShadow().centered().color(Formatting.DARK_GRAY.getColorValue(), Formatting.GRAY.getColorValue()));

        return widgets;
    }
}
