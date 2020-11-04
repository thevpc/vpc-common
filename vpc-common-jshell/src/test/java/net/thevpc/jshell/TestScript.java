package net.thevpc.jshell;

import net.thevpc.jshell.parser.JShellParser;
import net.thevpc.jshell.parser.ParseException;
import net.thevpc.jshell.parser.nodes.Node;
import org.junit.jupiter.api.Test;

public class TestScript {
    @Test
    public void test() {
        JShellParser p = new JShellParser();
        Node n = null;
        try {
            n = p.parse("$(dirname $0)");
//            n = p.parse("base=$(dirname $0)");
            System.out.println(n);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
