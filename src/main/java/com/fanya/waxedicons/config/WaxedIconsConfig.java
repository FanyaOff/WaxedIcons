package com.fanya.waxedicons.config;

import com.fanya.waxedicons.WaxediconsClient;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.Identifier;

@Config(name = WaxediconsClient.MOD_ID)
public class WaxedIconsConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip()
    public String iconStyle = "default";

    public static final String[] AVAILABLE_STYLES = {
            "default",
            "alternative"
    };

    public Identifier getIconTexture() {
        return Identifier.of("waxedicons", "textures/gui/waxed_icon_" + iconStyle + ".png");
    }

    public Identifier getValidIconTexture() {
        return getIconTexture();
    }
}
