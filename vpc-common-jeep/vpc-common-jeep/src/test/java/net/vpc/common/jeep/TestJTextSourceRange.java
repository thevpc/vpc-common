package net.vpc.common.jeep;

import net.vpc.common.textsource.JTextSourceFactory;
import net.vpc.common.textsource.log.JSourceMessage;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

public class TestJTextSourceRange {
    @Test
    public void test1(){
        JToken t=new JToken();
        String text = "\nexample\nmessage\n";
        t.source= JTextSourceFactory.fromString(text,null);
        t.startCharacterNumber=text.length();
        t.endCharacterNumber=4;
        JSourceMessage m=new JSourceMessage("0", null, "error", Level.SEVERE, t);
        System.out.println(m);
    }
}
