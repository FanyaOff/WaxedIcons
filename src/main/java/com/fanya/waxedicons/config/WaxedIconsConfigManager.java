package com.fanya.waxedicons.config;

import com.fanya.waxedicons.WaxediconsClient;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.util.Identifier;

public class WaxedIconsConfigManager {
    private static WaxedIconsConfig CONFIG;

    public static void init() {
        AutoConfig.register(WaxedIconsConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(WaxedIconsConfig.class).getConfig();
        WaxediconsClient.LOGGER.info("WaxedIcons config initialized");
    }

    public static WaxedIconsConfig getConfig() {
        return CONFIG;
    }

    public static Identifier getIconTexture() {
        return CONFIG.getValidIconTexture();
    }
}
