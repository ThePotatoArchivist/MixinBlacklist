package archives.tater.mixinblacklist;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import com.google.common.collect.Streams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static archives.tater.mixinblacklist.MixinBlacklist.LOGGER;

public class MixinBlacklistCanceller implements MixinCanceller {
    public static final Gson BUILDER = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create();

    public static final List<Entry> ENTRIES = new ArrayList<>();

    static {
        LOGGER.info("Reading config");

        Config config = null;

        try {
            config = BUILDER.fromJson(Files.newBufferedReader(MixinBlacklist.CONFIG_PATH), Config.class);
        } catch (NoSuchFileException e) {
            LOGGER.info("Creating empty config");

            try {
                Files.writeString(MixinBlacklist.CONFIG_PATH, BUILDER.toJson(new Config()));
            } catch (IOException e2) {
                LOGGER.error("Could not write config", e2);
            }
        } catch (IOException e) {
            LOGGER.error("Could not read config", e);
        }

        if (config != null) {
            Streams.concat(
                    config.common.mixinClassNames.stream().map(it -> new Entry(it, false, false)),
                    config.common.targetClassNames.stream().map(it -> new Entry(it, true, false)),
                    config.client.mixinClassNames.stream().map(it -> new Entry(it, false, true)),
                    config.client.targetClassNames.stream().map(it -> new Entry(it, true, true))
            ).collect(Collectors.toCollection(() -> ENTRIES));
        }
    }

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        var isClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
        for (var entry : ENTRIES) {
            if (!isClient && entry.isClient) continue;
            if (entry.isTarget ? !targetClassNames.contains(entry.className) : !entry.className.equals(mixinClassName)) continue;
            LOGGER.info("Cancelling mixin {} on classes {}", mixinClassName, targetClassNames);
            entry.setApplied();
            return true;
        }
        return false;
    }
}
