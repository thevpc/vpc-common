package net.thevpc.common.config;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Taha BEN SALAH (thevpc@walla.com) %creationtime 13 juil. 2006
 * 22:14:21
 */
public abstract class AbstractConfiguration implements Configuration {

    private static final char[] hexDigit = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F'
    };
    private static boolean static_persistenceEnabled = true;
    public static final String USER_CONFIGURATION_CHANGED = "USER_CONFIGURATION_CHANGED";
    private Map<String, Converter> typesToConverters = new HashMap<String, Converter>();
    private Map<Class, Converter> classesToConverters = new HashMap<Class, Converter>();
    private ConfigOptions options = new ConfigOptions();
//    private static Configuration globalConfig;
//    private static Configuration userConfig;

//    @Override
    public ConfigOptions getOptions() {
        return options;
    }

    

    public void registerConverter(Converter c) {
        typesToConverters.put(c.getAcceptedType(), c);
        classesToConverters.put(c.getAcceptedClass(), c);
    }

    private void initConverters() {
        registerConverter(new IntConverter());
        registerConverter(new LongConverter());
        registerConverter(new ShortConverter());
        registerConverter(new DoubleConverter());
        registerConverter(new FloatConverter());
        registerConverter(new ByteConverter());
        registerConverter(new CharConverter());
        registerConverter(new DateConverter(this));
        registerConverter(new TimeConverter());
        registerConverter(new ObfuscatedValueConverter());
        registerConverter(new LocaleConverter());
        registerConverter(new ColorConverter());
        registerConverter(new FontConverter());
        registerConverter(new BooleanConverter());
        registerConverter(new StringConverter());
        registerConverter(new PrimitiveBooleanConverter());
        registerConverter(new PrimitiveIntArrayConverter());
    }

    public AbstractConfiguration() {

        options.helpAsEntry = false;
        options.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss+SSS");
        options.autoSave = true;
        initConverters();
    }

    public AbstractConfiguration(Map m) {
        this();
        java.util.Map.Entry e;
        for (Iterator i = m.entrySet().iterator(); i.hasNext(); setProperty((String) e.getKey(), e.getValue())) {
            e = (java.util.Map.Entry) i.next();
        }
        options.autoSave = true;
    }

    public AbstractConfiguration(File file)
            throws IOException, ParseException {
        this();
        options.setFile(file);
        load();
        options.autoSave = true;
    }

//    @Override
    public abstract void load() throws ConfigurationException ;

    
//    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        Boolean s = (Boolean) getPropertyValueImpl(key);
        return s != null ? s : defaultValue;
    }

//    @Override
    public char getChar(String key, char defaultValue) {
        Character s = (Character) getPropertyValueImpl(key);
        return s != null ? s : defaultValue;
    }

//    @Override
    public void setDate(String key, Date date) {
        setProperty(key, date);
    }

//    @Override
    public void setTime(String key, Time date) {
        setProperty(key, date);
//        props.put(key, date ==null ? null : date);
    }

//    @Override
    public Date getDate(String key) {
        return (Date) getPropertyValueImpl(key);
    }

//    @Override
    public Date getDate(String key, Date defaultValue) {
        Date r = (Date) getPropertyValueImpl(key);
        return r == null ? defaultValue : r;
    }

//    @Override
    public Time getTime(String key) {
        return (Time) getPropertyValueImpl(key);
    }

//    @Override
    public Time getTime(String key, Time defaultValue) {
        Time r = (Time) getPropertyValueImpl(key);
        return r == null ? defaultValue : r;
    }

//    @Override
    public double getDouble(String key, double defaultValue) {
        Number s = (Number) getPropertyValueImpl(key);
        return s != null ? s.doubleValue() : defaultValue;
    }

//    @Override
    public float getFloat(String key, float defaultValue) {
        Number s = (Number) getPropertyValueImpl(key);
        return s != null ? s.floatValue() : defaultValue;
    }

//    @Override
    public int getInt(String key, int defaultValue) {
        Number s = (Number) getPropertyValueImpl(key);
        return s != null ? s.intValue() : defaultValue;
    }

//    @Override
    public Font getFont(String key, Font defaultValue) {
        Font s = (Font) getPropertyValueImpl(key);
        return s != null ? s : defaultValue;
    }

//    @Override
    public Color getColor(String key, Color defaultValue) {
        Color s = (Color) getPropertyValueImpl(key);
        return s != null ? s : defaultValue;
    }

