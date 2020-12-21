package net.thevpc.jshell;

import java.io.*;

public abstract class AbstractJShellHistory implements JShellHistory {
    @Override
    public void load(File file) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line=null;
            while((line=bufferedReader.readLine())!=null){
                add(line);
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    @Override
    public void load(Reader reader) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            if(reader instanceof BufferedReader) {
                bufferedReader = (BufferedReader) reader;
            }else{
                bufferedReader = new BufferedReader(reader);
            }
            String line=null;
            while((line=bufferedReader.readLine())!=null){
                add(line);
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    @Override
    public void save(PrintWriter writer) throws IOException {
        for (String element : getElements()) {
            writer.println(element);
        }
    }

    @Override
    public void save(PrintStream writer) throws IOException {
        for (String element : getElements()) {
            writer.println(element);
        }
    }

    @Override
    public void save(File file) throws IOException {
        if(file==null){
            return;
        }
        if(file.getParentFile()!=null){
            file.getParentFile().mkdirs();
        }
        PrintWriter w=null;
        try{
            w=new PrintWriter(new FileWriter(file));
            save(w);
        }finally {
            if(w!=null){
                w.close();
            }
        }
    }

    @Override
    public void append(JShellHistory other) {
        for (String element : other.getElements(-1)) {
            add(element);
        }
    }
}
