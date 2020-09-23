package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.JDeclaration;
import net.vpc.common.jeep.JMethod;
import net.vpc.common.jeep.impl.JTypesSPI;

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
        return ((JTypesSPI)getTypes()).isAbstractMethod(this);
    }

    @Override
    public boolean isStatic() {
        return ((JTypesSPI)getTypes()).isStaticMethod(this);
    }

    @Override
    public boolean isPublic() {
        return ((JTypesSPI)getTypes()).isPublicMethod(this);
    }

    public boolean isSynthetic() {
        return ((JTypesSPI)getTypes()).isSyntheticMethod(this);
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
