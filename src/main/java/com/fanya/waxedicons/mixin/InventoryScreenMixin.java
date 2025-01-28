package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        renderIconsForItems(context);
    }

    @Unique
    private void renderIconsForItems(DrawContext context) {
        InventoryScreen screen = (InventoryScreen) (Object) this;
        int guiLeft = (screen.width - 176) / 2;
        int guiTop = (screen.height - 166) / 2;

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, WaxedBlocks.CUSTOM_ICON);

        for (Slot slot : screen.getScreenHandler().slots) {
            ItemStack stack = slot.getStack();

            if (WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
                int slotX = guiLeft + slot.x;
                int slotY = guiTop + slot.y;

                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 300);
                context.drawTexture(id -> RenderLayer.getGuiTextured(WaxedBlocks.CUSTOM_ICON), WaxedBlocks.CUSTOM_ICON, slotX, slotY, 0, 0, 6, 6, 6, 6);
                context.getMatrices().pop();
            }
        }

        RenderSystem.disableBlend();
    }
}
