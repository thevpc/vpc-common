
import net.thevpc.common.strings.Pattern2;
import org.junit.jupiter.api.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vpc
 */
public class Pattern2Test {

    @Test
    public void testA() {
        Pattern2 p = Pattern2.compile2("a-£(name1).b-£name2.c",
                "[^.]+",
                "[^.]+"
        );
        for (Pattern2.Matcher2 m : p.allMatches("a-rr.b-cc.c")) {
            System.out.println("FOUND");
            System.out.println(m.subPuttern(1));
            System.out.println(m.subPuttern(2));
        }
        for (Pattern2.Matcher2 m : p.allMatches("a-rr.b-cc.c")) {
            System.out.println("FOUND");
            System.out.println(m.subPuttern("name1"));
            System.out.println(m.subPuttern("name2"));
        }
    }

    @Test
    public void testB() {
        Pattern2 p = Pattern2.compile2("a-£1.b-£2.c",
                "[^.]+",
                "[^.]+"
        );
        for (Pattern2.Matcher2 m : p.allMatches("a-rr.b-cc.c")) {
            System.out.println("FOUND");
            System.out.println(m.subPuttern(1));
            System.out.println(m.subPuttern(2));
        }
    }


}
