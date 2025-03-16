package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        CreativeInventoryScreen screen = (CreativeInventoryScreen) (Object) this;
        int guiLeft = (screen.width - 195) / 2;
        int guiTop = (screen.height - 136) / 2;
        Identifier iconTexture = WaxedBlocks.getCustomIcon();
        int size = Objects.equals(iconTexture, Identifier.of("waxedicons", "textures/gui/waxed_icon_alternative.png")) ? 8 : 6;

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, iconTexture);

        for (Slot slot : screen.getScreenHandler().slots) {
            ItemStack stack = slot.getStack();

            if (WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
                int slotX = guiLeft + slot.x;
                int slotY = guiTop + slot.y;

                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 300);
                context.drawTexture(id -> RenderLayer.getGuiTextured(iconTexture),
                        iconTexture, slotX, slotY, 0, 0, size, size, size, size);
                context.getMatrices().pop();
            }
        }

        RenderSystem.disableBlend();
    }
}
