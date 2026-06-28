package com.fanya.waxedicons.gui;

import com.fanya.waxedicons.config.WaxedIconsConfig;
import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class WaxedIconsConfigScreen extends Screen {
    private static final Identifier PREVIEW_BLOCK_ID = Identifier.withDefaultNamespace("copper_block");
    private static final Identifier DEFAULT_ICON = Identifier.fromNamespaceAndPath("waxedicons", "textures/gui/waxed_icon_default.png");
    private static final Identifier ALTERNATIVE_ICON = Identifier.fromNamespaceAndPath("waxedicons", "textures/gui/waxed_icon_alternative.png");
    private static final Identifier HONEYCOMB_ICON = Identifier.fromNamespaceAndPath("waxedicons", "textures/gui/waxed_icon_honeycomb.png");

    private final Screen parent;
    private final WaxedIconsConfig config;
    private ItemStack previewItem = ItemStack.EMPTY;

    public WaxedIconsConfigScreen(Screen parent) {
        super(Component.translatable("title.waxedicons.config"));
        this.parent = parent;
        this.config = WaxedIconsConfigManager.getConfig();
    }

    @Override
    protected void init() {
        super.init();
        this.config.validate();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int leftX = centerX - 160;
        int startY = centerY - 46;

        Button styleButton = Button.builder(getStyleText(), button -> {
            cycleStyle();
            button.setMessage(getStyleText());
        }).bounds(leftX, startY, 150, 20).build();

        Button cornerButton = Button.builder(getCornerText(), button -> {
            cycleCorner();
            button.setMessage(getCornerText());
        }).bounds(leftX, startY + 28, 150, 20).build();

        OpacitySlider opacitySlider = new OpacitySlider(leftX, startY + 56, 150, 20, this.config.iconOpacity / 100.0);

        this.addRenderableWidget(styleButton);
        this.addRenderableWidget(cornerButton);
        this.addRenderableWidget(opacitySlider);

        int rightX = centerX + 10;
        LivePreviewWidget livePreview = new LivePreviewWidget(rightX, startY, 150, 76);
        this.addRenderableWidget(livePreview);

        int buttonY = this.height - 30;
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> this.saveAndClose())
                .bounds(centerX - 155, buttonY, 150, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), button -> this.minecraft.gui.setScreen(this.parent))
                .bounds(centerX + 5, buttonY, 150, 20)
                .build());
    }

    private Component getStyleText() {
        return Component.translatable("option.waxedicons.icon_style").append(": ").append(Component.translatable("style.waxedicons." + this.config.iconStyle));
    }

    private Component getCornerText() {
        return Component.translatable("option.waxedicons.icon_corner").append(": ").append(Component.translatable("corner.waxedicons." + this.config.iconCorner));
    }

    private void cycleStyle() {
        int index = Arrays.asList(WaxedIconsConfig.AVAILABLE_STYLES).indexOf(this.config.iconStyle);
        this.config.iconStyle = WaxedIconsConfig.AVAILABLE_STYLES[(index + 1) % WaxedIconsConfig.AVAILABLE_STYLES.length];
    }

    private void cycleCorner() {
        int index = Arrays.asList(WaxedIconsConfig.AVAILABLE_CORNERS).indexOf(this.config.iconCorner);
        this.config.iconCorner = WaxedIconsConfig.AVAILABLE_CORNERS[(index + 1) % WaxedIconsConfig.AVAILABLE_CORNERS.length];
    }

    private void saveAndClose() {
        this.config.validate();
        WaxedIconsConfigManager.saveConfig();
        this.minecraft.gui.setScreen(this.parent);
    }

    @Override
    public void onClose() {
        this.config.validate();
        this.minecraft.gui.setScreen(this.parent);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
        graphics.fillGradient(0, 0, this.width, this.height, 0xC0101010, 0xD0101010);
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        super.extractRenderState(graphics, mouseX, mouseY, delta);
    }

    private ItemStack createPreviewItem() {
        if (!this.previewItem.isEmpty()) {
            return this.previewItem;
        }

        this.previewItem = BuiltInRegistries.ITEM.get(PREVIEW_BLOCK_ID)
                .map(holder -> {
                    bindPreviewBlockComponents(holder);
                    return new ItemStack(holder);
                })
                .orElse(ItemStack.EMPTY);
        return this.previewItem;
    }

    private static void bindPreviewBlockComponents(Holder.Reference<Item> holder) {
        if (holder.areComponentsBound()) {
            return;
        }

        holder.bindComponents(DataComponentMap.builder()
                .addAll(DataComponents.COMMON_ITEM_COMPONENTS)
                .set(DataComponents.ITEM_NAME, Component.translatable("block.minecraft.copper_block"))
                .set(DataComponents.ITEM_MODEL, PREVIEW_BLOCK_ID)
                .build());
    }

    private Identifier selectedIconTexture() {
        return switch (this.config.iconStyle) {
            case "alternative" -> ALTERNATIVE_ICON;
            case "honeycomb" -> HONEYCOMB_ICON;
            default -> DEFAULT_ICON;
        };
    }

    private static int baseIconSize(Identifier texture) {
        String path = texture.getPath();
        if (path.contains("alternative")) return 8;
        if (path.contains("honeycomb")) return 7;
        return 6;
    }

    private static int[] cornerPosition(int x, int y, int slotSize, int iconSize, String corner) {
        return switch (corner) {
            case "top_left" -> new int[]{x, y};
            case "bottom_left" -> new int[]{x, y + slotSize - iconSize};
            case "bottom_right" -> new int[]{x + slotSize - iconSize, y + slotSize - iconSize};
            default -> new int[]{x + slotSize - iconSize, y};
        };
    }

    private void drawPreviewItem(GuiGraphicsExtractor graphics, int x, int y, int size) {
        ItemStack item = this.createPreviewItem();
        if (item.isEmpty()) return;

        graphics.pose().pushMatrix();
        float scale = size / 16.0F;
        graphics.pose().scale(scale, scale);
        graphics.item(item, (int) (x / scale), (int) (y / scale));
        graphics.pose().popMatrix();
    }

    private void drawIcon(GuiGraphicsExtractor graphics, Identifier iconTexture, int slotX, int slotY, int slotSize) {
        float scale = slotSize / 16.0F;
        int iconSize = Math.max(4, Math.round(baseIconSize(iconTexture) * scale));
        int[] iconPos = cornerPosition(slotX, slotY, slotSize, iconSize, this.config.iconCorner);
        int color = (int) (this.config.iconOpacity / 100.0F * 255.0F) << 24 | 0xFFFFFF;

        graphics.blit(RenderPipelines.GUI_TEXTURED, iconTexture, iconPos[0], iconPos[1],
                0.0F, 0.0F, iconSize, iconSize, iconSize, iconSize, color);
    }

    private class LivePreviewWidget extends AbstractWidget {
        public LivePreviewWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty());
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
            int slotSize = 48;
            int slotX = this.getX() + (this.width - slotSize) / 2;
            int slotY = this.getY() + (this.height - slotSize) / 2 + 4;

            graphics.centeredText(WaxedIconsConfigScreen.this.font, Component.translatable("preview.waxedicons.live"), this.getX() + this.width / 2, this.getY() - 4, 0xAAAAAA);

            graphics.fill(slotX - 2, slotY - 2, slotX + slotSize + 2, slotY + slotSize + 2, 0xFF2A2A2A);
            graphics.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0xFF8B8B8B);
            
            WaxedIconsConfigScreen.this.drawPreviewItem(graphics, slotX, slotY, slotSize);
            WaxedIconsConfigScreen.this.drawIcon(graphics, selectedIconTexture(), slotX, slotY, slotSize);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narration) {}
    }

    private class OpacitySlider extends AbstractSliderButton {
        public OpacitySlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Component.empty(), value);
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            int opacity = (int) Math.round(this.value * 100);
            this.setMessage(Component.translatable("option.waxedicons.opacity", opacity + "%"));
        }

        @Override
        protected void applyValue() {
            config.iconOpacity = (int) Math.round(this.value * 100);
        }
    }
}
