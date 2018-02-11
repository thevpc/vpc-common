package net.vpc.common.prs.log;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 7 août 2009
 */
public class TLogList implements TLog {
    private final TLog[] list;

    public TLogList(TLog... list) {
        this.list = list;
    }

    @Override
    public void trace(Object msg) {
        for (TLog log : list) {
            log.trace(msg);
        }
    }

    @Override
    public void error(Object msg) {
        for (TLog log : list) {
            log.error(msg);
        }
    }

    @Override
    public void error(String message, Object msg) {
        for (TLog log : list) {
            log.error(message,msg);
        }
    }

    @Override
    public void warning(Object msg) {
        for (TLog log : list) {
            log.warning(msg);
        }
    }

    @Override
    public void debug(Object msg) {
        for (TLog log : list) {
            log.debug(msg);
        }
    }

    public TLog[] getList() {
        return list;
    }

    @Override
    public void close() {
        for (TLog log : list) {
            log.close();
        }
    }
}
