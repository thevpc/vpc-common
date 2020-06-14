package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.types.DefaultJTypes;
import net.vpc.common.jeep.util.JTypeUtils;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class TestGenerics {
    @Test
    public void test1_Lists() {
        JTypes types = new DefaultJTypes();
        JRawType tList = (JRawType) types.forName(List.class.getName());
        System.out.println("===============================");
        System.out.println(tList);
        for (JMethod jMethod : Arrays.stream(tList.declaredMethods())
                .filter(x -> x.name().equals("add"))
                .toArray(JMethod[]::new)) {
            System.out.println("\t" + jMethod);
        }

        System.out.println("===============================");
        JType tListOfStrings = tList.parametrize(types.forName(String.class.getName()));
        for (JType anInterface : tListOfStrings.interfaces()) {
            System.out.println("#" + anInterface.name());
        }
        System.out.println(tListOfStrings);
        for (JMethod jMethod : Arrays.stream(tListOfStrings.declaredMethods())
                .filter(x -> x.name().equals("add"))
                .toArray(JMethod[]::new)) {
            System.out.println("\t" + jMethod);
        }
    }

    @Test
    public void test2_parents() {
        JTypes types = new DefaultJTypes();
        JType I2ofString = ((JRawType) types.forName(I2.class.getName())).parametrize(types.forName(String.class.getName()));
        System.out.println(I2ofString.name());
        System.out.println(Arrays.asList(I2ofString.interfaces()));
    }

    @Test
    public void test3_fields_method_constructors() {
        JTypes types = new DefaultJTypes();
        JRawType _A2 = (JRawType) types.forName(A2.class.getName());
        JType _AofString = _A2.parametrize(types.forName(String.class.getName()), JTypeUtils.forInt(types));
        System.out.println("===============================");
        System.out.println(((JRawType) _A2).gname());
        for (JField i : _A2.declaredFields()) {
            System.out.println("\t" + i);
        }
        for (JConstructor i : _A2.declaredConstructors()) {
            System.out.println("\t" + i);
        }
        for (JMethod i : _A2.declaredMethods()) {
            System.out.println("\t" + i);
        }

        System.out.println("===============================");
        System.out.println(_AofString.name());
        for (JField i : _AofString.declaredFields()) {
            System.out.println("\t" + i);
        }
        for (JConstructor i : _AofString.declaredConstructors()) {
            System.out.println("\t" + i);
        }
        for (JMethod i : _AofString.declaredMethods()) {
            System.out.println("\t" + i);
        }
        JMethod method2 = (_AofString.declaredMethods("method2")[0]).
                parametrize(types.forName(Long.class.getName()));
        System.out.println("\t" + method2);
//        System.out.println("===============================");
//        JType tListOfStrings = _A.parametrize(types.forName(String.class));
//        System.out.println(tListOfStrings);
//        for (JMethod jMethod : Arrays.stream(tListOfStrings.declaredMethods())
//                .filter(x->x.name().equals("add"))
//                .toArray(JMethod[]::new)) {
//            System.out.println("\t"+jMethod);
//        }
    }

    @Test
    public void test3_subtypes() {

        JTypes types = new DefaultJTypes();
        System.out.println("===============================");
        JRawType _A = (JRawType) types.forName(A.class.getName());
        JType _AOfStrings = _A.parametrize(types.forName(String.class.getName()), JTypeUtils.forInt(types));
        System.out.println(_AOfStrings);
        System.out.println(_A.declaredInnerType("B"));
        System.out.println(_AOfStrings.declaredInnerType("C"));
        System.out.println(_AOfStrings.declaredInnerType("B"));
        System.out.println();
//        A<String, Integer> stringIntegerA = new A<>();
//        A<String, Integer>.D dd = stringIntegerA.new D();
//        System.out.println(dd.getClass().getGenericSuperclass().getTypeName());

//        for (JMethod jMethod : Arrays.stream(tListOfStrings.declaredMethods())
//                .filter(x -> x.name().equals("add"))
//                .toArray(JMethod[]::new)) {
//            System.out.println("\t" + jMethod);
//        }
    }

    public interface I1<T1> {

    }

    public interface I2<T2> extends I1<T2> {

    }

    public static class A2<T1 extends Serializable, T2> {
        public T1 field;

        public A2(T2 t2) {
        }

        public T1 method(T2 t2) {
            return null;
        }

        public <X extends T1> X method2(T2 t2) {
            return null;
        }
    }

    public static class A<T1, T2> {
        public abstract class B<T1> {
            public T1 method(T2 t2) {
                return null;
            }
        }

        public class C<T3> {
            public T3 method(T2 t1, T2 t2) {
                return null;
            }
        }
        public class D extends C<String>{

        }
    }
}
