package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.display.CuttingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.display.SlotDisplayContexts;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(StonecutterScreen.class)
public class StonecutterScreenMixin {

    @Shadow
    private int scrollOffset;

    @Inject(method = "renderRecipeIcons", at = @At("TAIL"))
    private void onRenderRecipeIcons(DrawContext context, int x, int y, int scrollOffset, CallbackInfo ci) {
        StonecutterScreen screen = (StonecutterScreen) (Object) this;
        StonecutterScreenHandler handler = screen.getScreenHandler();

        CuttingRecipeDisplay.Grouping<?> grouping = handler.getAvailableRecipes();
        assert MinecraftClient.getInstance().world != null;
        ContextParameterMap contextParameterMap = SlotDisplayContexts.createParameters(MinecraftClient.getInstance().world);

        Identifier iconTexture = WaxedBlocks.getCustomIcon();
        int size = Objects.equals(iconTexture, Identifier.of("waxedicons", "textures/gui/waxed_icon_alternative.png")) ? 8 : 6;


        for (int i = this.scrollOffset; i < scrollOffset && i < grouping.size(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;

            SlotDisplay slotDisplay = ((CuttingRecipeDisplay.GroupEntry<?>) grouping.entries().get(i)).recipe().optionDisplay();
            ItemStack resultStack = slotDisplay.getFirst(contextParameterMap);

            if (WaxedBlocks.WAXED_BLOCKS.contains(resultStack.getItem())) {
                context.getMatrices().pushMatrix();

                context.drawTexture(RenderPipelines.GUI_TEXTURED, iconTexture,
                        k, m, 0.0f, 0.0f, size, size, size, size);

                context.getMatrices().popMatrix();
            }
        }
    }
}
