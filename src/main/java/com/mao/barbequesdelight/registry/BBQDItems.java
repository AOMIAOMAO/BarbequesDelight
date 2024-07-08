package com.mao.barbequesdelight.registry;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.common.item.SeasoningItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class BBQDItems {

    public static final Item GRILL = register("grill", new BlockItem(BBQDBlocks.GRILL, new Item.Settings()));
    public static final Item INGREDIENTS_BASIN = register("ingredients_basin", new BlockItem(BBQDBlocks.INGREDIENTS_BASIN, new Item.Settings()));
    public static final Item CUMIN_POWDER = register("cumin_powder", new SeasoningItem(BBQDSeasoning.CUMIN_SEASONING));
    public static final Item PEPPER_POWDER = register("pepper_powder", new SeasoningItem(BBQDSeasoning.PEPPER_SEASONING));
    public static final Item CHILLI_POWDER = register("chilli_powder", new SeasoningItem(BBQDSeasoning.CHILLI_SEASONING));

    public static final Item COD_SKEWER = register("cod_skewer", rawSkewerItem());
    public static final Item SALMON_SKEWER = register("salmon_skewer", rawSkewerItem());
    public static final Item CHICKEN_SKEWER = register("chicken_skewer", rawSkewerItem());
    public static final Item BEEF_SKEWER = register("beef_skewer", rawSkewerItem());
    public static final Item LAMB_SKEWER = register("lamb_skewer", rawSkewerItem());
    public static final Item RABBIT_SKEWER = register("rabbit_skewer", rawSkewerItem());
    public static final Item PORK_SAUSAGE_SKEWER = register("pork_sausage_skewer", rawSkewerItem());
    public static final Item POTATO_SKEWER = register("potato_skewer", rawSkewerItem());

    public static final Item GRILLED_COD_SKEWER = register("grilled_cod_skewer", grilledSkewerItem(BBQDFoods.GRILLED_COD_SKEWER, false));
    public static final Item GRILLED_SALMON_SKEWER = register("grilled_salmon_skewer", grilledSkewerItem(BBQDFoods.GRILLED_SALMON_SKEWER, false));
    public static final Item GRILLED_CHICKEN_SKEWER = register("grilled_chicken_skewer", grilledSkewerItem(BBQDFoods.GRILLED_CHICKEN_SKEWER, false));
    public static final Item GRILLED_BEEF_SKEWER = register("grilled_beef_skewer", grilledSkewerItem(BBQDFoods.GRILLED_BEEF_SKEWER, false));
    public static final Item GRILLED_LAMB_SKEWER = register("grilled_lamb_skewer", grilledSkewerItem(BBQDFoods.GRILLED_LAMB_SKEWER, true));
    public static final Item GRILLED_RABBIT_SKEWER = register("grilled_rabbit_skewer", grilledSkewerItem(BBQDFoods.GRILLED_RABBIT_SKEWER, true));
    public static final Item GRILLED_PORK_SAUSAGE_SKEWER = register("grilled_pork_sausage_skewer", grilledSkewerItem(BBQDFoods.GRILLED_PORK_SAUSAGE_SKEWER, true));
    public static final Item GRILLED_POTATO_SKEWER = register("grilled_potato_skewer", grilledSkewerItem(BBQDFoods.GRILLED_POTATO_SKEWER, true));

    public static final Item BURNT_FOOD = register("burnt_food", new ConsumableItem(new Item.Settings().food(BBQDFoods.BURNT_FOOD), true));
    public static final Item KEBAB_WRAP = register("kebab_wrap", new ConsumableItem(new Item.Settings().food(BBQDFoods.SKEWER_WRAP), true));
    public static final Item KEBAB_SANDWICH = register("kebab_sandwich", new Item(new Item.Settings().food(BBQDFoods.SKEWER_SANDWICH)));

    private static Item register(String id, Item item){
        Item i =  Items.register(BarbequesDelight.asID(id), item);
        ItemGroupEvents.modifyEntriesEvent(BarbequesDelight.ITEM_GROUP).register(entries -> entries.add(i));
        return i;
    }

    private static Item rawSkewerItem(){
        return new Item(new Item.Settings().recipeRemainder(Items.STICK));
    }

    private static Item grilledSkewerItem(FoodComponent food, boolean hasTooltip){
        return new ConsumableItem(new Item.Settings().food(food).recipeRemainder(Items.STICK), hasTooltip);
    }

    public static void registerBBQDItems(){
        BarbequesDelight.LOGGER.debug("Register BBQD Items For" + BarbequesDelight.MODID);
    }
}
