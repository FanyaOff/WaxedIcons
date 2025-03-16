package com.fanya.waxedicons;

import com.fanya.waxedicons.config.WaxedIconsConfig;
import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import com.fanya.waxedicons.entries.StylesPreviewEntry;
import com.fanya.waxedicons.util.WaxedBlocks;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WaxediconsClient implements ClientModInitializer {
    public static final String MOD_ID = "waxedicons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding configKeyBinding;
    public static String currentStyle = "default";

    @Override
    public void onInitializeClient() {
        WaxedIconsConfigManager.init();
        currentStyle = WaxedIconsConfigManager.getConfig().iconStyle;

        WaxedBlocks.init();

        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.waxedicons.config",
                GLFW.GLFW_KEY_F8,
                "category.waxedicons"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKeyBinding.wasPressed()) {
                client.setScreen(createConfigScreen(client.currentScreen));
            }
        });

        LOGGER.info("WaxedIcons initialized");
    }

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.waxedicons.config"));

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("category.waxedicons.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startTextDescription(Text.translatable("description.waxedicons.click_to_select"))
                .build()
        );

        general.addEntry(entryBuilder.startTextDescription(Text.translatable("preview.waxedicons.title"))
                .build()
        );

        general.addEntry(entryBuilder.startSelector(
                        Text.translatable("option.waxedicons.icon_style"),
                        WaxedIconsConfig.AVAILABLE_STYLES,
                        currentStyle)
                .setTooltip(Text.translatable("tooltip.waxedicons.icon_style"))
                .setSaveConsumer(newValue -> {
                    WaxedIconsConfigManager.getConfig().iconStyle = newValue;
                    currentStyle = newValue;
                    LOGGER.info("Icon style saved: {}", newValue);
                })
                .build()
        );

        general.addEntry(new StylesPreviewEntry(Text.of("")));

        builder.setSavingRunnable(() -> {
            AutoConfig.getConfigHolder(WaxedIconsConfig.class).save();
            LOGGER.info("Config saved, current style: {}", currentStyle);
        });

        return builder.build();
    }
}
