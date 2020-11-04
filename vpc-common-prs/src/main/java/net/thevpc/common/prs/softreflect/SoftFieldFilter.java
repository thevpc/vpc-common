package net.thevpc.common.prs.softreflect;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public interface SoftFieldFilter {
    public static final SoftFieldFilter NONE = new SoftFieldFilter() {
        public boolean accept(SoftField field) {
            return true;
        }
    };

    boolean accept(SoftField field);
}
