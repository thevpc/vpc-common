package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.JDeclaration;
import net.vpc.common.jeep.JMethod;

import java.lang.reflect.Modifier;

public abstract class AbstractJMethod implements JMethod {
    @Override
    public String getName() {
        return getSignature().name();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(isPublic()){
            sb.append("public ");
        }
        if(isStatic()){
            sb.append("static ");
        }
        //type if not anonymous!
        if (getReturnType() != null) {
            sb.append(getReturnType().getName());
            sb.append(" ");
        }
        if(getDeclaringType()!=null) {
            sb.append(getDeclaringType().getName());
            sb.append(".");
        }
        sb.append(getSignature());
        return sb.toString();

    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    @Override
    public JDeclaration getDeclaration() {
        return getDeclaringType();
    }

    @Override
    public int hashCode() {
        return getSignature().hashCode();
    }
}
