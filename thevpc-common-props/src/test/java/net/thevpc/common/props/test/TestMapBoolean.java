package net.thevpc.common.props.test;

import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.DefaultPropertyListeners;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMapBoolean {
    @Test
    public void test_mapBooleanValue() {
        TestLogHelper out=new TestLogHelper();
        int[] stringFired = new int[1];
        int[] booleanFired = new int[1];
        WritableString s = Props.of("test").stringOf(null);
        ObservableBoolean b = s.mapBooleanValue(x -> x != null && x.equals("Hello"));
        s.onChange((c) -> {
            stringFired[0]++;
            out.println("some updates from string " + s.get());
        });
        b.onChange((c) -> {
            booleanFired[0]++;
            out.println("some updates from boolean " + b.get());
        });
        s.set("Anything");
        s.set("Hello");
        s.set("Bye");
        Assertions.assertEquals(3, stringFired[0]);
        Assertions.assertEquals(2, booleanFired[0]);
        out.assertEquals(
               "some updates from string Anything",
               "some updates from boolean true",
               "some updates from string Hello",
               "some updates from boolean false",
               "some updates from string Bye"
        );
    }

    @Test
    public void test_bind() {
        WritableString a = Props.of("test").stringOf(null);
        WritableString b = Props.of("test").stringOf(null);
        a.bind(b);
        a.set("Hello");
        Assertions.assertEquals("Hello", b.get());
        b.set("Bye");
        Assertions.assertEquals("Bye", a.get());
    }
}
