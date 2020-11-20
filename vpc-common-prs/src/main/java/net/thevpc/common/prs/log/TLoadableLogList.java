package net.thevpc.common.prs.log;

import java.util.ArrayList;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 7 août 2009
 */
public class TLoadableLogList extends TLogList implements TLoadableLog{
    private TLoadableLog loadableLog;
    public TLoadableLogList(TLoadableLog loadableLog,TLog... list) {
        super(concat(loadableLog,list));
        this.loadableLog=loadableLog;
    }

    private static TLog[] concat(TLog loadableLog,TLog... list){
        ArrayList<TLog> all=new ArrayList<TLog>();
        all.add(loadableLog);
        for (TLog log : list) {
            if (log!=null) {
                all.add(log);
            }
        }
        return all.toArray(new TLog[all.size()]);
    }

    public String tail(int size) {
        return loadableLog.tail(size);
    }
}
