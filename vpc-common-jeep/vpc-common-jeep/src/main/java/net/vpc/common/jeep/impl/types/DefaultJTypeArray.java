package net.vpc.common.jeep.impl.types;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.types.AbstractJField;
import net.vpc.common.jeep.core.JStaticObject;
import net.vpc.common.jeep.impl.JTypesSPI;
import net.vpc.common.jeep.impl.types.host.AbstractJType;
import net.vpc.common.jeep.impl.types.host.HostJArray;
import net.vpc.common.jeep.impl.types.host.HostJRawType;
import net.vpc.common.jeep.util.JTypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

public class DefaultJTypeArray extends AbstractJType implements JTypeArray{
    private JType root;
    private int dim;
    private String name;
    private String sname;
    private ArrFieldLength lengthField;
    private JType componentType;

    public DefaultJTypeArray(JType root, int dim) {
        super(root.types());
        this.root = root;
        this.dim = dim;
        if (root.isArray() || dim==0) {
            throw new IllegalStateException("Invalid Array with dimension ==0");
        }
        StringBuilder fb = new StringBuilder(root.name().length() + 2 * dim);
        StringBuilder sb = new StringBuilder(root.simpleName().length() + 2 * dim);
        fb.append(root.name());
        sb.append(root.simpleName());
        for (int i = 0; i < dim; i++) {
            fb.append("[]");
            sb.append("[]");
        }
        this.name = fb.toString();
        this.sname = sb.toString();
        this.lengthField = new ArrFieldLength(this,types());
        this.componentType= dim==1?root:
                JTypesSPI.getRegisteredOrRegister(types2().createArrayType0(root,dim-1),
                        types()
        );
    }


    @Override
    public JDeclaration declaration() {
        return null;
    }

//    @Override
//    public JType[] actualTypeArguments() {
//        return new JType[0];
//    }

    @Override
    public JTypeVariable[] typeParameters() {
        return new JTypeVariable[0];
    }

    @Override
    public JType rawType() {
        return root.isRawType() ? this : root.rawType().toArray(arrayDimension());
    }

    @Override
    public JStaticObject staticObject() {
        return null;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String simpleName() {
        return sname;
    }

    @Override
    public boolean isNullable() {
        return true;
    }

    @Override
    public JType toArray(int count) {
        return JTypesSPI.getRegisteredOrRegister(
                types2().createArrayType0(rootComponentType(), arrayDimension() + count),
                types()
        );
    }

    @Override
    public JType getSuperType() {
        return JTypeUtils.forObject(types());
    }

//    @Override
//    public JType parametrize(JType... parameters) {
//        return ((JRawType)rootComponentType()).parametrize(parameters).toArray(arrayDimension());
//    }

    public JType componentType() {
        return componentType;
    }

    @Override
    public JType[] interfaces() {
        return new JType[0];
    }

    @Override
    public JConstructor defaultConstructorOrNull() {
        return null;
    }

    @Override
    public JConstructor[] declaredConstructors() {
        return new JConstructor[0];
    }

    @Override
    public JField[] declaredFields() {
        return new JField[]{lengthField};
    }

    @Override
    public JMethod[] declaredMethods() {
        return new JMethod[0];
    }

    @Override
    public JType[] declaredInnerTypes() {
        return new JType[0];
    }

    @Override
    public Object defaultValue() {
        return null;
    }

    @Override
    public JType declaringType() {
        return null;
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    @Override
    public int modifiers() {
        return Modifier.PUBLIC;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public String packageName() {
        return null;
    }

    @Override
    public String[] getExports() {
        return new String[0];
    }

    public int arrayDimension() {
        return dim;
    }

    @Override
    public JType rootComponentType() {
        return root;
    }

    @Override
    public Object newArray(int... len) {
        if(rootComponentType() instanceof HostJRawType){
            HostJRawType hjt = (HostJRawType) rootComponentType().rawType();
            Type ht= hjt.hostType();
            return Array.newInstance((Class<?>) ht, len);
        }else{
            int len0 = len[0];
            JType jType = componentType();
            DefaultJArray aa = new DefaultJArray(new Object[len0], jType);
            if (len.length > 1) {
                JTypeArray jTypea=(JTypeArray) jType;
                int[] len2 = Arrays.copyOfRange(len, 0, len.length - 1);
                for (int i = 0; i < len0; i++) {
                    aa.set(i, jTypea.newArray(len2));
                }
            }
            return aa;
        }
    }

    @Override
    public JArray asArray(Object o) {
        if(o instanceof JArray){
            return (JArray) o;
        }
        return new HostJArray(o, componentType());
    }
    @Override
    public JType replaceParameter(String name, JType param) {
        JType r = rootComponentType().replaceParameter(name, param);
        if(r.name().equals(rootComponentType().name())){
            return this;
        }
        return types2().createArrayType0(r,arrayDimension());
    }

    @Override
    public boolean isArray() {
        return true;
    }
    


    private static class ArrFieldLength extends AbstractJField {
        private JType arrayType;
        private JTypes types;
        private int modifiers = Modifier.PUBLIC | Modifier.FINAL;

        public ArrFieldLength(JType arrayType, JTypes types) {
            this.arrayType = arrayType;
            this.types = types;
        }

        @Override
        public String name() {
            return "length";
        }

        @Override
        public JType type() {
            return JTypeUtils.forInt(types);
        }

        @Override
        public Object get(Object instance) {
            if (instance instanceof JArray) {
                return ((JArray) instance).length();
            }
            return Array.getLength(instance);
        }

        @Override
        public void set(Object instance, Object value) {
            throw new IllegalStateException("Unmodifiable field");
        }

        @Override
        public boolean isPublic() {
            return Modifier.isPublic(modifiers);
        }

        @Override
        public boolean isStatic() {
            return Modifier.isStatic(modifiers);
        }

        @Override
        public boolean isFinal() {
            return Modifier.isFinal(modifiers);
        }

        @Override
        public int modifiers() {
            return modifiers;
        }

        @Override
        public JType declaringType() {
            return arrayType;
        }
    }

    @Override
    public boolean isInterface() {
        return false;
    }
}
