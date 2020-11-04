package net.thevpc.common.prs.softreflect;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public interface SoftMethodFilter {
    public static final SoftMethodFilter NONE = new SoftMethodFilter() {
        public boolean accept(SoftMethod method) {
            return true;
        }
    };

    boolean accept(SoftMethod method);
}
