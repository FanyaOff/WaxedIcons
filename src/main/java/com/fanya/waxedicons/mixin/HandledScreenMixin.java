package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.WaxediconsClient;
import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Unique
    private static final List<Class<?>> DISALLOWED_CONTAINERS = Arrays.asList(
            CreativeInventoryScreen.class,
            InGameHud.class,
            InventoryScreen.class
    );

    @Inject(method = "render", at = @At("TAIL"))
    public void onRenderScreen(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;

        if (!isAllowedScreen(screen)) {
            return;
        }

        int rows = 0;
        if (screen.getScreenHandler() instanceof GenericContainerScreenHandler containerHandler) {
            rows = containerHandler.getRows();
        }

        int backgroundWidth = getBackgroundWidth(screen);
        int backgroundHeight = getBackgroundHeight(screen, rows);

        int guiLeft = (screen.width - backgroundWidth) / 2;
        int guiTop = (screen.height - backgroundHeight) / 2;

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, WaxedBlocks.CUSTOM_ICON);

        for (Slot slot : screen.getScreenHandler().slots) {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty() && WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
                int x = guiLeft + slot.x;
                int y = guiTop + slot.y;
                renderWaxedIcon(context, x, y);
            }
        }

        RenderSystem.disableBlend();
    }

    @Unique
    private boolean isAllowedScreen(HandledScreen<?> screen) {
        for (Class<?> allowed : DISALLOWED_CONTAINERS) {
            if (allowed.isInstance(screen)) {
                return false;
            }
        }
        return true;
    }

    @Unique
    private void renderWaxedIcon(DrawContext context, int x, int y) {
        Identifier icon = WaxedBlocks.CUSTOM_ICON;

        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 300);
        context.drawTexture(id -> RenderLayer.getGuiTextured(WaxedBlocks.CUSTOM_ICON), icon, x, y, 0, 0, 6, 6, 6, 6);
        context.getMatrices().pop();
    }

    @Unique
    int getBackgroundWidth(Screen screen) {
        return screen instanceof BeaconScreen ? 230 : 176;
    }

    @Unique
    int getBackgroundHeight(Screen screen, int rows) {
        if (screen instanceof BeaconScreen) {
            return 219;
        } else if (screen instanceof GenericContainerScreen) {
            return 114 + rows * 18;
        } else {
            return 166;
        }
    }
}
