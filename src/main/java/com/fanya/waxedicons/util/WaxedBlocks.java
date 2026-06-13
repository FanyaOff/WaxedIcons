package com.fanya.waxedicons.util;

import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WaxedBlocks {
    private static final Logger LOGGER = LoggerFactory.getLogger("waxedicons.WaxedBlocks");

    public static final Set<Item> WAXED_BLOCKS = new HashSet<>();

    public static Identifier getCustomIcon() {
        return WaxedIconsConfigManager.getIconTexture();
    }

    public static void init() {
        WAXED_BLOCKS.clear();

        BuiltInRegistries.ITEM.forEach(item -> {
            Identifier id = BuiltInRegistries.ITEM.getKey(item);
            if (id != null) {
                String path = id.getPath().toLowerCase();
                if (path.contains("waxed")) {
                    WAXED_BLOCKS.add(item);
                }
            }
        });

        LOGGER.info("[WaxedIcons] Found waxed blocks: {}", WAXED_BLOCKS.size());
    }

    public static boolean isWaxed(Item item) {
        return WAXED_BLOCKS.contains(item);
    }

    public static Set<Item> getWaxedBlocks() {
        return Collections.unmodifiableSet(WAXED_BLOCKS);
    }
}
