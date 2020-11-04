package net.thevpc.common.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
                Assertions.assertEquals(4,x);
                Assertions.assertArrayEquals(new byte[]{1,2,3,4,0}, buf);
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
                Assertions.assertEquals(2,x);
                if(ot) {
                    Assertions.assertArrayEquals(new byte[]{1, 2}, buf);
                }else{
                    Assertions.assertArrayEquals(new byte[]{3, 4}, buf);
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
                Assertions.assertEquals(v,x);
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
