package net.vpc.common.xfile;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class XFileSystems {
    private static List<XFileSystem> systems = new ArrayList<>();
    private static XFileSystem[] _systems = new XFileSystem[0];

    static {
        systems.add(new JavaFileXFileSystem());
        systems.add(new JavaURLXFileSystem());
        try {
            ServiceLoader<XFileSystem> ll = ServiceLoader.load(XFileSystem.class);
            for (XFileSystem xFileSystem : ll) {
                systems.add(xFileSystem);
            }
            _systems = systems.toArray(new XFileSystem[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static XFile get(String path) {
        if (path == null) {
            return null;
        }
        XFileSystem best = null;
        int bestAccepted = -1;
        for (XFileSystem system : _systems) {
            int accepted = system.accept(path);
            if (accepted >= 0) {
                if (accepted > bestAccepted) {
                    best = system;
                    bestAccepted = accepted;
                }
            }
        }
        if (best == null) {
            throw new IllegalArgumentException("Path not supported " + path);
        }
        return best.get(path);
    }
}
