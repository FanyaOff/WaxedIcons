package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
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

    @Inject(method = "drawSlots", at = @At("TAIL"))
    public void onDrawSlots(DrawContext context, CallbackInfo ci) {
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;

        if (!isAllowedScreen(screen)) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, WaxedBlocks.CUSTOM_ICON);

        for (Slot slot : screen.getScreenHandler().slots) {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty() && WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
                int slotX = slot.x;
                int slotY = slot.y;

                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 300);
                context.drawTexture(id -> RenderLayer.getGuiTextured(WaxedBlocks.CUSTOM_ICON),
                        WaxedBlocks.CUSTOM_ICON, slotX, slotY, 0, 0, 6, 6, 6, 6);
                context.getMatrices().pop();
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
}
