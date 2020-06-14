package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.JDeclaration;
import net.vpc.common.jeep.JMethod;

import java.lang.reflect.Modifier;

public abstract class AbstractJMethod implements JMethod {
    @Override
    public String name() {
        return signature().name();
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
        if (returnType() != null) {
            sb.append(returnType().name());
            sb.append(" ");
        }
        sb.append(declaringType().name());
        sb.append(".");
        sb.append(signature());
        return sb.toString();

    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(modifiers());
    }

    @Override
    public boolean isPublic() {
        return Modifier.isPublic(modifiers());
    }

    @Override
    public JDeclaration declaration() {
        return declaringType();
    }

    @Override
    public int hashCode() {
        return signature().hashCode();
    }
}
