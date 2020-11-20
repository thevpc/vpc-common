package net.thevpc.common.util;

/**
 * Created by vpc on 3/24/17.
 */
public final class MemoryUsage {
    private final long maxMemory;
    private final long totalMemory;
    private final long freeMemory;
    private final long inUseMemory;

    public MemoryUsage(long maxMemory, long totalMemory, long freeMemory, long inUseMemory) {
        this.maxMemory = maxMemory;
        this.totalMemory = totalMemory;
        this.freeMemory = freeMemory;
        this.inUseMemory = inUseMemory;
    }

    public long maxMemory() {
        return maxMemory;
    }

    public long totalMemory() {
        return totalMemory;
    }

    public long freeMemory() {
        return freeMemory;
    }

    public long inUseMemory() {
        return inUseMemory;
    }

    public String toString() {
        BytesSizeFormat p = BytesSizeFormat.INSTANCE;
        return
                "free : " + p.format(freeMemory()) + " ; "
                        + "total : " + p.format(totalMemory()) + " ; "
                        + "max : " + p.format(maxMemory()) + " ; "
                        + "inUse : " + p.format(inUseMemory())
                ;
    }
}
