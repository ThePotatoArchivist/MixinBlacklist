package archives.tater.mixinblacklist;

import com.google.common.collect.Streams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MixinBlacklist implements ModInitializer {
    public static final String MOD_ID = "mixinblacklist";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");

    public static final Gson BUILDER = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    public static final List<Entry> ENTRIES = new ArrayList<>();

    static {
        LOGGER.debug("Reading config");

        Config config = null;

        try {
            config = BUILDER.fromJson(Files.newBufferedReader(CONFIG_PATH), Config.class);
        } catch (NoSuchFileException e) {
            LOGGER.info("Creating empty config");

            try {
                Files.writeString(CONFIG_PATH, BUILDER.toJson(new Config()));
            } catch (IOException e2) {
                LOGGER.error("Could not write config", e2);
            }
        } catch (IOException e) {
            LOGGER.error("Could not read config", e);
        }

        if (config != null)
            Streams.concat(
                    config.common.mixinClassNames.stream().map(it -> new Entry(it, false, false)),
                    config.common.targetClassNames.stream().map(it -> new Entry(it, true, false)),
                    config.client.mixinClassNames.stream().map(it -> new Entry(it, false, true)),
                    config.client.targetClassNames.stream().map(it -> new Entry(it, true, true))
            ).collect(Collectors.toCollection(() -> ENTRIES));
    }

    @Override
    public void onInitialize() {
        for (var entry : ENTRIES) {
            if (entry.isClient || entry.isApplied()) continue;
            LOGGER.warn("Mixin cancel {} was not applied", entry);
        }
    }
}