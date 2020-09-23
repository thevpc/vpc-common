package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.types.JAnnotationInstanceList;
import net.vpc.common.jeep.impl.types.JModifierList;

public interface JDeclaration {
    //parent declaration
    JDeclaration getDeclaration();

    JModifierList getModifiers();

    JAnnotationInstanceList getAnnotations();
    JTypes getTypes();

    String getSourceName();
}
