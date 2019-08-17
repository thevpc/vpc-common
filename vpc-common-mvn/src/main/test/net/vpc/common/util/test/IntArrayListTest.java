package net.vpc.common.util.test;

import net.vpc.common.util.Chronometer;
import net.vpc.common.util.IntArrayList;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IntArrayListTest {

    @Test
    public void test0(){
        IntArrayList a=new IntArrayList(0);
        int oldCommited=-1;
        int commitCount=0;
        for (int i = 0; i < 1000; i++) {
            a.add(i);
            if(a.getCommittedSize()!=oldCommited){
                oldCommited=a.getCommittedSize();
                commitCount++;
            }
            //System.out.println(a.size()+"/"+a.getCommittedSize());
        }
        System.out.println(commitCount);
    }

    @Test
    public void test1(){
        IntArrayList a=new IntArrayList();
        Assert.assertEquals("[]",a.toString());
        a.addAll(1,2,3);
        Assert.assertEquals("[1,2,3]",a.toString());
        a.addAll(a);
        Assert.assertEquals("[1,2,3,1,2,3]",a.toString());

        a.insertAll(2,7,9);
        System.out.println(a+" : insertAll "+a.toStringDebug());

        a.removeAll(2,2);
        System.out.println(a+" : insertAll "+a.toStringDebug());

        a.removeAll(-1,2);
        System.out.println(a+" : insertAll "+a.toStringDebug());

        a.removeAll(5,2);
        System.out.println(a+" : removeAll "+a.toStringDebug());

        a.removeAll(6,2);
        System.out.println(a+" : removeAll "+a.toStringDebug());

        a.insertAll(0,7,9);
        System.out.println(a+" : insertAll "+a.toStringDebug());

        a.insertAll(7,7,9);
        System.out.println(a+" : insertAll "+a.toStringDebug());

        a.replaceSubList(2,2,8,8,8);
        System.out.println(a+" : replaceSubList "+a.toStringDebug());
    }

    @Test
    public void testPerf(){
        Chronometer c=new Chronometer();
        IntArrayList a1=new IntArrayList();
        int count = 100000;
        for (int i = 0; i < count; i++) {
            a1.add(0,i);
        }
        System.out.println(c.restart());
        List<Integer> a2=new ArrayList<>();
        for (int i = 0; i < count; i++) {
            a1.add(0,i);
        }
        System.out.println(c.restart());
    }
}
