package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        renderIconsForItems(context);
    }

    @Unique
    private void renderIconsForItems(DrawContext context) {
        InventoryScreen screen = (InventoryScreen) (Object) this;

        // Получаем координаты GUI через аксессор
        HandledScreenAccessor accessor = (HandledScreenAccessor) screen;
        int guiLeft = accessor.getX();
        int guiTop = accessor.getY();

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, WaxedBlocks.CUSTOM_ICON);

        for (Slot slot : screen.getScreenHandler().slots) {
            ItemStack stack = slot.getStack();

            if (WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
                int slotX = guiLeft + slot.x;
                int slotY = guiTop + slot.y;

                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 300);
                context.drawTexture(id -> RenderLayer.getGuiTextured(WaxedBlocks.CUSTOM_ICON),
                        WaxedBlocks.CUSTOM_ICON, slotX, slotY, 0, 0, 6, 6, 6, 6);
                context.getMatrices().pop();
            }
        }

        RenderSystem.disableBlend();
    }
}
