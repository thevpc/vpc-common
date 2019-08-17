/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;

/**
 *
 * @author vpc
 */
public class PrintStreamWriter extends Writer{
    private PrintStream out;

    public PrintStreamWriter(PrintStream out) {
        this.out = out;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        out.append(new String(cbuf,off,len));
    }

    @Override
    public void flush() {
        out.flush();
    }

    @Override
    public void close() {
        out.close();
    }
    
}
