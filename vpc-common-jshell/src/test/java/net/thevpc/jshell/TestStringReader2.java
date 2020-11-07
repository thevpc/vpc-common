package net.thevpc.jshell;

import net.thevpc.jshell.parser2.StringReader2;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class TestStringReader2 {
    @Test
    public void test2(){
        StringReader2 r=StringReader2.fromString(
                "a\\\nb\\nt"
        );
        for (String line : r.lines()) {
            System.out.println(line);
        }
    }
}
