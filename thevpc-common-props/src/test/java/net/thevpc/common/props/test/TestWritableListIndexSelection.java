package net.thevpc.common.props.test;

import net.thevpc.common.props.PropertyType;
import net.thevpc.common.props.WritableList;
import net.thevpc.common.props.impl.WritableListImpl;
import net.thevpc.common.props.impl.WritableListIndexSelectionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestWritableListIndexSelection {
    @Test
    public void test(){
        WritableList<String> base=new WritableListImpl<>(
                "base",PropertyType.of(String.class)
        );
        WritableListIndexSelectionImpl<String> selection
                =new WritableListIndexSelectionImpl<>(
                        "selection",
                PropertyType.of(String.class),
                base
        );
        base.addAll("A","B","C");
        System.out.println("init");
        System.out.println("\t"+selection+" : "+selection.indices());
        System.out.println("multipleSelection=true");
        selection.multipleSelection().set(true);
        System.out.println("\t"+selection+" : "+selection.indices());

        System.out.println("select B");
        selection.add("B");
        System.out.println("\t"+selection+" : "+selection.indices());
        Assertions.assertEquals(Arrays.asList("B"),selection.toList());

        System.out.println("select A");
        selection.add("A");
        System.out.println("\t"+selection+" : "+selection.indices());
        Assertions.assertEquals(Arrays.asList("B","A"),selection.toList());

        System.out.println("select F");
        selection.add("F");
        System.out.println("\t"+selection+" : "+selection.indices());
        Assertions.assertEquals(Arrays.asList("B","A"),selection.toList());

        System.out.println("select C");
        selection.indices().add(2);
        System.out.println("\t"+selection+" : "+selection.indices());
        Assertions.assertEquals(Arrays.asList("B","A","C"),selection.toList());
    }

    @Test
    public void test2(){
        WritableList<String> base=new WritableListImpl<>(
                "base",PropertyType.of(String.class)
        );
        WritableListIndexSelectionImpl<String> selection
                =new WritableListIndexSelectionImpl<>(
                        "selection",
                PropertyType.of(String.class),
                base
        );
        base.addAll("A","B","C");

        System.out.println("init");
        System.out.println("\t"+selection+" : "+selection.indices());

        System.out.println("multipleSelection=true");
        selection.multipleSelection().set(true);
        System.out.println("\t"+selection+" : "+selection.indices());

        System.out.println("select B");
        selection.indices().add(1);
        System.out.println("\t"+selection+" : "+selection.indices());
        Assertions.assertEquals(Arrays.asList("B"),selection.toList());

        System.out.println("select C");
        selection.indices().add(2);
        System.out.println("\t"+selection+" : "+selection.indices());
        Assertions.assertEquals(Arrays.asList("B","C"),selection.toList());

        System.out.println("multipleSelection=true");
        selection.multipleSelection().set(false);
        System.out.println("\t"+selection+" : "+selection.indices());
        Assertions.assertEquals(Arrays.asList("C"),selection.toList());

        System.out.println("select B");
        selection.indices().add(1);
        System.out.println("\t"+selection+" : "+selection.indices());
        Assertions.assertEquals(Arrays.asList("B"),selection.toList());


    }
}
