package archives.tater.mixinblacklist;

import net.fabricmc.api.ClientModInitializer;

import static archives.tater.mixinblacklist.MixinBlacklist.LOGGER;

public class MixinBlacklistClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (var entry : MixinBlacklistCanceller.ENTRIES) {
            if (!entry.isClient || entry.isApplied()) continue;
            LOGGER.warn("Mixin cancel {} was not applied", entry);
        }
    }
}
