package net.thevpc.common.util;

/**
 * Created by vpc on 3/24/17.
 */
public final class MemoryInfo {
    private final long maxMemory;
    private final long totalMemory;
    private final long freeMemory;

    public MemoryInfo() {
        Runtime rt = Runtime.getRuntime();
        maxMemory = rt.maxMemory();
        totalMemory = rt.totalMemory();
        freeMemory = rt.freeMemory();
    }

    public MemoryUsage diff(MemoryInfo other) {
        if (other == null) {
            return new MemoryUsage(
                    maxMemory,
                    totalMemory,
                    freeMemory,
                    inUseMemory()
            );
        }
        return new MemoryUsage(
                maxMemory,
                totalMemory,
                freeMemory - other.freeMemory,
                inUseMemory() - other.inUseMemory()
        );
    }

    public long maxFreeMemory() {
        return maxMemory - (totalMemory - freeMemory);
    }

    public long inUseMemory() {
        return totalMemory - freeMemory;
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


    public String toString() {
        BytesSizeFormat f = BytesSizeFormat.INSTANCE;
        return
                "free : " + f.format(freeMemory()) + " ; "
                        + "total : " + f.format(totalMemory()) + " ; "
                        + "max : " + f.format(maxMemory()) + " ; "
                        + "inUse : " + f.format(inUseMemory()) + " ; "
                        + "max Free : " + f.format(maxFreeMemory())
                ;
    }
}
