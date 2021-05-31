package net.thevpc.common.props.impl;

public class PropsUtils {
    public static Integer parseInt(String s) {
        if (s != null && s.length() > 0) {
            try {
                return Integer.parseInt(s);
            } catch (Exception ex) {
                //
            }
        }
        return null;
    }
}
