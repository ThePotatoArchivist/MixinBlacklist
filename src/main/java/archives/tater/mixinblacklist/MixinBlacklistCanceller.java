package archives.tater.mixinblacklist;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class MixinBlacklistCanceller implements MixinCanceller {

    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        var isClient = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
        for (var entry : MixinBlacklist.ENTRIES) {
            if (!isClient && entry.isClient) continue;
            if (entry.isTarget ? !targetClassNames.contains(entry.className) : !entry.className.equals(mixinClassName)) continue;
            entry.setApplied();
            return true;
        }
        return false;
    }
}
