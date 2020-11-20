package net.thevpc.common.prs.log;


import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 3 janv. 2006 21:40:14
 */
public class TLogStringBuffer implements TLoadableLog {
    private final StringBuffer buffer=new StringBuffer();
    private int max=Integer.getInteger("StringBufferLog.MAX",100000);

    public TLogStringBuffer() {
    }

    public TLogStringBuffer(int max) {
        this.max = max;
    }

    @Override
    public void trace(Object msg) {
        write("trace",msg);
    }

    @Override
    public void error(Object msg) {
        write("error",msg);
    }

    @Override
    public void error(String message, Object msg) {
        write("error",message);
        write("error",msg);
    }

    @Override
    public void warning(Object msg) {
        write("warning",msg);
    }

    @Override
    public void debug(Object msg) {
        write("debug",msg);
    }

    protected void write(String type, Object msg) {
        buffer.append("[").append(type).append("] ");
        if (msg == null) {
            buffer.append("");
        } else if (msg instanceof SQLException) {
            SQLException s = (SQLException) msg;
            boolean first=true;
            while (s != null) {
                if(first){
                    first=false;
                }else{
                    buffer.append("\n------------------\n");
                }
                buffer.append(throwableToString(s));
                s = s.getNextException();
            }
        } else if(msg instanceof Throwable){
            buffer.append(throwableToString((Throwable) msg));
        }else{
            buffer.append(msg);
        }
        buffer.append("\n");
        if(buffer.length()>max){
            buffer.delete(0,buffer.length()-max);
        }
    }

    private String throwableToString(Throwable th) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        th.printStackTrace(new PrintStream(out));
        return (out.toString());
    }

    @Override
    public String tail(int size) {
        int len=buffer.length();
        if(size<=0 || size>=len){
            return buffer.toString();
        }else {
            return buffer.substring(len-size,len);
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    @Override
    public void close() {
        //do nothing
    }
    
}
