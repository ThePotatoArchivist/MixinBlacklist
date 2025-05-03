package archives.tater.mixinblacklist;

public record Entry(
        String className,
        boolean isTarget,
        boolean isClient
) {
    @Override
    public String toString() {
        return "[" + (isTarget ? "Target" : "Mixin") + ": " + className + (isClient ? " (client only)" : "") + "]";
    }
}
