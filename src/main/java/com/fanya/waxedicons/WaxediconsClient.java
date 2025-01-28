package com.fanya.waxedicons;

import com.fanya.waxedicons.util.WaxedBlocks;
import net.fabricmc.api.ClientModInitializer;

public class WaxediconsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        WaxedBlocks.init();
    }
}
