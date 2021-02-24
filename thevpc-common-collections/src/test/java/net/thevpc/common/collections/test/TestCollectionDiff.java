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
 * @author vpc
 */
public class TestCollectionDiff {

    @Test
    public void test1() {
        List<String> a = Arrays.asList("a", "b", "c", "a", "e");
        List<String> b = Arrays.asList("c", "b", "a", "b", "d");
        CollectionDiff diff = CollectionDiff.of(a, b);
        System.out.println(diff);
        Assertions.assertEquals(3, diff.getUnchanged().size());
        Assertions.assertEquals(2, diff.getAdded().size());
        Assertions.assertEquals(1, diff.getRemoved().size());
    }
}
