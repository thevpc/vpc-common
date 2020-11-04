package net.thevpc.commons.md;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMdWriter implements MdWriter{
    private Writer writer;
    private Map<String,Object> properties=new HashMap<>();
    private boolean headerWritten;

    @Override
    public void setHeader(String name, Object value) {
        properties.put(name,value);
    }

    @Override
    public final void write(MdElement element) {
        ensureHeaderWritten();
        writeImpl(element);
    }

    public abstract void writeImpl(MdElement element) ;

    private void ensureHeaderWritten() {
        if(!headerWritten){
            headerWritten=true;
            writeHeader();
        }
    }

    public Object getRequiredProperty(String name) {
        Object v = properties.get(name);
        if(v==null){
            throw new IllegalArgumentException("Missing property "+name);
        }
        return v;
    }
    public Object getProperty(String name,Object defaultValue) {
        Object v = properties.get(name);
        if(v==null){
            return defaultValue;
        }
        return v;
    }

    protected void writeHeader() {

    }

    public AbstractMdWriter(Writer writer) {
        this.writer = writer;
    }

    protected void write(String text){
        try {
            writer.write(text);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected void writeln(){
        try {
            writer.write("\n");
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected void writeln(String text){
        try {
            writer.write(text);
            writer.write("\n");
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void close() {
        ensureHeaderWritten();
        try {
            writer.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
