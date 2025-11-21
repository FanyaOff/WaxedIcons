package com.fanya.waxedicons.mixin;

import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import com.fanya.waxedicons.util.WaxedBlocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Inject(
            method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;III)V",
            at = @At("TAIL")
    )
    void waxedicons$injectDrawItem(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, CallbackInfo ci) {
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
        DrawContext context = (DrawContext) (Object) this;
        Matrix3x2fStack matrices = context.getMatrices();
        
        Identifier iconTexture = WaxedBlocks.getCustomIcon();
        String path = iconTexture.getPath();
        int size = path.contains("alternative") ? 8 : (path.contains("honeycomb") ? 7 : 6);
        float alpha = WaxedIconsConfigManager.getIconOpacity();

        matrices.pushMatrix();
        matrices.translate(0.0F, 0.0F);
        
        int color = (int) (alpha * 255) << 24 | 0xFFFFFF;

        context.drawTexture(
                net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED,
                iconTexture,
                x, y,
                0.0F, 0.0F,
                size, size,
                size, size,
                color
        );

        matrices.popMatrix();
    }
}