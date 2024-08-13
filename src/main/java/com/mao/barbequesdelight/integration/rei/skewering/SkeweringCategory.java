package com.mao.barbequesdelight.integration.rei.skewering;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.integration.rei.BBQDREIPlugin;
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
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SkeweringCategory implements DisplayCategory<SkeweringDisplay> {

    private static final Identifier BG = BarbequesDelight.asID("textures/gui/skewering_rei.png");
    private static final Identifier NO_SIDEDISHES_BG = BarbequesDelight.asID("textures/gui/skewering_without_sidedishes_rei.png");

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
        widgets.add(Widgets.createSlot(new Point(bgBounds.x + 27, bgBounds.y + 16)).entries(display.getTool()).markInput().disableBackground());
        widgets.add(Widgets.createSlot(new Point(bgBounds.x + 77, bgBounds.y + 16)).entries(display.getOutputEntries().get(0)).markOutput().disableBackground());

        if (display.getInputEntries().size() >1){
            widgets.add(Widgets.createTexturedWidget(BG, new Rectangle(bgBounds.x, bgBounds.y, 110, 50), 6, 6));
            Slot slot = Widgets.createSlot(new Point(bgBounds.x + 7, bgBounds.y + 7)).entries(display.getInputEntries().get(0)).markInput().disableBackground();
            widgets.add(slot);
            Slot slot1 = Widgets.createSlot(new Point(bgBounds.x + 7, bgBounds.y + 26)).entries(display.getInputEntries().get(1)).markInput().disableBackground();
            widgets.add(slot1);
            widgets.add(Widgets.withTooltip(slot, Text.translatable("barbequesdelight.rei.count", display.getCount())));
            widgets.add(Widgets.withTooltip(slot1, Text.translatable("barbequesdelight.rei.count", display.getCount())));

        }else {
            widgets.add(Widgets.createTexturedWidget(NO_SIDEDISHES_BG, new Rectangle(bgBounds.x, bgBounds.y, 110, 50), 6, 6));
            Slot slot = Widgets.createSlot(new Point(bgBounds.x + 6, bgBounds.y + 16)).entries(display.getInputEntries().get(0)).markInput().disableBackground();
            widgets.add(slot);
            widgets.add(Widgets.withTooltip(slot, Text.translatable("barbequesdelight.rei.count", display.getCount())));
        }
        return widgets;
    }
}
