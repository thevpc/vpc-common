package net.vpc.common.prs.log;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public interface TLoadableLog extends TLog{
    String tail(int size);
}
