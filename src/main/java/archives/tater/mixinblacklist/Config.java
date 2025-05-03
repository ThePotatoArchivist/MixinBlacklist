package archives.tater.mixinblacklist;

import java.util.List;

@SuppressWarnings("unused")
public class Config {
    public MixinList client = new MixinList();
    public MixinList common = new MixinList();

    public static class MixinList {
        public List<String> mixinClassNames = List.of();
        public List<String> targetClassNames = List.of();
    }
}
