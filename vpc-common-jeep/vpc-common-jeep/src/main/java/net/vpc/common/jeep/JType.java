package net.vpc.common.jeep;

import net.vpc.common.jeep.core.JStaticObject;
import net.vpc.common.jeep.impl.functions.JSignature;

public interface JType extends JDeclaration, JTypeOrVariable {
    JDeclaration declaration();


    JTypeVariable[] typeParameters();

    JType rawType();

//    JAnnotation annotations();

    JStaticObject staticObject();

    String name();

    JTypeName typeName();

    JTypes types();

    String dname();

    String simpleName();

    Object cast(Object o);

    JType boxed();

    int modifiers();
    boolean isPublic();

    boolean isStatic();

    boolean isNullable();

    boolean isPrimitive();

    boolean isAssignableFrom(JType other);

    boolean isInstance(Object instance);

    //<editor-fold desc="Array">

    boolean isArray();

    default JType toArray() {
        return toArray(1);
    }

    JType toArray(int count);

    //</editor-fold>

    JType getSuperType();

    JType firstCommonSuperType(JType other);

    JType toPrimitive();


    JField matchedField(String fieldName);


    //<editor-fold desc="Match">
//    @Deprecated
//    JMethod findMethodMatchOrNull(JSignature signature, JContext context);
//
//    @Deprecated
//    JConstructor findConstructorMatch(JSignature signature, JContext context);
//
//    @Deprecated
//    JConstructor findConstructorMatchOrNull(JSignature signature, JContext context);
//
//    @Deprecated
//    JMethod findMethodMatch(JSignature signature, JContext context);
    //</editor-fold>


    //<editor-fold desc="Declarations">

    JMethod declaredMethod(String sig);

    JMethod declaredMethod(JSignature sig);

    JMethod declaredMethodOrNull(JSignature sig);

    //    JMethod declaredMethodOrNull(String methodName, JType... parameterTypes);
    JConstructor declaredConstructorOrNull(String sig);

    JConstructor declaredConstructor(String sig);

    JType[] interfaces();

    JConstructor declaredConstructor(JSignature sig);

    JConstructor declaredConstructor(JType... parameterTypes);

    JConstructor[] publicConstructors();

    JConstructor defaultConstructorOrNull();

    JConstructor defaultConstructor();

    JConstructor[] declaredConstructors();

    JField declaredField(String fieldName);

    JField[] declaredFields();

    JField declaredFieldOrNull(String fieldName);

    JField publicField(String name);


    JMethod[] publicMethods();

    JMethod[] declaredMethods();


    JMethod[] declaredMethods(String name);


    JField[] declaredFieldsWithParents();

    JType[] declaredInnerTypes();

    JType declaredInnerType(String name);

    JType declaredInnerTypeOrNull(String name);

    JMethod declaredMethodOrNull(String sig);

    /**
     * all methods of the given names and that can be called with {@code callArgumentsCount}
     * arguments. This includes methods that has exactly {@code callArgumentsCount}
     * and methods that are vararg and who's arguments count is less or equals than
     * {@code callArgumentsCount}
     *
     * @param names              method names
     * @param callArgumentsCount arg count, when -1, all methods are returned!
     * @param includeParents     include super classes/interfaces
     * @return array of all applicable methods
     */
    JMethod[] declaredMethods(String[] names, int callArgumentsCount, boolean includeParents);

    JMethod[] declaredMethods(boolean includeParents);

    JType[] parents();

    JConstructor declaredConstructorOrNull(JSignature sig);
    //</editor-fold>

    Object defaultValue();

    JType declaringType();

    String packageName();

    /**
     * exports are a list of "import" statement that should be processed each time
     * one imports this type with "**" end operator.
     * For instance
     * This code :
     * <pre>
     * class A{
     *     import java.util.*;
     *     import B.**;
     * }
     * class B{
     *     export java.reflect.*;
     * }
     * </pre>
     * is equivalent to
     * <pre>
     * class A{
     *     import java.util.*;
     *     import java.reflect.*;
     * }
     * class B{
     *     export java.reflect.*;
     * }
     * </pre>
     *
     * @return
     */
    String[] getExports();

    boolean isRawType();

    JType replaceParameter(String name, JType param);

    boolean isInterface();
}
