package net.vpc.common.tson;

import org.junit.Test;

public class TestProcessor {
    @Test
    public void testRemoveComments(){
        TsonElement a = Tson.obj().comments("Hello")
                .add("name", Tson.elem().set("Really me").anchor("ref"))
                .add("otherName", Tson.alias("ref"))
                .build();
        System.out.println(a);
        TsonElement b = Tson.processor().resolveAliases(a);
        System.out.println(b);
    }
}
