package net.vpc.common.jeep.core.compiler;

import net.vpc.common.jeep.JCompilerLog;
import net.vpc.common.jeep.JSource;

import java.util.Iterator;

public interface JSourceRoot extends Iterable<JSource>{
    String getId();
    Iterable<JSource> iterate(JCompilerLog log);

    @Override
    default Iterator<JSource> iterator() {
        return iterate(null).iterator();
    }
}
