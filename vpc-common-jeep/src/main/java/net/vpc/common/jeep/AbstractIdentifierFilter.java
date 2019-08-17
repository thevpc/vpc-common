package net.vpc.common.jeep;

public abstract class AbstractIdentifierFilter implements IdentifierFilter{
    public boolean isIdentifierStart(char cc) {
        return Character.isJavaIdentifierPart(cc);
    }

    public boolean isIdentifierPart(char cc) {
        return Character.isJavaIdentifierPart(cc);
    }
}
