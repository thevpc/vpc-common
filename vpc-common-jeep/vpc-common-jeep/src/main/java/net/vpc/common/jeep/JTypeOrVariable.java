package net.vpc.common.jeep;

public interface JTypeOrVariable {
    String name();
    boolean isVar();
    JTypeVariable toVar();
}
