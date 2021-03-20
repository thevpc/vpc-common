package net.thevpc.common.time;

class _StringUtils {
    public static String formatLeft(Object number, int size) {
        String s = String.valueOf(number);
        int len = s.length();
        int bufferSize = Math.max(size, len);
        StringBuilder sb = new StringBuilder(bufferSize);
        sb.append(s);
        for (int i = bufferSize-len; i >0 ; i--) {
            sb.append(' ');
        }
        return sb.toString();
    }

    public static String formatRight(Object number, int size) {
        String s = String.valueOf(number);
        int len = s.length();
        int bufferSize = Math.max(size, len);
        StringBuilder sb = new StringBuilder(bufferSize);
        for (int i = bufferSize-len; i >0 ; i--) {
            sb.append(' ');
        }
        sb.append(s);
        return sb.toString();
    }

    public static String fillString(char x, int width) {
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < width; i++) {
            sb.append(x);
        }
        return sb.toString();
    }


    public static String formatLeft(Object number, int size,boolean fixedLength) {
        if (fixedLength) {
            return formatLeft(number, size);
        } else {
            return String.valueOf(number);
        }
    }

}
