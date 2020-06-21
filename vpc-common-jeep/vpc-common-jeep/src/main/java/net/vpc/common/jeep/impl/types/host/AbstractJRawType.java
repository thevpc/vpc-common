package net.vpc.common.jeep.impl.types.host;

import net.vpc.common.jeep.JType;
import net.vpc.common.jeep.JTypeVariable;
import net.vpc.common.jeep.JTypes;
import net.vpc.common.jeep.impl.JTypesSPI;
import net.vpc.common.jeep.JRawType;

public abstract class AbstractJRawType extends AbstractJType implements JRawType {
    public AbstractJRawType(JTypes types) {
        super(types);
    }

    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(this,count)
                ,types());
    }

    @Override
    public JType parametrize(JType... parameters) {
        if (parameters.length == 0) {
            throw new IllegalArgumentException("Invalid zero parameters count");
        }
        JTypeVariable[] vars = getTypeParameters();
        if (vars.length != parameters.length) {
            throw new IllegalArgumentException("Invalid parameters count. expected "+vars.length+" but got "+parameters.length);
        }
        return JTypesSPI.getRegisteredOrRegister(
                types2().createParameterizedType0(this, parameters,
                getDeclaringType()),types());
    }
}
