package net.thevpc.common.strings;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StringToObjectFactory {
    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final StringToObject STRING_STRING_PARSER = new StringToObject() {
        @Override
        public Object toObject(String value) {
            return value;
        }
    };
    public static final StringToObject LONG_STRING_PARSER = new StringToObject() {
        @Override
        public Object toObject(String value) {
            if (value.length()==0) {
                return null;
            }
            return Long.parseLong(value);
        }
    };
    public static final StringToObject TIMESTAMP_STRING_PARSER = new StringToObject() {
        @Override
        public Object toObject(String value) {
            if (value.length()==0) {
                return null;
            }
            try {
                return new Timestamp(TS.parse(value).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    };
    public static final StringToObject DOUBLE_STRING_PARSER = new StringToObject() {
        @Override
        public Object toObject(String value) {
            if (value.length()==0) {
                return null;
            }
            try {
                return Double.parseDouble(value);
            }catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
    };
}
