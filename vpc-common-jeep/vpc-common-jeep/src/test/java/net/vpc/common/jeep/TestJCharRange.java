package net.vpc.common.jeep;

import net.vpc.common.jeep.core.compiler.JSourceFactory;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

public class TestJCharRange {
    @Test
    public void test1(){
        JToken t=new JToken();
        String text = "\nexample\nmessage\n";
        t.compilationUnit=new DefaultJCompilationUnit(JSourceFactory.fromString(text,null));
        t.startCharacterNumber=text.length();
        t.endCharacterNumber=4;
        JCompilerMessage m=new JCompilerMessage("0","error", Level.SEVERE, t);
        System.out.println(m);
    }
}
