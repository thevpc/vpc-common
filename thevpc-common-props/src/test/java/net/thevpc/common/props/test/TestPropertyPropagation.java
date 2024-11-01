package net.thevpc.common.props.test;

import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.PropertyBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPropertyPropagation {
    @Test
    void test_propagation() {
        TestLogHelper out = new TestLogHelper();
        A a = new A("a");
        a.events().addPropagated((e) -> {
            if (e.immediate()) {
                out.println("<IMMEDIATE> " + e);
            } else {
                out.println("<DEEP     > " + e);
            }
        });
        a.b.setUsing(() -> {
            B b = new B("b");
            b.c.set(new C("c"));
            return b;
        });
        a.b.get().c.get().string.set("new value");

        a.b.setUsing(() -> {
            B b = new B("newB");
            b.c.set(new C("newC"));
            return b;
        });
        a.b.getRequired().c.getRequired().string.set("new value 2");
//        out.assertEquals(
//                "<DEEP     > UPDATE{ propagated, path='/a/b', property=b, oldValue=null, newValue=B{c=C{string=null}}}",
//                "<DEEP     > UPDATE{ propagated, path='/a/b/c/string', property=string, oldValue=null, newValue=new value}",
//                "<DEEP     > UPDATE{ propagated, path='/a/b', property=b, oldValue=B{c=C{string=new value}}, newValue=B{c=C{string=null}}}",
//                "<DEEP     > UPDATE{ propagated, path='/a/newB/newC/string', property=string, oldValue=null, newValue=new value 2}"
//        );
    }

    @Test
    void test_propagation_bind() {
        TestLogHelper out = new TestLogHelper();
        A a = new A("a");
        a.events().addPropagated((e) -> {
            if (e.immediate()) {
                out.println("<IMMEDIATE> " + e);
            } else {
                out.println("<DEEP     > " + e);
            }
        });
        //a.s.bind(a, Path.of("/a"), Path.of("/b/c/string"));
        a.b.setUsing(() -> {
            B b = new B("b");
            b.c.set(new C("c"));
            return b;
        });
        a.b.get().c.set(null);
        Property c2 = a.getChildProperty(Path.of("/b/c/string"));
        System.out.println(c2);
        Assertions.assertNull(c2);

        a.b.get().c.set(new C("c"));
        a.b.get().c.get().string.set("new value");
        Property c = a.getChildProperty(Path.of("/b/c/string"));
        System.out.println(c);
        Assertions.assertNotNull(c);

        a.s.set("InA");
//        Assertions.assertEquals("InA",((ObservableValue)a.getChildProperty(Path.of("/b/c/string"))).get());
        a.b.get().c.get().string.set("InString");
//        Assertions.assertEquals("InString",a.s.get());
    }

    class A extends PropertyBase {
        WritableValue<B> b = Props.of("b").valueOf(B.class);
        WritableString s = Props.of("s").stringOf(null);

        public A(String name) {
            super(name);
            propagateEvents(s,b);
        }

        @Override
        public String toString() {
            return "A{" +
                    "b=" + b +
                    '}';
        }
    }

    class B extends PropertyBase {
        WritableValue<C> c = Props.of("c").valueOf(C.class);

        public B(String name) {
            super(name);
            propagateEvents(c);
        }

        @Override
        public String toString() {
            return "B{" +
                    "c=" + c +
                    '}';
        }
    }

    class C extends PropertyBase {
        WritableString string = Props.of("string").stringOf(null);

        public C(String name) {
            super(name);
            propagateEvents(string);
        }

        @Override
        public String toString() {
            return "C{" +
                    "string=" + string +
                    '}';
        }
    }
}