//    @Override
    public Locale getLocale(String key) {
        return (Locale) getPropertyValueImpl(key);
    }

//    @Override
    public void setLocale(String key, Locale locale) {
        setProperty(key, locale);
    }

//    @Override
    public Locale getLocale(String key, Locale defaultValue) {
        Locale r = (Locale) getPropertyValueImpl(key);
        return r == null ? defaultValue : r;
    }

//    @Override
    public long getLong(String key, long defaultValue) {
        Number s = (Number) getPropertyValueImpl(key);
        return s != null ? s.longValue() : defaultValue;
    }

//    @Override
    public abstract void deleteTree(String key, boolean deleteRoot) ;

//    @Override
    public abstract String[] getChildrenKeys(String key, boolean recurse) ;

//    @Override
    public Object getProperty(String key) {
        return getPropertyValueImpl(key);
    }

//    @Override
    public String getPropertyComments(String key) {
        
        if (getPropertyDescImpl(key)==null) {
            String s = null;//getHelper().get(key, false);
            if (s != null) {
                setPropertyDescImpl(key, s);
            } else {
                setPropertyDescImpl(key, key);
            }
            return s;
        } else {
            return getPropertyDescImpl(key);
        }
    }

//    @Override
    public String getPropertyType(String key) {
        if (!containsKey(key)) {
            return null;
        } else {
            String v = getPropertyTypeImpl(key);
            return v != null ? v : "string";
        }
    }

//    @Override
    public short getShort(String key, short defaultValue) {
        Number s = (Number) getPropertyValueImpl(key);
        return s != null ? s.shortValue() : defaultValue;
    }

//    @Override
    public String getString(String key) {
        return (String) getPropertyValueImpl(key);
    }

//    @Override
    public int[] getIntArray(String key) {
        return (int[]) getPropertyValueImpl(key);
    }

//    @Override
    public Object getObject(String key, Object defaultValue) {
        Object r = getPropertyValueImpl(key);
        return r == null ? defaultValue : r;
    }

//    @Override
    public String getString(String key, String defaultValue) {
        String r = (String) getPropertyValueImpl(key);
        return r == null ? defaultValue : r;
    }

//    @Override
    public String[] getStringArray(String key, String[] defaultValue) {
        String[] r = (String[]) getPropertyValueImpl(key);
        return r == null ? defaultValue : r;
    }

//    @Override
    public Color[] getColorArray(String key, Color[] defaultValue) {
        Color[] r = (Color[]) getPropertyValueImpl(key);
        return r == null ? defaultValue : r;
    }

//    @Override
    public Color[] getColorArray(String key) {
        return (Color[]) getPropertyValueImpl(key);
    }

//    @Override
    public String[] getStringArray(String key) {
        return (String[]) getPropertyValueImpl(key);
    }

    public abstract Set<String> keySet() ;

    
//    @Override
    public Converter getConverter(String type) {
        Converter conv = typesToConverters.get(type);
        if (conv == null) {
            throw new RuntimeException("Unhandled type " + type);
        }
        return conv;
    }

//    @Override
    public Converter getConverter(Class someClass) {
        Converter conv = classesToConverters.get(someClass);
        if (conv == null) {
            throw new RuntimeException("Unhandled Class " + someClass);
        }
        return conv;
    }

