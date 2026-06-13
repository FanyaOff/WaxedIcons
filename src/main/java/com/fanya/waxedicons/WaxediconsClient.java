package com.fanya.waxedicons;

import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import com.fanya.waxedicons.gui.WaxedIconsConfigScreen;
import com.fanya.waxedicons.util.WaxedBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ExtractItemDecorationsCallback;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaxediconsClient implements ClientModInitializer {
    public static final String MOD_ID = "waxedicons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyMapping configKeyBinding;

    @Override
    public void onInitializeClient() {
        WaxedIconsConfigManager.init();
        WaxedBlocks.init();

        configKeyBinding = KeyMappingHelper.registerKeyMapping(WaxedIconsKeybinding.CONFIG_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKeyBinding.consumeClick()) {
                client.setScreen(createConfigScreen(client.screen));
            }
        });

        ExtractItemDecorationsCallback.EVENT.register(WaxediconsClient::extractWaxedIcon);

        LOGGER.info("WaxedIcons initialized");
    }

    public static Screen createConfigScreen(Screen parent) {
        return new WaxedIconsConfigScreen(parent);
    }

    private static void extractWaxedIcon(GuiGraphicsExtractor graphics, net.minecraft.client.gui.Font font, ItemStack stack, int x, int y) {
        if (stack.isEmpty() || !WaxedBlocks.isWaxed(stack.getItem()) || WaxedIconsConfigManager.getIconOpacity() <= 0.0F) {
            return;
        }

        renderWaxedIndicator(graphics, x, y);
    }

    private static void renderWaxedIndicator(GuiGraphicsExtractor graphics, int x, int y) {
        Identifier iconTexture = WaxedBlocks.getCustomIcon();
        String path = iconTexture.getPath();
        int size = path.contains("alternative") ? 8 : (path.contains("honeycomb") ? 7 : 6);
        int color = (int) (WaxedIconsConfigManager.getIconOpacity() * 255.0F) << 24 | 0xFFFFFF;
        int iconX = x;
        int iconY = y;

        switch (WaxedIconsConfigManager.getIconCorner()) {
            case "top_left" -> {
                iconX = x;
                iconY = y;
            }
            case "bottom_left" -> {
                iconX = x;
                iconY = y + 16 - size;
            }
            case "bottom_right" -> {
                iconX = x + 16 - size;
                iconY = y + 16 - size;
            }
            default -> {
                iconX = x + 16 - size;
                iconY = y;
            }
        }

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                iconTexture,
                iconX, iconY,
                0.0F, 0.0F,
                size, size,
                size, size,
                color
        );
    }
}
