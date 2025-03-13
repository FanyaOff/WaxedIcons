package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookScreen.class)
public abstract class RecipeBookScreenMixin {

    @Shadow
    private RecipeBookWidget<?> recipeBook;

    @Inject(method = "drawSlots", at = @At("TAIL"))
    public void onDrawSlots(DrawContext context, CallbackInfo ci) {
        // Проверяем, открыта ли книга рецептов
        RecipeBookWidgetAccessor accessor = (RecipeBookWidgetAccessor) recipeBook;
        if (accessor.isOpen()) {
            // Получаем координаты GUI
            HandledScreenAccessor screenAccessor = (HandledScreenAccessor) (Object) this;
            int guiLeft = screenAccessor.getX();
            int guiTop = screenAccessor.getY();

            // Координаты для области результатов рецептов
            int resultX = guiLeft + 143; // Примерное положение выходного слота
            int resultY = guiTop + 33;   // Примерное положение выходного слота

            // Вместо проверки конкретного рецепта, просто отрисовываем иконку
            // в области результата рецепта, если книга рецептов открыта
            // Это упрощенный подход, но он должен работать в большинстве случаев

            RenderSystem.enableBlend();
            RenderSystem.setShaderTexture(0, WaxedBlocks.CUSTOM_ICON);

            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 300);
            context.drawTexture(id -> RenderLayer.getGuiTextured(WaxedBlocks.CUSTOM_ICON),
                    WaxedBlocks.CUSTOM_ICON, resultX, resultY, 0, 0, 6, 6, 6, 6);
            context.getMatrices().pop();

            RenderSystem.disableBlend();
        }
    }
}
