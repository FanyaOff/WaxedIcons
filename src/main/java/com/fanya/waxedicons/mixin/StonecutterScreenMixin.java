package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.util.WaxedBlocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(StonecutterScreen.class)
public abstract class StonecutterScreenMixin {

    @Shadow
    private int scrollOffset;

    @Inject(method = "renderRecipeIcons", at = @At("TAIL"))
    private void onRenderRecipeIcons(DrawContext context, int x, int y, int scrollOffset, CallbackInfo ci) {
        StonecutterScreen screen = (StonecutterScreen) (Object) this;
        StonecutterScreenHandler handler = screen.getScreenHandler();

        List<StonecuttingRecipe> recipes = handler.getAvailableRecipes();

        Identifier iconTexture = WaxedBlocks.getCustomIcon();
        int size = Objects.equals(iconTexture, Identifier.of("waxedicons", "textures/gui/waxed_icon_alternative.png")) ? 8 : 6;

        for (int i = this.scrollOffset; i < scrollOffset && i < handler.getAvailableRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;

            assert MinecraftClient.getInstance().world != null;
            ItemStack resultStack = recipes.get(i).getOutput(MinecraftClient.getInstance().world.getRegistryManager());

            if (WaxedBlocks.WAXED_BLOCKS.contains(resultStack.getItem())) {
                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 300);
                context.drawTexture(
                        iconTexture,
                        k, m,
                        0, 0,
                        size, size,
                        size, size
                );
                context.getMatrices().pop();
            }
        }
    }
}
