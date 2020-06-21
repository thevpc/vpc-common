package net.vpc.common.jeep;

public interface JTypeOrVariable {
    String getName();
    boolean isVar();
    JTypeVariable toVar();
}
