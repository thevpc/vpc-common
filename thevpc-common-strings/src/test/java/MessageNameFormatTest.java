
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.thevpc.common.strings.MessageNameFormat;
import net.thevpc.common.strings.MessageNameFormatContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author thevpc
 */
public class MessageNameFormatTest {

    @Test
    public void test1() {
        MessageNameFormat m = new MessageNameFormat("${title} named $name (id is ${id}) is updated. Fields updated are : ${fields}");
        String s = m.format(new HashMap<String, Object>(), new MessageNameFormatContext().addDefaults());
        System.out.println(s);

    }

    @Test
    public void testEscape() {
        MessageNameFormat f = new MessageNameFormat("Hello \\${world}");

        HashMap<String, Object> p = new HashMap<>();
        p.put("world", "World");
        System.out.println(f.format(p, new MessageNameFormatContext().addDefaults()));
        Assertions.assertEquals("Hello ${world}", f.format(p, new MessageNameFormatContext().addDefaults()));
    }

    @Test
    public void testNotFound() {
        MessageNameFormat f = new MessageNameFormat("Hello ${World}");

        HashMap<String, Object> p = new HashMap<>();
        p.put("world", "World");
        System.out.println(f.format(p, new MessageNameFormatContext().addDefaults()));
        Assertions.assertEquals("Hello null", f.format(p, new MessageNameFormatContext().addDefaults()));
    }

    @Test
    public void testDate() {
        MessageNameFormat f = new MessageNameFormat("Hello ${date(date,'yyyy-MM')}");

        HashMap<String, Object> p = new HashMap<>();
        p.put("date", new Date());
        System.out.println(f.format(p, new MessageNameFormatContext().addDefaults()));
        Assertions.assertEquals("Hello " + new SimpleDateFormat("yyyy-MM").format(p.get("date")), f.format(p, new MessageNameFormatContext().addDefaults()));
    }

    @Test
    public void testSwitch() {
        MessageNameFormat f = new MessageNameFormat("Hello ${world} ${switch(count,0,'none',1,'one','any')}");

        HashMap<String, Object> p = new HashMap<>();
        p.put("world", "World");
        p.put("count", 3);
        Assertions.assertEquals("Hello World any", f.format(p, new MessageNameFormatContext().addDefaults()));

        p.put("count", 1);
        Assertions.assertEquals("Hello World one", f.format(p, new MessageNameFormatContext().addDefaults()));

        p.put("count", 0);
        Assertions.assertEquals("Hello World none", f.format(p, new MessageNameFormatContext().addDefaults()));

        p.put("count", 0.0);
        Assertions.assertEquals("Hello World any", f.format(p, new MessageNameFormatContext().addDefaults()));
    }
}