//    @Override
    public Object getLoadedValue(String stringValue, String type) {
        try {
            if (stringValue == null) {
                return null;
            }

            // test if some specific converter is registred
            if (typesToConverters.containsKey(type)) {
                return getConverter(type).stringToObject(stringValue);
            }

//            boolean isArray = false;
            if (isArrayType(type)) {

                String elemtype = type.substring(0, type.length() - 3);
                String sep = "" + getArraySeparator(type);
                ArrayList<Object> v = new ArrayList<Object>(3);
                boolean lastIsSep = true;
                StringTokenizer t = new StringTokenizer(stringValue, sep, true);
                while (t.hasMoreTokens()) {
                    String x = t.nextToken();
                    if (x.equals(sep)) {
                        if (lastIsSep) {
                            v.add(null);
                        }
                        lastIsSep = true;
                    } else {
                        lastIsSep = false;
                        v.add(getConverter(elemtype).stringToObject(x));
                    }
                }
                Class c = getConverter(elemtype).getAcceptedClass();
                Object[] arr = (Object[]) Array.newInstance(c, v.size());
                return v.toArray(arr);

            } else {
                return getConverter(type).stringToObject(stringValue);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.toString());
        }
    }

 
    public synchronized void loadFromClass(Object classOrInstance) {
        boolean oldIsAutoSave = options.isAutoSave();
        options.setAutoSave(false);
        Object instance;
        Class instanceClass;
        if (classOrInstance instanceof Class) {
            instance = null;
            instanceClass = (Class) classOrInstance;
        } else {
            instance = classOrInstance;
            instanceClass = classOrInstance.getClass();
        }
        Field[] fields = instanceClass.getFields();
        for (Field field : fields) {
            try {
                if (Modifier.isPublic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
//                    Class fc = fields[i].getType();
                    String fn = field.getName();
                    Object fv = field.get(instance);
                    Field hf = null;
                    Field tf = null;
                    try {
                        hf = instanceClass.getField(fn + "__HELP");
                    } catch (Throwable e) {
                        //
                    }
                    try {
                        tf = instanceClass.getField(fn + "__TYPE");
                    } catch (Throwable e) {
                        //
                    }
                    String helpString = hf != null ? (String) hf.get(instance) : "NO_HELP_PROVIDED";
                    String typeField = tf != null ? (String) tf.get(instance) : null;
                    setProperty(fn, fv, typeField);
                    setPropertyComments(fn, helpString);
                }
            } catch (Throwable ee) {
                ee.printStackTrace();
            }
        }

        options.setAutoSave(oldIsAutoSave);
        autoSaveIfNecessary();
    }

//    @Override
    public void setBoolean(String key, boolean value) {
        setProperty(key, value ? Boolean.TRUE : Boolean.FALSE);
    }

//    @Override
    public void setChar(String key, char value) {
        setProperty(key, value);
    }

//    @Override
    public void setConfidential(String key, String confidential) {
        setProperty(key, confidential, "confidential");
    }

//    @Override
    public void setDouble(String key, double value) {
        setProperty(key, value);
    }

//    @Override
    public void setFloat(String key, float value) {
        setProperty(key, value);
    }

//    @Override
    public void setArray(String key, Object[] arr, char sep) {
        setProperty(key, arr, null, sep);
    }

//    @Override
    public void setIntArray(String key, int[] intArray) {
        setProperty(key, intArray);
    }

//    @Override
    public void setInt(String key, int value) {
        setProperty(key, value);
    }

//    @Override
    public void setLong(String key, long value) {
        setProperty(key, value);
    }

//    @Override
    public void setObfuscated(String key, String value) {
        setProperty(key, obfuscate(value), "obfuscated");
    }

//    @Override
    public void setProperty(String key, Object value) {
        setProperty(key, value, null);
    }

//    @Override
    public void setString(String key, String value) {
        setProperty(key, value, null);
    }

//    @Override
    public void setFont(String key, Font value) {
        setProperty(key, value, FONT_TYPE);
    }

//    @Override
    public void setColor(String key, Color value) {
        setProperty(key, value, COLOR_TYPE);
    }

//    @Override
    public void setStringArray(String key, String[] value, char sep) {
        setProperty(key, value, null, sep);
    }

//    @Override
    public void setColorArray(String key, Color[] value) {
        setProperty(key, value, null, ';');
    }

    private void setProperty(String key, Object value, String type) {
        setProperty(key, value, type, ';');
    }

//    @Override
    public boolean isSupportedClass(Class aClass) {
        if (classesToConverters.containsKey(aClass)) {
            return true;
        } else if (aClass.isArray()) {
            Class elementClass = aClass.getComponentType();
            if (classesToConverters.containsKey(elementClass)) {
                return true;
            }
        }
        return false;
    }

    private void setProperty(String key, Object value, String type, char sep) {
        if (value == null) {
            if (!containsKey(key)) {
                return;
            }
            remove(key);
        } else {
            if (type == null) {
                if (classesToConverters.containsKey(value.getClass())) {
                    type = getConverter(value.getClass()).getAcceptedType();
                } else if (value.getClass().isArray()) {
                    Class elementClass = value.getClass().getComponentType();
                    if (elementClass == null) {
                        throw new RuntimeException("exprected an array");
                    }
                    type = getConverter(elementClass).getAcceptedType();
                    type = type + "[" + sep + "]";
                    if (equals(value, getPropertyValueImpl(key)) && equals(type, getPropertyType(key))) {
                        return;
                    }
                } else {
                    type = getConverter(value.getClass()).getAcceptedType();
                }
            }
            if (equals(value, getPropertyValueImpl(key)) && equals(type, getPropertyType(key))) {
                return;
            }
            setPropertyValueImpl(key, value, type);
        }
        autoSaveIfNecessary();
    }
    

    private void autoSaveIfNecessary() {
        try {
            if (options.isAutoSave() && options.getFile() != null) {
                store();
            }
        } catch (Exception e) {
            System.err.println("Could not autosave Configuration to " + options.file.getAbsolutePath());
        }
    }

