package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderHotbar", at = @At("TAIL"))
    public void onRenderHUD(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) return;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            if (WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
                int x = client.getWindow().getScaledWidth() / 2 - 88 + i * 20;
                int y = client.getWindow().getScaledHeight() - 19;

                RenderSystem.enableBlend();
                RenderSystem.setShaderTexture(0, WaxedBlocks.CUSTOM_ICON);

                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 300);
                context.drawTexture(id -> RenderLayer.getGuiTextured(WaxedBlocks.CUSTOM_ICON),
                        WaxedBlocks.CUSTOM_ICON, x, y, 0, 0, 6, 6, 6, 6);
                context.getMatrices().pop();

                RenderSystem.disableBlend();
            }
        }
    }
}
