/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.impl.compiler;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.function.Supplier;

import net.vpc.common.jeep.JCharRange;
import net.vpc.common.jeep.JSource;

/**
 *
 * @author vpc
 */
public class DefaultJCompilationUnitSource implements JSource {
    
    String name;
    Reader reader;
    Supplier<char[]> supplier;
    private boolean consumed=false;
    public DefaultJCompilationUnitSource(String name,Reader reader, Supplier<char[]> supplier) {
        this.reader = reader;
        this.name = name;
        this.supplier = supplier;
    }

    public String name() {
        return name;
    }

    @Override
    public Reader reader() {
        if(consumed){
            return new StringReader(text());
        }else {
            consumed=true;
            return reader;
        }
    }

    @Override
    public String text() {
        return new String(supplier.get());
    }

    @Override
    public char[] charArray() {
        return supplier.get();
    }

    @Override
    public JCharRange range(int from, int to) {
        char[] chars = charArray();
        if(from<0){
            from=0;
        }
        if(to>chars.length){
            to=chars.length;
        }
        return new JCharRange(
                from,
                Arrays.copyOfRange(chars,from,to)
        );
    }
}
