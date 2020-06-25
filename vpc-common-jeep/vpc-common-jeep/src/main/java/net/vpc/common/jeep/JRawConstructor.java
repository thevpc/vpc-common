package net.vpc.common.jeep;

import net.vpc.common.jeep.JConstructor;
import net.vpc.common.jeep.impl.functions.JSignature;

public interface JRawConstructor extends JConstructor {
    JSignature getGenericSignature();
}
