package net.thevpc.common.util.test;

import net.thevpc.common.util.DecimalFormat2;
import org.junit.jupiter.api.Test;

public class DefaultNumberFormatTest {
    @Test
    public void test1(){
        DecimalFormat2 df = new DecimalFormat2("__00.00_");
//        System.out.println(df.format(12.336));
        System.out.println(df.format(2.336));

//        DefaultNumberFormat f=new DefaultNumberFormat("Ω","E-3..3 0.0*");
//        DefaultNumberFormat f=new DefaultNumberFormat("Ω","E-3..3 #0.00");
//        System.out.println(f.format(336553.336558E-2));
    }
}
