package com.fanya.waxedicons.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

public class IconRenderer {

    public static void renderWaxedIconForSlot(DrawContext context, HandledScreen<?> screen, Slot slot) {
        ItemStack stack = slot.getStack();
        if (WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
            int guiLeft;
            int backgroundWidth = 176;

            if (screen instanceof RecipeBookScreen) {
                Slot firstSlot = screen.getScreenHandler().slots.get(0);
                int expectedSlotX = 8;

                if (firstSlot.x > expectedSlotX + 10) {
                    guiLeft = (screen.width + 154) / 2;
                } else {
                    guiLeft = (screen.width - backgroundWidth) / 2;
                }
            } else {
                guiLeft = (screen.width - backgroundWidth) / 2;
            }

            int guiTop = (screen.height - 166) / 2;

            int slotX = guiLeft + slot.x;
            int slotY = guiTop + slot.y;

            renderWaxedIcon(context, slotX, slotY);
        }
    }

    public static void renderWaxedIcon(DrawContext context, int x, int y) {
        Identifier icon = WaxedBlocks.CUSTOM_ICON;

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, icon);

        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 300);
        context.drawTexture(id -> RenderLayer.getGuiTextured(icon), icon, x, y, 0, 0, 6, 6, 6, 6);
        context.getMatrices().pop();

        RenderSystem.disableBlend();
    }
}
