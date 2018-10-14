//package net.vpc.common.util.test;
//
//
//import net.vpc.common.util.MemorySizeFormatter;
//import net.vpc.common.util.Units;
//import org.junit.Assert;
//import org.junit.Test;
//
//public class MemorySizeFormatterTest {
//    @Test
//    public void test(){
//        long value= Units.GiBYTE;
//        String[] results={
//            "  0Ei   0Pi   0Ti   1Gi   0Mi   0Ki",
//            "  0E   0P   0T   1G  73M 741K 824B",
//            "  1G",
//            "  1G  73M",
//            "  1G  73M 741K",
//            "  1Gi",
//            "  1Gi",
//            "  1Gi",
//            "  0Ei   0Pi   0Ti   1Gi   0Mi   0Ki",
//            "  1Gi",
//            "  1Gi",
//            "  0Ti   1Gi"
//        };
//        String[] formats = {
//                "0I0BEF0", "00BEF0", "B0TD1F", "B0TD2F", "B0TD3F", "B0TD1FI", "B0TD2FI", "B0TD3FI", "0I0BEF0", "I0BEF", "IBEF", "0IBTF"
//        };
//        for (int i = 0; i < formats.length; i++) {
//            String s = formats[i];
//            String format = new MemorySizeFormatter(s).format(value);
//            Assert.assertEquals(format,results[i]);
//        }
//    }
//}
