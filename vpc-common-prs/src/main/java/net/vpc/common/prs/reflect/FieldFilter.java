package net.vpc.common.prs.reflect;

import java.lang.reflect.Field;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public interface FieldFilter {
    public static final FieldFilter NONE = new FieldFilter() {
        public boolean accept(Field field) {
            return true;
        }
    };

    boolean accept(Field field);
}
