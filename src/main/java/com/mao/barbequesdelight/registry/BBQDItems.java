package com.mao.barbequesdelight.registry;

import com.mao.barbequesdelight.BarbequesDelight;
import com.mao.barbequesdelight.common.item.SeasoningItem;
import com.mao.barbequesdelight.common.item.SkewerItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class BBQDItems {

    public static final Item GRILL = register("grill", new BlockItem(BBQDBlocks.GRILL, new Item.Settings()));
    public static final Item INGREDIENTS_BASIN = register("ingredients_basin", new BlockItem(BBQDBlocks.INGREDIENTS_BASIN, new Item.Settings()));
    public static final Item CUMIN_POWDER = register("cumin_powder", new SeasoningItem("cumin_powder"));
    public static final Item PEPPER_POWDER = register("pepper_powder", new SeasoningItem("pepper_powder"));
    public static final Item CHILLI_POWDER = register("chilli_powder", new SeasoningItem("chilli_powder"));
    public static final Item KEBAB_WRAP = register("kebab_wrap", new ConsumableItem(new Item.Settings().food(BBQDFoods.SKEWER_WRAP), true));
    public static final Item KEBAB_SANDWICH = register("kebab_sandwich", new Item(new Item.Settings().food(BBQDFoods.SKEWER_SANDWICH)));
    public static final Item BURNT_FOOD = register("burnt_food", new ConsumableItem(new Item.Settings().food(new FoodComponent.Builder().alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.POISON, 200, 1), 1.0f).statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1), 1.0f).hunger(4).build()), true));
    public static final Item COD_SKEWER = register("cod_skewer", new Item(new Item.Settings().recipeRemainder(Items.STICK)));
    public static final Item COOKED_COD_SKEWER = register("cooked_cod_skewer", new SkewerItem(BBQDFoods.COOKED_COD_SKEWER, false));
    public static final Item SALMON_SKEWER = register("salmon_skewer", new Item(new Item.Settings().recipeRemainder(Items.STICK)));
    public static final Item COOKED_SALMON_SKEWER = register("cooked_salmon_skewer", new SkewerItem(BBQDFoods.COOKED_SALMON_SKEWER, false));
    public static final Item CHICKEN_SKEWER = register("chicken_skewer", new Item(new Item.Settings().recipeRemainder(Items.STICK)));
    public static final Item COOKED_CHICKEN_SKEWER = register("cooked_chicken_skewer", new SkewerItem(BBQDFoods.COOKED_CHICKEN_SKEWER, false));
    public static final Item LAMB_SKEWER = register("lamb_skewer", new Item(new Item.Settings().recipeRemainder(Items.STICK)));
    public static final Item COOKED_LAMB_SKEWER = register("cooked_lamb_skewer", new SkewerItem(BBQDFoods.COOKED_LAMB_SKEWER, true));
    public static final Item RABBIT_SKEWER = register("rabbit_skewer", new Item(new Item.Settings().recipeRemainder(Items.STICK)));
    public static final Item COOKED_RABBIT_SKEWER = register("cooked_rabbit_skewer", new SkewerItem(BBQDFoods.COOKED_RABBIT_SKEWER, true));
    public static final Item PORK_SAUSAGE_SKEWER = register("pork_sausage_skewer", new Item(new Item.Settings().recipeRemainder(Items.STICK)));
    public static final Item GRILLED_PORK_SAUSAGE_SKEWER = register("grilled_pork_sausage_skewer", new SkewerItem(BBQDFoods.GRILLED_PORK_SAUSAGE_SKEWER, true));
    public static final Item POTATO_SKEWER = register("potato_skewer", new Item(new Item.Settings().recipeRemainder(Items.STICK)));
    public static final Item BAKED_POTATO_SKEWER = register("baked_potato_skewer", new SkewerItem(BBQDFoods.BAKED_POTATO_SKEWER, true));


    private static Item register(String id, Item item){
        Item i =  Items.register(BarbequesDelight.asID(id), item);
        ItemGroupEvents.modifyEntriesEvent(BarbequesDelight.ITEM_GROUP).register(entries -> entries.add(i));
        return i;
    }

    public static void registerBBQDItems(){
        BarbequesDelight.LOGGER.debug("Register BBQD Items For" + BarbequesDelight.MODID);
    }
}
