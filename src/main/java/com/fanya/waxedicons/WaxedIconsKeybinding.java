package com.fanya.waxedicons;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class WaxedIconsKeybinding {
    public static final KeyBinding.Category WAXED_CATEGORY = KeyBinding.Category.create(Identifier.of("waxedicons", "main"));
    public static final KeyBinding CONFIG_KEY = new KeyBinding(
            "key.waxedicons.config",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_G,
            WAXED_CATEGORY
    );
}
