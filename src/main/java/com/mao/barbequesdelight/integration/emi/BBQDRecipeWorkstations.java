package com.mao.barbequesdelight.integration.emi;

import com.mao.barbequesdelight.registry.BBQDItems;
import dev.emi.emi.api.stack.EmiStack;

public class BBQDRecipeWorkstations {
    public static final EmiStack GRILLING = EmiStack.of(BBQDItems.GRILL);
    public static final EmiStack SKEWERING = EmiStack.of(BBQDItems.INGREDIENTS_BASIN);
}
