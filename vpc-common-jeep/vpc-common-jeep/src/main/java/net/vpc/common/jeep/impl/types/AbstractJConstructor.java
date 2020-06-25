package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.JConstructor;
import net.vpc.common.jeep.impl.JArgumentTypes;

public abstract class AbstractJConstructor implements JConstructor {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isPublic()){
            sb.append("public ");
        }
        sb.append(getDeclaringType().getName());
        sb.append(new JArgumentTypes(getSignature().argTypes(), getSignature().isVarArgs()));
        return sb.toString();

    }
}
