package net.thevpc.jshell;

import net.thevpc.jshell.parser2.JShellParser2;
import org.junit.jupiter.api.Test;

public class TestJShellParser2 {
    @Test
    public void test2(){
        JShellParser2 r= JShellParser2.fromString(
                "a\\\nb\\nt"
        );
        for (String line : r.strReader().lines()) {
            System.out.println(line);
        }
    }
}
