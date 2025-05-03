package archives.tater.mixinblacklist;

public class Entry {
    public final String className;
    public final boolean isClient;
    public final boolean isTarget;
    private boolean applied = false;

    public Entry(String className, boolean target, boolean client) {
        this.className = className;
        this.isTarget = target;
        this.isClient = client;
    }

    public boolean isApplied() {
        return applied;
    }

    public void setApplied() {
        this.applied = true;
    }

    @Override
    public String toString() {
        return "[" + (isTarget ? "Target" : "Mixin") + ": " + className + (isClient ? " (client only)" : "") + "]";
    }
}
