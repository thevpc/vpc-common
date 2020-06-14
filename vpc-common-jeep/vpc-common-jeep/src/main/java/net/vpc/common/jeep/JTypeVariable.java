package net.vpc.common.jeep;

public interface JTypeVariable extends JType {

    JType[] upperBounds();

    JType[] lowerBounds();

    boolean isWildcard();

    String getVName();
}
