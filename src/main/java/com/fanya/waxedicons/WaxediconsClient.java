package com.fanya.waxedicons;

import com.fanya.waxedicons.util.WaxedBlocks;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaxediconsClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("waxedicons");
    @Override
    public void onInitializeClient() {
        WaxedBlocks.init();
    }
}