//    @Override
    public void setPropertyComments(String key, String comments) {
        if (equals(getPropertyDescImpl(key), comments)) {
            return;
        }
        if (comments == null || "".equals(comments)) {
            setPropertyDescImpl(key,null);
        } else {
            setPropertyDescImpl(key, comments);
        }
        autoSaveIfNecessary();
    }

//    @Override
    public void setShort(String key, short value) {
        setProperty(key, value);
    }

//    @Override
    public String getStoredValue(Object val, String type) {
        if (val == null) {
            return null;
        }
        if (isArrayType(type)) {
            Object[] valArr = (Object[]) val;
            char sep = getArraySeparator(type);
            String baseType = getBaseType(type);
            StringBuilder sb = new StringBuilder();
            Converter conv = getConverter(baseType);
            for (int i = 0; i < valArr.length; i++) {
                if (i > 0) {
                    sb.append(sep);
                }
                sb.append(conv.objectToString(valArr[i]));
            }
            if (isSecureType(type)) {
                return lightWeightEncrypt(sb.toString());
            } else {
                return sb.toString();
            }
        } else {
            Converter conv = getConverter(type);
            if (isSecureType(type)) {
                return lightWeightEncrypt(conv.objectToString(val));
            } else {
                return conv.objectToString(val);
            }
        }
    }

    private static boolean isArrayType(String type) {
        return (type.length() > 3 && type.charAt(type.length() - 3) == '[' && type.charAt(type.length() - 1) == ']');
    }

    private static char getArraySeparator(String type) {
        return (type.charAt(type.length() - 2));
    }

    private static boolean isSecureType(String type) {
        return (type.length() > 4 && type.charAt(type.length() - 3) == '[' && type.charAt(type.length() - 1) == ']' && type.charAt(type.length() - 4) == '!') || (type.charAt(type.length() - 1) == '!');
    }

    private static String getBaseType(String type) {
        if ((type.length() > 3 && type.charAt(type.length() - 3) == '[' && type.charAt(type.length() - 1) == ']')) {
            type = type.substring(0, type.length() - 3);
        }
        if (type.length() > 1 && type.charAt(type.length() - 1) == '!') {
            type = type.substring(0, type.length() - 1);
        }
        return type;
    }

    

    public void storeToClass(Object classOrInstance) {
        Object instance;
        Class instanceClass;
        if (classOrInstance instanceof Class) {
            instance = null;
            instanceClass = (Class) classOrInstance;
        } else {
            instance = classOrInstance;
            instanceClass = classOrInstance.getClass();
        }
        Field[] fields = instanceClass.getFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
//                Class fc = fields[i].getType();
                String fn = field.getName();
                Object value = getProperty(fn);
                if (value != null) {
                    try {
                        field.set(instance, getProperty(fn));
                    } catch (Exception e) {
                        throw new RuntimeException(e + " (" + fn + ")");
                    }
                }
            }
        }

    }

    public abstract Map toMap() ;

    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (!o1.getClass().isArray()) {
            return o1.equals(o2);
        }
        Class clz = o1.getClass().getComponentType();
        if (clz.isPrimitive()) {
            System.err.println("could not compare primitive arrays");
            return false;
        }
        Object[] o1arr = (Object[]) o1;
        Object[] o2arr = (Object[]) o2;
        if (o1arr.length != o2arr.length) {
            return false;
        } else {
            Collection o1coll = Arrays.asList(o1arr);
            Collection o2coll = Arrays.asList(o2arr);
            return o1coll.equals(o2coll);
        }
    }

    
