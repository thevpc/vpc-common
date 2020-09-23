package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.JClassType;
import net.vpc.common.jeep.JEnumType;
import net.vpc.common.jeep.JTypes;

public class HostJEnumType extends HostJRawType implements JEnumType {
    public HostJEnumType(Class hostType, JTypes types) {
        super(hostType, types);
    }
}
