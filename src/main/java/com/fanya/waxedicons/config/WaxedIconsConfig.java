package com.fanya.waxedicons.config;

import com.fanya.waxedicons.WaxediconsClient;
import net.minecraft.util.Identifier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WaxedIconsConfig {

    public String iconStyle = "default";
    public int iconOpacity = 80;

    public static final String[] AVAILABLE_STYLES = {
            "default",
            "alternative",
            "honeycomb"
    };

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = Paths.get("config", WaxediconsClient.MOD_ID + ".json");

    public Identifier getIconTexture() {
        return Identifier.of("waxedicons", "textures/gui/waxed_icon_" + iconStyle + ".png");
    }

    public Identifier getValidIconTexture() {
        return getIconTexture();
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_FILE.getParent());
            try (FileWriter writer = new FileWriter(CONFIG_FILE.toFile())) {
                GSON.toJson(this, writer);
            }
            WaxediconsClient.LOGGER.info("Config saved to: {}", CONFIG_FILE);
        } catch (IOException e) {
            WaxediconsClient.LOGGER.error("Failed to save config", e);
        }
    }

    public static WaxedIconsConfig load() {
        if (Files.exists(CONFIG_FILE)) {
            try (FileReader reader = new FileReader(CONFIG_FILE.toFile())) {
                WaxedIconsConfig config = GSON.fromJson(reader, WaxedIconsConfig.class);
                if (config != null) {
                    WaxediconsClient.LOGGER.info("Config loaded from: {}", CONFIG_FILE);
                    return config;
                }
            } catch (Exception e) {
                WaxediconsClient.LOGGER.error("Failed to load config, using defaults", e);
            }
        }
        
        WaxedIconsConfig defaultConfig = new WaxedIconsConfig();
        defaultConfig.save();
        return defaultConfig;
    }
}
