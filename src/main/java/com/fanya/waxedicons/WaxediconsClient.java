package com.fanya.waxedicons;

import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import com.fanya.waxedicons.gui.WaxedIconsConfigScreen;
import com.fanya.waxedicons.util.WaxedBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaxediconsClient implements ClientModInitializer {
    public static final String MOD_ID = "waxedicons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding configKeyBinding;

    @Override
    public void onInitializeClient() {
        WaxedIconsConfigManager.init();
        WaxedBlocks.init();

        configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.waxedicons.config",
                GLFW.GLFW_KEY_F8,
                KeyBinding.Category.create(Identifier.of("waxedicons", "category.waxedicons"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKeyBinding.wasPressed()) {
                client.setScreen(createConfigScreen(client.currentScreen));
            }
        });

        LOGGER.info("WaxedIcons initialized");
    }

    public static Screen createConfigScreen(Screen parent) {
        return new WaxedIconsConfigScreen(parent);
    }
}
