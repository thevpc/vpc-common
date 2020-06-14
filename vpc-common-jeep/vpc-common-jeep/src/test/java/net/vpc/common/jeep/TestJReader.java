package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.tokens.JReader;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class TestJReader {
    @Test
    public void test1() {
        JReader r=new JReader(new StringReader("abcd"));
        r.read();
        System.out.println(r);
    }
}
