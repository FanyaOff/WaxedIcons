package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Unique
    private static final List<Class<?>> DISALLOWED_CONTAINERS = Arrays.asList(
            CreativeInventoryScreen.class,
            InGameHud.class,
            InventoryScreen.class
    );

    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void waxedicons$drawWaxedIcon(DrawContext context, Slot slot, CallbackInfo ci) {
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;

        if (!isAllowedScreen(screen)) return;

        ItemStack stack = slot.getStack();
        if (stack.isEmpty()) return;
        if (!WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) return;

        Identifier iconTexture = WaxedBlocks.getCustomIcon();
        int size = 6;
        Identifier alt = Identifier.of("waxedicons", "textures/gui/waxed_icon_alternative.png");
        if (Objects.equals(iconTexture, alt)) size = 8;

        RenderSystem.setShaderTexture(0, iconTexture);
        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 300); // поверх предмета
        context.drawTexture(iconTexture, slot.x, slot.y, 0, 0, size, size, size, size);
        context.getMatrices().pop();
    }

    @Unique
    private boolean isAllowedScreen(HandledScreen<?> screen) {
        for (Class<?> disallowed : DISALLOWED_CONTAINERS) {
            if (disallowed.isInstance(screen)) {
                return false;
            }
        }
        return true;
    }
}
