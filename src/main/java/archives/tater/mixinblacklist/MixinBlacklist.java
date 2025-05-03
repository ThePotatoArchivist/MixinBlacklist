package archives.tater.mixinblacklist;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class MixinBlacklist implements ModInitializer {
	public static final String MOD_ID = "mixinblacklist";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Path CONFIG_PATH =
		FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");

	@Override
	public void onInitialize() {
		for (var entry : MixinBlacklistCanceller.ENTRIES) {
			if (entry.isClient || entry.isApplied()) continue;
			LOGGER.warn("Mixin cancel {} was not applied", entry);
		}
	}
}