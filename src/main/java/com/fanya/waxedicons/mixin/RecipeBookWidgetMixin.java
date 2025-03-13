package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeBookWidget.class)
public abstract class RecipeBookWidgetMixin {

    @Shadow
    private boolean open;

    @Shadow
    private MinecraftClient client;

    @Shadow
    private GhostRecipe ghostRecipe;

    @Inject(method = "drawGhostSlots", at = @At("TAIL"))
    public void onDrawGhostSlots(DrawContext context, boolean resultHasPadding, CallbackInfo ci) {
        if (!this.open || this.client == null || this.client.world == null || this.client.player == null) {
            return;
        }

        // Проверяем предметы в руках игрока
        ItemStack mainHandStack = this.client.player.getMainHandStack();
        ItemStack offHandStack = this.client.player.getOffHandStack();

        // Если игрок держит покрытый воском блок, отображаем иконку в области результата
        if (WaxedBlocks.WAXED_BLOCKS.contains(mainHandStack.getItem()) ||
                WaxedBlocks.WAXED_BLOCKS.contains(offHandStack.getItem())) {

            // Примерные координаты для области результата рецепта
            int x = 97; // Примерное положение выходного слота
            int y = 19; // Примерное положение выходного слота

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
