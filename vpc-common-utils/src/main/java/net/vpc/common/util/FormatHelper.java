package net.vpc.common.util;

class FormatHelper {
    static String formatLeft(Object number, int size,boolean fixedLength) {
        if (fixedLength) {
            return sformatLeft(number, size);
        } else {
            return String.valueOf(number);
        }
    }
    static String sformatLeft(Object number,int size){
        StringBuilder sb=new StringBuilder();
        sb.append(number);
        while (sb.length()<size){
            sb.insert(0,' ');
        }
        return sb.toString();
    }
    static String fillString(char x, int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            sb.append(x);
        }
        return sb.toString();
    }

}
