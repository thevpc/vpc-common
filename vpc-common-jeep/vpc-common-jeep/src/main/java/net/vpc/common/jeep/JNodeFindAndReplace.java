package net.vpc.common.jeep;

import net.vpc.common.jeep.JNode;

public interface JNodeFindAndReplace {
    boolean accept(JNode node);
    JNode replace(JNode node);
    default boolean isReplaceFirst(){
        return true;
    }
}