//    @Override
//    public Iterator iterator() {
//        return props.entrySet().iterator();
//    }

    public static String toSpecialString(String theString, boolean escapeSpace) {
        return toSpecialString(theString, "=: \t\r\n\f#!", escapeSpace);
    }

    public static String toSpecialString(String theString, String specialSaveChars, boolean escapeSpace) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len * 2);
        for (int x = 0; x < len; x++) {
            char aChar = theString.charAt(x);
            switch (aChar) {
                case 32: // ' '
                    if (x == 0 || escapeSpace) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(' ');
                    break;

                case 92: // '\\'
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    break;

                case 9: // '\t'
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;

                case 10: // '\n'
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;

                case 13: // '\r'
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;

                case 12: // '\f'
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;

                default:
                    if (aChar < ' ' || aChar > '~') {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex(aChar >> 12 & 0xf));
                        outBuffer.append(toHex(aChar >> 8 & 0xf));
                        outBuffer.append(toHex(aChar >> 4 & 0xf));
                        outBuffer.append(toHex(aChar & 0xf));
                        break;
                    }
                    if (specialSaveChars.indexOf(aChar) != -1) {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(aChar);
                    break;
            }
        }

        return outBuffer.toString();
    }

    public static String fromSpecialString(String theString) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len;) {
            char aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case 48: // '0'
                            case 49: // '1'
                            case 50: // '2'
                            case 51: // '3'
                            case 52: // '4'
                            case 53: // '5'
                            case 54: // '6'
                            case 55: // '7'
                            case 56: // '8'
                            case 57: // '9'
                                value = ((value << 4) + aChar) - 48;
                                break;

                            case 97: // 'a'
                            case 98: // 'b'
                            case 99: // 'c'
                            case 100: // 'd'
                            case 101: // 'e'
                            case 102: // 'f'
                                value = ((value << 4) + 10 + aChar) - 97;
                                break;

                            case 65: // 'A'
                            case 66: // 'B'
                            case 67: // 'C'
                            case 68: // 'D'
                            case 69: // 'E'
                            case 70: // 'F'
                                value = ((value << 4) + 10 + aChar) - 65;
                                break;

                            case 58: // ':'
                            case 59: // ';'
                            case 60: // '<'
                            case 61: // '='
                            case 62: // '>'
                            case 63: // '?'
                            case 64: // '@'
                            case 71: // 'G'
                            case 72: // 'H'
                            case 73: // 'I'
                            case 74: // 'J'
                            case 75: // 'K'
                            case 76: // 'L'
                            case 77: // 'M'
                            case 78: // 'N'
                            case 79: // 'O'
                            case 80: // 'P'
                            case 81: // 'Q'
                            case 82: // 'R'
                            case 83: // 'S'
                            case 84: // 'T'
                            case 85: // 'U'
                            case 86: // 'V'
                            case 87: // 'W'
                            case 88: // 'X'
                            case 89: // 'Y'
                            case 90: // 'Z'
                            case 91: // '['
                            case 92: // '\\'
                            case 93: // ']'
                            case 94: // '^'
                            case 95: // '_'
                            case 96: // '`'
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                        }
                    }

                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }

        return outBuffer.toString();
    }

    public static char toHex(int nibble) {
        return hexDigit[nibble & 0xf];
    }

    public static Locale getLocaleFromString(String locale) {
        StringTokenizer st = new StringTokenizer(locale, "_ ,;:/");
        int tcount = st.countTokens();
        String a1;
        String a2 = "";
        String a3 = "";
        if (tcount < 1 || tcount > 3) {
            throw new RuntimeException("bad locale : " + locale);
        }
        if (st.hasMoreTokens()) {
            a1 = st.nextToken();
            if (st.hasMoreTokens()) {
                a2 = st.nextToken();
                if (st.hasMoreTokens()) {
                    a2 = st.nextToken();
                    if (st.hasMoreTokens()) {
                        throw new RuntimeException("bad locale : " + locale);
                    }
                }
            }
        } else {
            throw new RuntimeException("bad locale : " + locale);
        }
        return new Locale(a1, a2, a3);
    }

    public static String lightWeightDecrypt(String data) {
        return new String(Base64.decode(data));
    }

    public static String lightWeightEncrypt(String data) {
        return Base64.encode(data.getBytes());
    }

    public static String obfuscate(String plain) {
        int o = 0;
        for (int i = 0; i < plain.length(); i++) {
            o = 31 * o + plain.charAt(i);
        }

        return "" + o;
    }

// @Override
    public void remove(String key) {
        setPropertyDescImpl(key, null);
        setPropertyValueImpl(key,null, null);
    }

    protected abstract Object getPropertyValueImpl(String key);//{props.get(key);}
    protected abstract void setPropertyValueImpl(String key, Object value, String type);//{props.get(key);}
    protected abstract String getPropertyTypeImpl(String key);
    protected abstract void setPropertyDescImpl(String key,String type);
    protected abstract String getPropertyDescImpl(String key);
}
