package com.fanya.waxedicons.gui;

import com.fanya.waxedicons.config.WaxedIconsConfig;
import com.fanya.waxedicons.config.WaxedIconsConfigManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WaxedIconsConfigScreen extends Screen {
    private final Screen parent;
    private final WaxedIconsConfig config;
    private StylePreviewWidget defaultPreview;
    private StylePreviewWidget alternativePreview;
    private StylePreviewWidget honeycombPreview;
    private final ItemStack previewItem = new ItemStack(Items.COPPER_BLOCK);
    
    private static final Identifier DEFAULT_ICON = Identifier.of("waxedicons", "textures/gui/waxed_icon_default.png");
    private static final Identifier ALTERNATIVE_ICON = Identifier.of("waxedicons", "textures/gui/waxed_icon_alternative.png");
    private static final Identifier HONEYCOMB_ICON = Identifier.of("waxedicons", "textures/gui/waxed_icon_honeycomb.png");

    private static final int PANEL_COLOR = 0xFF2A2A2A;
    private static final int SELECTED_COLOR = 0xFF4A4A4A;
    private static final int HOVER_COLOR = 0xFF3A3A3A;
    private static final int ACCENT_COLOR = 0xFFFFD700;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int SUBTITLE_COLOR = 0xFFCCCCCC;
    private static final int BORDER_COLOR = 0xFF555555;

    public WaxedIconsConfigScreen(Screen parent) {
        super(Text.translatable("title.waxedicons.config"));
        this.parent = parent;
        this.config = WaxedIconsConfigManager.getConfig();
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int contentWidth = Math.min(400, this.width - 40);
        int startX = centerX - contentWidth / 2;
        int currentY = 40;

        currentY += 40;

        currentY += 20;

        int previewSize = Math.min(64, contentWidth / 5);
        int previewSpacing = (contentWidth - previewSize * 3) / 4;
        int previewY = currentY;
        
        this.defaultPreview = new StylePreviewWidget(
            startX + previewSpacing, previewY, previewSize, previewSize,
            DEFAULT_ICON, "default", config.iconStyle.equals("default")
        );
        
        this.alternativePreview = new StylePreviewWidget(
            startX + previewSpacing * 2 + previewSize, previewY, previewSize, previewSize,
            ALTERNATIVE_ICON, "alternative", config.iconStyle.equals("alternative")
        );
        
        this.honeycombPreview = new StylePreviewWidget(
            startX + previewSpacing * 3 + previewSize * 2, previewY, previewSize, previewSize,
            HONEYCOMB_ICON, "honeycomb", config.iconStyle.equals("honeycomb")
        );
        
        this.addDrawableChild(defaultPreview);
        this.addDrawableChild(alternativePreview);
        this.addDrawableChild(honeycombPreview);
        
        currentY += previewSize + 40;

        int sliderWidth = Math.min(300, contentWidth);
        ModernSliderWidget opacitySlider = new ModernSliderWidget(
                centerX - sliderWidth / 2, currentY, sliderWidth, 20,
                Text.translatable("option.waxedicons.opacity", config.iconOpacity + "%"),
                config.iconOpacity / 100.0
        );
        this.addDrawableChild(opacitySlider);

        int buttonY = this.height - 35;
        int buttonWidth = 80;
        
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), (button) -> this.saveAndClose()).dimensions(centerX - buttonWidth - 5, buttonY, buttonWidth, 20).build());
        
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.cancel"), (button) -> {
            assert this.client != null;
            this.client.setScreen(this.parent);
        }).dimensions(centerX + 5, buttonY, buttonWidth, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderPanoramaBackground(context, delta);
        context.fillGradient(0, 0, this.width, this.height, 0x66101010, 0x66202020);
        
        int centerX = this.width / 2;

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, centerX, 20, ACCENT_COLOR);

        context.drawCenteredTextWithShadow(this.textRenderer,
            Text.translatable("option.waxedicons.icon_style"), centerX, 80, SUBTITLE_COLOR);

        this.drawStyleLabels(context);
        
        super.render(context, mouseX, mouseY, delta);

        this.drawTooltips(context, mouseX, mouseY);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }
    
    private void drawStyleLabels(DrawContext context) {
        int labelY = this.defaultPreview.getY() + this.defaultPreview.getHeight() + 8;
        
        context.drawCenteredTextWithShadow(this.textRenderer, 
            Text.translatable("style.waxedicons.default"), 
            this.defaultPreview.getX() + this.defaultPreview.getWidth() / 2, labelY, TEXT_COLOR);
            
        context.drawCenteredTextWithShadow(this.textRenderer, 
            Text.translatable("style.waxedicons.alternative"), 
            this.alternativePreview.getX() + this.alternativePreview.getWidth() / 2, labelY, TEXT_COLOR);
            
        context.drawCenteredTextWithShadow(this.textRenderer, 
            Text.translatable("style.waxedicons.honeycomb"), 
            this.honeycombPreview.getX() + this.honeycombPreview.getWidth() / 2, labelY, TEXT_COLOR);
    }
    
    private void drawTooltips(DrawContext context, int mouseX, int mouseY) {
        if (this.defaultPreview.isMouseOver(mouseX, mouseY)) {
            context.drawTooltip(this.textRenderer, Text.translatable("style.waxedicons.default"), mouseX, mouseY);
        } else if (this.alternativePreview.isMouseOver(mouseX, mouseY)) {
            context.drawTooltip(this.textRenderer, Text.translatable("style.waxedicons.alternative"), mouseX, mouseY);
        } else if (this.honeycombPreview.isMouseOver(mouseX, mouseY)) {
            context.drawTooltip(this.textRenderer, Text.translatable("style.waxedicons.honeycomb"), mouseX, mouseY);
        }
    }

    private void selectStyle(String style) {
        this.config.iconStyle = style;
        this.defaultPreview.setSelected(style.equals("default"));
        this.alternativePreview.setSelected(style.equals("alternative"));
        this.honeycombPreview.setSelected(style.equals("honeycomb"));
    }
    
    private void updatePreviewOpacity() {
        this.defaultPreview.updateOpacity(config.iconOpacity / 100.0f);
        this.alternativePreview.updateOpacity(config.iconOpacity / 100.0f);
        this.honeycombPreview.updateOpacity(config.iconOpacity / 100.0f);
    }

    private void saveAndClose() {
        WaxedIconsConfigManager.saveConfig();
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
    
    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public class StylePreviewWidget extends ButtonWidget {
        private final Identifier iconTexture;
        private boolean selected;
        private float opacity;

        public StylePreviewWidget(int x, int y, int width, int height, Identifier iconTexture, String style, boolean selected) {
            super(x, y, width, height, Text.empty(), (button) -> WaxedIconsConfigScreen.this.selectStyle(style), DEFAULT_NARRATION_SUPPLIER);
            this.iconTexture = iconTexture;
            this.selected = selected;
            this.opacity = config.iconOpacity / 100.0f;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
        
        public void updateOpacity(float opacity) {
            this.opacity = opacity;
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            int backgroundColor = this.selected ? SELECTED_COLOR : PANEL_COLOR;
            if (this.isHovered()) {
                backgroundColor = this.selected ? SELECTED_COLOR : HOVER_COLOR;
            }
            
            context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, backgroundColor);

            int borderColor = this.selected ? ACCENT_COLOR : BORDER_COLOR;
            if (this.isHovered() && !this.selected) {
                borderColor = 0xFF777777;
            }
            context.drawBorder(this.getX(), this.getY(), this.width, this.height, borderColor);

            if (this.selected) {
                context.drawBorder(this.getX() - 1, this.getY() - 1, this.width + 2, this.height + 2, ACCENT_COLOR);
            }

            int slotSize = Math.min(32, this.width - 16);
            int slotX = this.getX() + (this.width - slotSize) / 2;
            int slotY = this.getY() + (this.height - slotSize) / 2;

            context.fill(slotX - 1, slotY - 1, slotX + slotSize + 1, slotY + slotSize + 1, 0xFF333333);
            context.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, 0xFF8B8B8B);

            if (slotSize >= 16) {
                context.getMatrices().push();
                float scale = slotSize / 16.0f;
                context.getMatrices().scale(scale, scale, 1.0f);
                context.drawItem(previewItem, 
                    (int)(slotX / scale), (int)(slotY / scale));
                context.getMatrices().pop();
            }

            if (this.iconTexture != null) {
                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 200);
                context.setShaderColor(1.0f, 1.0f, 1.0f, this.opacity);
                
                int iconSize = Math.max(8, slotSize / 3);
                int iconX = slotX + slotSize - iconSize;

                context.drawTexture(this.iconTexture, iconX, slotY, iconSize, iconSize,
                    0, 0, iconSize, iconSize, iconSize, iconSize);
                context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                context.getMatrices().pop();
            }
        }
    }
    
    public class ModernSliderWidget extends SliderWidget {
        public ModernSliderWidget(int x, int y, int width, int height, Text message, double value) {
            super(x, y, width, height, message, value);
        }

        @Override
        protected void updateMessage() {
            int opacity = (int) Math.round(this.value * 100);
            this.setMessage(Text.translatable("option.waxedicons.opacity", opacity + "%"));
        }

        @Override
        protected void applyValue() {
            config.iconOpacity = (int) Math.round(this.value * 100);
            updatePreviewOpacity();
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            boolean result = super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);

            this.updateMessage();
            this.applyValue();
            return result;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean result = super.mouseClicked(mouseX, mouseY, button);
            if (result) {
                this.updateMessage();
                this.applyValue();
            }
            return result;
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            int trackHeight = 4;
            int trackY = this.getY() + (this.height - trackHeight) / 2;
            context.fill(this.getX(), trackY, this.getX() + this.width, trackY + trackHeight, PANEL_COLOR);
            context.drawBorder(this.getX(), trackY, this.width, trackHeight, BORDER_COLOR);

            int fillWidth = (int) (this.value * this.width);
            if (fillWidth > 0) {
                context.fill(this.getX(), trackY, this.getX() + fillWidth, trackY + trackHeight, ACCENT_COLOR);
            }

            int handleWidth = 12;
            int handleHeight = this.height;
            int handleX = this.getX() + (int) (this.value * (this.width - handleWidth));
            
            int handleColor = this.isHovered() ? 0xFFFFFFFF : ACCENT_COLOR;
            context.fill(handleX, this.getY(), handleX + handleWidth, this.getY() + handleHeight, handleColor);
            context.drawBorder(handleX, this.getY(), handleWidth, handleHeight, BORDER_COLOR);

            context.drawCenteredTextWithShadow(WaxedIconsConfigScreen.this.textRenderer,
                this.getMessage(), this.getX() + this.width / 2, this.getY() - 12, TEXT_COLOR);
        }
    }
}