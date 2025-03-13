package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {

    @Inject(
            method = "drawItemWithoutEntity",
            at = @At("RETURN")
    )
    private void afterDrawItem(ItemStack stack, int x, int y, CallbackInfo ci) {
        // Проверяем, нужно ли рисовать иконку
        if (stack != null && !stack.isEmpty() && WaxedBlocks.WAXED_BLOCKS.contains(stack.getItem())) {
            // Получаем текущий контекст отрисовки
            DrawContext context = (DrawContext) (Object) this;

            // Рисуем иконку
            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 300);
            context.drawTexture(id -> RenderLayer.getGuiTextured(WaxedBlocks.CUSTOM_ICON),
                    WaxedBlocks.CUSTOM_ICON, x, y, 0, 0, 6, 6, 6, 6);
            context.getMatrices().pop();
        }
    }
}
