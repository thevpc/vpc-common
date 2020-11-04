package net.thevpc.common.diff.jar;

import java.io.File;

public final class Diff {
    public static DiffBuilder of(File source,File target){
        return new DiffBuilder(source,target);
    }
}
