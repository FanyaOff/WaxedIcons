package com.fanya.waxedicons;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class WaxedIconsKeybinding {
    public static final KeyMapping.Category CONFIG_CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("waxedicons", "main")
    );

    public static final KeyMapping CONFIG_KEY = new KeyMapping(
            "key.waxedicons.config",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_G,
            CONFIG_CATEGORY
    );
}
