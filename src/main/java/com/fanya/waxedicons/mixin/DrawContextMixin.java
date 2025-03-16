package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Inject(
            method = "drawItemWithoutEntity*",
            at = @At("RETURN")
    )
    private void afterDrawItem(ItemStack stack, int x, int y, CallbackInfo ci) {
        if (stack != null && !stack.isEmpty() && WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
            Identifier iconTexture = WaxedBlocks.getCustomIcon();
            int size = Objects.equals(iconTexture, Identifier.of("waxedicons", "textures/gui/waxed_icon_alternative.png")) ? 8 : 6;

            DrawContext context = (DrawContext) (Object) this;

            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 300);
            context.drawTexture(id -> RenderLayer.getGuiTextured(iconTexture),
                    iconTexture, x, y, 0, 0, size, size, size, size);
            context.getMatrices().pop();
        }
    }
}
