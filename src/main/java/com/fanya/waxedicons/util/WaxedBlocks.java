package com.fanya.waxedicons.util;

import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class WaxedBlocks {
    public static final Set<Item> WAXED_BLOCKS = new HashSet<>();

    public static Identifier getCustomIcon() {
        return WaxedIconsConfigManager.getIconTexture();
    }

    public static void init(){
        WAXED_BLOCKS.add(Items.WAXED_COPPER_BLOCK);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_CHISELED_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_CHISELED_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_CHISELED_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_CHISELED_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_CUT_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_CUT_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_CUT_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_CUT_COPPER);
        WAXED_BLOCKS.add(Items.WAXED_CUT_COPPER_STAIRS);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_CUT_COPPER_STAIRS);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_CUT_COPPER_STAIRS);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
        WAXED_BLOCKS.add(Items.WAXED_CUT_COPPER_SLAB);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_CUT_COPPER_SLAB);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_CUT_COPPER_SLAB);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB);
        WAXED_BLOCKS.add(Items.WAXED_COPPER_DOOR);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_COPPER_DOOR);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_COPPER_DOOR);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_COPPER_DOOR);
        WAXED_BLOCKS.add(Items.WAXED_COPPER_GRATE);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_COPPER_GRATE);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_COPPER_GRATE);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_COPPER_GRATE);
        WAXED_BLOCKS.add(Items.WAXED_COPPER_BULB);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_COPPER_BULB);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_COPPER_BULB);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_COPPER_BULB);
        WAXED_BLOCKS.add(Items.WAXED_COPPER_TRAPDOOR);
        WAXED_BLOCKS.add(Items.WAXED_EXPOSED_COPPER_TRAPDOOR);
        WAXED_BLOCKS.add(Items.WAXED_OXIDIZED_COPPER_TRAPDOOR);
        WAXED_BLOCKS.add(Items.WAXED_WEATHERED_COPPER_TRAPDOOR);
    }
}
