package net.vpc.common.jeep.impl.functions;

import net.vpc.common.jeep.JDeclaration;
import net.vpc.common.jeep.JInvokable;
import net.vpc.common.jeep.impl.types.DefaultJAnnotationInstanceList;
import net.vpc.common.jeep.impl.types.DefaultJModifierList;
import net.vpc.common.jeep.impl.types.JAnnotationInstanceList;
import net.vpc.common.jeep.impl.types.JModifierList;

public abstract class AbstractJInvokable implements JInvokable {
    private JModifierList modifiers = new DefaultJModifierList();
    private JAnnotationInstanceList annotations = new DefaultJAnnotationInstanceList();

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public JDeclaration getDeclaration() {
        return null;
    }

    @Override
    public JModifierList getModifiers() {
        return modifiers;
    }

    @Override
    public JAnnotationInstanceList getAnnotations() {
        return annotations;
    }

    public String getName() {
        return getSignature().name();
    }
}
