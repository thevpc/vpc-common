package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.io.SimpleFileCharSupplier;
import net.vpc.common.jeep.impl.io.TextCharSupplier;
import net.vpc.common.jeep.impl.io.URLCharSupplier;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.function.Supplier;

public class DefaultJCompilationUnit implements JCompilationUnit {
    private JSource source;
    private JNode ast;

    public DefaultJCompilationUnit(JSource source) {
        this.source = source;
    }

    public DefaultJCompilationUnit(JSource source, JNode ast) {
        this.source = source;
        this.ast = ast;
    }
    
    public DefaultJCompilationUnit(JSource source, JContext languageContext) {
        this.source=source;
        this.setAst(languageContext.parsers().of(source.reader(), this).parse());
    }
//
//    public DefaultJCompilationUnit(File source) {
//        this(source.getPath(),new SimpleFileCharSupplier(source));
//    }
//
//    public DefaultJCompilationUnit(URL source) {
//        this(source.toString(),new URLCharSupplier(source));
//    }
//
//    public DefaultJCompilationUnit(char[] source) {
//        this("<text>",new TextCharSupplier(new String(source)));
//    }
//
//    public DefaultJCompilationUnit(Supplier<char[]> content) {
//        this(content.toString(),content);
//    }
//
//    public DefaultJCompilationUnit(String source, Supplier<char[]> content) {
//        this.source = source;
//        this.content = content;
//    }

    @Override
    public JSource getSource() {
        return source;
    }

    @Override
    public JNode getAst() {
        return ast;
    }

    @Override
    public String toString() {
        return "CompilationUnit(" +source+')';
    }

    @Override
    public void setAst(JNode newNode) {
        this.ast=newNode;
    }
}
