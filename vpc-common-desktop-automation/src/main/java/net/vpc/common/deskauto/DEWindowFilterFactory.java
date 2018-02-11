/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.deskauto;

import net.vpc.common.strings.StringComparator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vpc
 */
public class DEWindowFilterFactory {

    public static DEWindowFilter and(DEWindowFilter... other) {
        final List<DEWindowFilter> valid = new ArrayList<>();
        if (other != null) {
            for (DEWindowFilter o : other) {
                if (o != null) {
                    valid.add(o);
                }
            }
        }
        if (valid.isEmpty()) {
            return none();
        }
        if (valid.size() == 1) {
            return valid.get(0);
        }
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                for (DEWindowFilter v : valid) {
                    if (!v.accept(w)) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public String toString() {
                StringBuilder b = new StringBuilder("(");
                for (int i = 0; i < valid.size(); i++) {
                    if (i > 0) {
                        b.append(" & ");
                    }
                    b.append(valid.get(i));
                }
                b.append(")");
                return b.toString();
            }

        };
    }

    public static DEWindowFilter or(DEWindowFilter... other) {
        final List<DEWindowFilter> valid = new ArrayList<>();
        if (other != null) {
            for (DEWindowFilter o : other) {
                if (o != null) {
                    valid.add(o);
                }
            }
        }
        if (valid.isEmpty()) {
            return all();
        }
        if (valid.size() == 1) {
            return valid.get(0);
        }
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                for (DEWindowFilter v : valid) {
                    if (v.accept(w)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString() {
                StringBuilder b = new StringBuilder("(");
                for (int i = 0; i < valid.size(); i++) {
                    if (i > 0) {
                        b.append(" | ");
                    }
                    b.append(valid.get(i));
                }
                b.append(")");
                return b.toString();
            }

        };
    }

    public static DEWindowFilter none() {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return false;
            }

            @Override
            public String toString() {
                return "none";
            }
        };
    }

    public static DEWindowFilter all() {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return true;
            }

            @Override
            public String toString() {
                return "all";
            }
        };
    }

    public static DEWindowFilter visible() {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return w.isVisible();
            }

            @Override
            public String toString() {
                return "visible";
            }
        };
    }

    public static DEWindowFilter special() {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return w.isSpecial();
            }

            @Override
            public String toString() {
                return "special";
            }
        };
    }
    public static DEWindowFilter child() {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return w.isChild();
            }

            @Override
            public String toString() {
                return "child";
            }
        };
    }

    public static DEWindowFilter not(final DEWindowFilter other) {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return !other.accept(w);
            }

            @Override
            public String toString() {
                return "not(" + other + ")";
            }
        };
    }

    public static DEWindowFilter regular() {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return w.isRegular();
            }

            @Override
            public String toString() {
                return "regular";
            }
        };
    }

    public static DEWindowFilter title(final StringComparator c) {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return c.matches(w.getTitle());
            }

            @Override
            public String toString() {
                return "(title  : " + c + ")";
            }
        };
    }

    public static DEWindowFilter module(final StringComparator c) {
        return new DEWindowFilter() {

            @Override
            public boolean accept(DEWindow w) {
                return c.matches(w.getModuleName());
            }

            @Override
            public String toString() {
                return "(module  : " + c + ")";
            }
        };
    }
}
