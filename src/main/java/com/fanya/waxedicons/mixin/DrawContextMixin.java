package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import com.fanya.waxedicons.util.WaxedBlocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow
    protected abstract void drawTexturedQuad(Function<Identifier, RenderLayer> renderLayers, Identifier sprite, int x1, int x2, int y1, int y2, float u1, float u2, float v1, float v2, int color);

    @Shadow
    @Final
    private MatrixStack matrices;

    @Inject(
        method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
        at = @At("TAIL")
    )
    void waxedicons$injectDrawItem(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo ci) {
        if (
            stack != null && !stack.isEmpty()
            && WaxedBlocks.isWaxed(stack.getItem())
            && WaxedIconsConfigManager.getIconOpacity() > 0F
        ) {
            waxedicons$renderWaxedIndicator(x, y);
        }
    }

    @Unique
    private void waxedicons$renderWaxedIndicator(int x, int y) {
        Identifier iconTexture = WaxedBlocks.getCustomIcon();
        String path = iconTexture.getPath();
        int size = path.contains("alternative") ? 8 : (path.contains("honeycomb") ? 7 : 6);
        float alpha = WaxedIconsConfigManager.getIconOpacity();
        
        this.matrices.push();
        this.matrices.translate(0.0F, 0.0F, 300);
        
        // Convert alpha to color int (ARGB format)
        int color = (int) (alpha * 255) << 24 | 0xFFFFFF; // Alpha in high byte, white RGB
        
        // Use the updated drawTexturedQuad method signature for 1.21.2
        drawTexturedQuad(
            RenderLayer::getGuiTextured, iconTexture, x, x + size, y, y + size,
            0.0f, 1.0f, 0.0f, 1.0f, color
        );
        
        this.matrices.pop();
    }
}
