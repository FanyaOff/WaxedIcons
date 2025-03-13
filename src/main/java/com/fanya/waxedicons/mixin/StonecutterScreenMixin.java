package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.display.CuttingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.context.ContextParameterMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StonecutterScreen.class)
public class StonecutterScreenMixin {

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        StonecutterScreen screen = (StonecutterScreen) (Object) this;
        StonecutterScreenHandler handler = screen.getScreenHandler();
        MinecraftClient client = MinecraftClient.getInstance();

        int backgroundWidth = 176;
        int backgroundHeight = 166;
        int guiLeft = (screen.width - backgroundWidth) / 2;
        int guiTop = (screen.height - backgroundHeight) / 2;

        RenderSystem.enableBlend();
        RenderSystem.setShaderTexture(0, WaxedBlocks.CUSTOM_ICON);

        // Отрисовка иконок для слотов с предметами
        for (Slot slot : handler.slots) {
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

        // Отрисовка иконок для результатов каменореза
        if (handler.getAvailableRecipeCount() > 0) {
            CuttingRecipeDisplay.Grouping<?> grouping = handler.getAvailableRecipes();
            ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(client.world);

            // Проверяем, содержат ли рецепты покрытые воском блоки
            boolean hasWaxedResult = false;
            ItemStack outputStack = handler.getSlot(StonecutterScreenHandler.OUTPUT_ID).getStack();
            if (WaxedBlocks.WAXED_BLOCKS.contains(outputStack.getItem())) {
                hasWaxedResult = true;
            }

            if (hasWaxedResult) {
                // Отрисовка иконок в списке рецептов
                for (int i = 0; i < handler.getAvailableRecipeCount() && i < grouping.size(); i++) {
                    int column = i % 4;
                    int row = i / 4;
                    int recipeX = guiLeft + 52 + column * 16;
                    int recipeY = guiTop + 14 + row * 18 + 2;

                    context.getMatrices().push();
                    context.getMatrices().translate(0, 0, 300);
                    context.drawTexture(id -> RenderLayer.getGuiTextured(WaxedBlocks.CUSTOM_ICON),
                            WaxedBlocks.CUSTOM_ICON, recipeX, recipeY, 0, 0, 6, 6, 6, 6);
                    context.getMatrices().pop();
                }
            }
        }

        RenderSystem.disableBlend();
    }
}
