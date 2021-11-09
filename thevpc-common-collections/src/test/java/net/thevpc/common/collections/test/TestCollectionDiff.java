/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.collections.test;

import java.util.Arrays;
import java.util.List;
import net.thevpc.common.collections.CollectionDiff;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author thevpc
 */
public class TestCollectionDiff {

    @Test
    public void test1() {
        List<String> a = Arrays.asList("a", "b", "c", "a", "e"); //2*a,   b, c,  , e 
        List<String> b = Arrays.asList("c", "b", "a", "b", "d"); //a  , 2*b, c, d,
        //a removed, b added, added, c unchanged, d added, e removed
        //a&e removed, b&d added, c unchanged
        CollectionDiff diff = CollectionDiff.of(a, b);
        System.out.println(diff);
        Assertions.assertEquals(Arrays.asList("a","b","c"), diff.getUnchanged()); // a,b,c
        Assertions.assertEquals(Arrays.asList("b","d"), diff.getAdded());     // b,d added
        Assertions.assertEquals(Arrays.asList("a","e"), diff.getRemoved());   // a,e removed
    }

    @Test
    public void test2() {
        List<String> a = Arrays.asList("a", "b", "C", "a", "e");
        List<String> b = Arrays.asList("c", "b", "a", "b", "D");
        CollectionDiff diff = CollectionDiff.of(a, b, x -> x.toLowerCase());
        System.out.println(diff);
        Assertions.assertEquals(3, diff.getUnchanged().size());
        Assertions.assertEquals(2, diff.getAdded().size());
        Assertions.assertEquals(2, diff.getRemoved().size());
    }
}
