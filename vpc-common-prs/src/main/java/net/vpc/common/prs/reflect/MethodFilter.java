package net.vpc.common.prs.reflect;

import java.lang.reflect.Method;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public interface MethodFilter {
    public static final MethodFilter NONE = new MethodFilter() {
        public boolean accept(Method method) {
            return true;
        }
    };

    boolean accept(Method method);
}
