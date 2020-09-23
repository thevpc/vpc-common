package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.types.DefaultJAnnotationObject;

import java.util.*;

public class DefaultJEnumType extends DefaultJType implements JEnumType {
    public DefaultJEnumType(String name, JTypeKind kind, JTypes types) {
        super(name, kind,types);
    }
}
