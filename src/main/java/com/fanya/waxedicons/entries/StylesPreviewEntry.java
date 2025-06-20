package com.fanya.waxedicons.entries;

import com.fanya.waxedicons.config.WaxedIconsConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.fanya.waxedicons.WaxediconsClient.currentStyle;

public class StylesPreviewEntry extends TooltipListEntry<String> {
    private final ItemStack previewItem;
    private final int PREVIEW_SIZE = 64;

    public StylesPreviewEntry(Text fieldName) {
        super(fieldName, null);
        this.previewItem = new ItemStack(Items.WAXED_COPPER_BLOCK);
    }

    @Override
    public String getValue() {
        return currentStyle;
    }

    @Override
    public Optional<String> getDefaultValue() {
        return Optional.of(currentStyle);
    }

    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends Selectable> narratables() {
        return Collections.emptyList();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {

        int totalStyles = WaxedIconsConfig.AVAILABLE_STYLES.length;
        int SPACING = 20;
        int totalWidth = totalStyles * (PREVIEW_SIZE + SPACING) - SPACING;

        int startX = x + (entryWidth - totalWidth) / 2;
        int startY = y + 10;

        int hoveredIndex = -1;
        for (int i = 0; i < totalStyles; i++) {
            int previewX = startX + i * (PREVIEW_SIZE + SPACING);

            if (mouseX >= previewX && mouseX < previewX + PREVIEW_SIZE &&
                    mouseY >= startY && mouseY < startY + PREVIEW_SIZE + 20) {
                hoveredIndex = i;
                break;
            }
        }

        for (int i = 0; i < totalStyles; i++) {
            String style = WaxedIconsConfig.AVAILABLE_STYLES[i];
            boolean isCurrentStyle = style.equals(currentStyle);
            boolean isHoveredStyle = i == hoveredIndex;

            int previewX = startX + i * (PREVIEW_SIZE + SPACING);

            int borderColor;
            if (isHoveredStyle) {
                borderColor = 0xFF00FFFF;
            } else if (isCurrentStyle) {
                borderColor = 0xFFFFFF00;
            } else {
                borderColor = 0xFF555555;
            }

            context.fill(previewX - 2, startY - 2, previewX + PREVIEW_SIZE + 2, startY + PREVIEW_SIZE + 2, borderColor);
            context.fill(previewX - 1, startY - 1, previewX + PREVIEW_SIZE + 1, startY + PREVIEW_SIZE + 1, 0xFF222222);
            context.fill(previewX, startY, previewX + PREVIEW_SIZE, startY + PREVIEW_SIZE, 0xFF000000);

            context.getMatrices().push();
            context.getMatrices().translate(previewX + (float) PREVIEW_SIZE / 2 - 16, startY + (float) PREVIEW_SIZE / 2 - 16, 0);
            context.getMatrices().scale(2.0f, 2.0f, 1.0f);
            context.drawItem(this.previewItem, 0, 0);
            context.getMatrices().pop();


            Identifier iconTexture = Identifier.of("waxedicons", "textures/gui/waxed_icon_" + style + ".png");

            //GpuTexture gpuTexture = MinecraftClient.getInstance().getTextureManager().getTexture(iconTexture).getGlTexture();
            RenderSystem.setShaderTexture(0, iconTexture);

            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 300);

            int iconX = previewX + PREVIEW_SIZE / 2 - 16;
            int iconY = startY + PREVIEW_SIZE / 2 - 16;
            int iconSize = 12;

            context.drawTexture(/*id -> RenderLayer.getGuiTextured(iconTexture),*/iconTexture, iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);

            context.getMatrices().pop();

            String styleName = style.substring(0, 1).toUpperCase() + style.substring(1);

            int textColor;
            if (isHoveredStyle) {
                textColor = 0xFF00FFFF;
            } else if (isCurrentStyle) {
                textColor = 0xFFFFFF00;
            } else {
                textColor = 0xFFFFFFFF;
            }

            Text nameText = Text.literal(styleName);
            int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(nameText);
            context.drawText(MinecraftClient.getInstance().textRenderer,
                    nameText,
                    previewX + (PREVIEW_SIZE - textWidth) / 2,
                    startY + PREVIEW_SIZE + 5,
                    textColor,
                    true);
        }
    }

    @Override
    public int getItemHeight() {
        return PREVIEW_SIZE + 30;
    }

    @Override
    public void save() {

    }
}
