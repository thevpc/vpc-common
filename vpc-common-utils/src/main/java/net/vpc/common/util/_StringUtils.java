package net.vpc.common.util;

class _StringUtils {
    public static String formatLeft(Object number, int size) {
        StringBuilder sb = new StringBuilder(size);
        sb.append(number);
        while (sb.length() < size) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String formatRight(Object number, int size) {
        StringBuilder sb = new StringBuilder(size);
        sb.append(number);
        while (sb.length() < size) {
            sb.insert(0, ' ');
        }
        return sb.toString();
    }
    public static String fillString(char x, int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            sb.append(x);
        }
        return sb.toString();
    }
}
