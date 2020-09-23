package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.JClassType;
import net.vpc.common.jeep.JTypes;

public class HostJClassType extends HostJRawType implements JClassType {
    public HostJClassType(Class hostType, JTypes types) {
        super(hostType, types);
    }
}
