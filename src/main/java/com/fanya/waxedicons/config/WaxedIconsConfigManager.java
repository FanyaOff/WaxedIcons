package com.fanya.waxedicons.config;

import com.fanya.waxedicons.WaxediconsClient;
import net.minecraft.util.Identifier;

public class WaxedIconsConfigManager {
    private static WaxedIconsConfig CONFIG;

    public static void init() {
        CONFIG = WaxedIconsConfig.load();
        WaxediconsClient.LOGGER.info("WaxedIcons config initialized");
    }

    public static WaxedIconsConfig getConfig() {
        return CONFIG;
    }

    public static void saveConfig() {
        if (CONFIG != null) {
            CONFIG.save();
        }
    }

    public static Identifier getIconTexture() {
        return CONFIG.getValidIconTexture();
    }

    public static float getIconOpacity() {
        return CONFIG.iconOpacity / 100.0f;
    }
}
