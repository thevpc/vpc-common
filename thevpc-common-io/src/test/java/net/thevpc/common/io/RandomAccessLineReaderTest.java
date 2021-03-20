/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.io;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author thevpc
 */
public class RandomAccessLineReaderTest {

    static File file;

    @BeforeAll
    public static void setup() throws IOException {
        System.out.println("setup");
        file = File.createTempFile("temp", "temp");
        PrintStream out = new PrintStream(file);
        for (int i = 0; i < 100; i++) {
            out.println("line number " + (i));
        }
        out.close();
    }

    @AfterAll
    public static void tearup() {
        file.delete();
    }

    @Test
    public void test_count() throws FileNotFoundException, IOException {
        if (file == null) {
            //maven is not calling setup
            setup();
        }
        System.out.println("test_count");
        try (RandomAccessLineReader r = new RandomAccessLineReader(file)) {
            Assertions.assertEquals(100, r.countLines());
            Assertions.assertEquals("line number " + 0, r.readLine());
            Assertions.assertEquals(100, r.countLines());
            Assertions.assertEquals("line number " + 1, r.readLine());
        }
    }

    @Test
    public void test_readLine_int() throws FileNotFoundException, IOException {
        if (file == null) {
            //maven is not calling setup
            setup();
        }
        try (RandomAccessLineReader r = new RandomAccessLineReader(file)) {
            for (int i = 3; i < 5; i++) {
                String s = r.readLine(i);
                Assertions.assertEquals("line number " + i, s);
            }
            for (int i = 3; i < 5; i++) {
                int j = 5 - i + 3;
                String s = r.readLine(j);
                Assertions.assertEquals("line number " + j, s);
            }
        }
    }

    @Test
    public void testOrientation() throws IOException {
        if (file == null) {
            //maven is not calling setup
            setup();
        }
        try (RandomAccessLineReader r = new RandomAccessLineReader(file)) {
            r.forward();
            m(r);
            m(r);
            r.backward();
            m(r);
            m(r);
            r.forward();
            m(r);
            m(r);
            r.close();
        }
    }

    private static void m(RandomAccessLineReader r) throws IOException {
        if (file == null) {
            //maven is not calling setup
            setup();
        }
        long n0 = r.getNextLineNumber();
        long l0 = r.getLastLineNumber();
        String rr = r.readLine();
        long n1 = r.getNextLineNumber();
        long l1 = r.getLastLineNumber();
//        System.out.println((r.getOrientation()==Orientation.FORWARD?'F':'B')+":" + n0 + " -> " + l1+" :: "+rr);
        System.out.println((r.getOrientation() == RandomAccessOrientation.FORWARD ? 'F' : 'B') + ":" + n0 + "/" + l0 + " -> " + n1 + "/" + l1 + " :: " + rr);
//        System.out.println("a:" + l0 + ":" + n0+" -> " + l1+":"+n1);

    }

}
