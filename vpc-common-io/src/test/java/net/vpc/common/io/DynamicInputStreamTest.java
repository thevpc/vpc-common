package net.vpc.common.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;

public class DynamicInputStreamTest {

    @Test
    public void testBigger() {
        InputStream b=new DynamicInputStream(10) {
            @Override
            protected boolean requestMore() {
                this.push(new byte[]{1,2,3,4}, 0,4);
                return true;
            }
        };
        int x=-1;
        int max=100;
        try {
//            while (max > 0 && (x = b.read()) != -1) {
//                System.out.println(max+"::"+x);
//                max--;
//            }
            byte[] buf=new byte[5];
            while (max > 0 && (x = b.read(buf)) != -1) {
                Assert.assertEquals(4,x);
                Assert.assertArrayEquals(new byte[]{1,2,3,4,0}, buf);
                System.out.println(max+"::"+ x+"::"+Arrays.toString(buf));
                max--;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void testSmaller() {
        InputStream b=new DynamicInputStream(10) {
            @Override
            protected boolean requestMore() {
                this.push(new byte[]{1,2,3,4}, 0,4);
                return true;
            }
        };
        int x=-1;
        int max=100;
        try {
//            while (max > 0 && (x = b.read()) != -1) {
//                System.out.println(max+"::"+x);
//                max--;
//            }
            byte[] buf=new byte[2];
            boolean ot=true;
            while (max > 0 && (x = b.read(buf)) != -1) {
                Assert.assertEquals(2,x);
                if(ot) {
                    Assert.assertArrayEquals(new byte[]{1, 2}, buf);
                }else{
                    Assert.assertArrayEquals(new byte[]{3, 4}, buf);
                }
                System.out.println(max+"::"+ x+"::"+Arrays.toString(buf));
                max--;
                ot=!ot;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Test
    public void testOne() {
        InputStream b=new DynamicInputStream(10) {
            @Override
            protected boolean requestMore() {
                this.push(new byte[]{1,2,3,4}, 0,4);
                return true;
            }
        };
        int x=-1;
        int max=100;
        try {
            int v=1;
            while (max > 0 && (x = b.read()) != -1) {
                System.out.println(max+"::"+x);
                Assert.assertEquals(v,x);
                v+=1;
                if(v>4){
                    v=1;
                }
                max--;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
