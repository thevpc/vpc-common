package net.vpc.common.jeep.core;

import net.vpc.common.jeep.JTokenIdFilter;

public abstract class AbstractJTokenIdFilter implements JTokenIdFilter {
    public boolean isIdentifierStart(char cc) {
        return Character.isJavaIdentifierPart(cc);
    }

    public boolean isIdentifierPart(char cc) {
        return Character.isJavaIdentifierPart(cc);
    }
}
