package net.vpc.common.jeep.core.types;

import net.vpc.common.jeep.JField;
import net.vpc.common.jeep.JType;

public abstract class AbstractJField implements JField {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isPublic()){
            sb.append("public ");
        }
        if(isStatic()){
            sb.append("static ");
        }
        JType type = type();
        sb.append(type==null?"?":(type.getName()));
        sb.append(" ");
        sb.append(declaringType().getName());
        sb.append(".");
        sb.append(name());
        return sb.toString();
    }
}
