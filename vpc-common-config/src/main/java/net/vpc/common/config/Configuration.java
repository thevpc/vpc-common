package net.vpc.common.config;

import java.awt.*;
import java.io.*;
import java.sql.Time;
import java.util.*;

/**
 * @author Taha BEN SALAH (thevpc@walla.com) @creationtime 13 juil. 2006
 * 22:14:21
 */
public interface Configuration extends Serializable {

    String BOOLEAN_TYPE = "boolean";
    String COLOR_TYPE = "color";
    String COMMENTS_SUFFIX = "$comments";
    String CONFIDENTIAL_TYPE = "confidential";
    String CONFIGURATION_CHANGED = "CONFIGURATION_CHANGED";
    String DATE_TYPE = "date";
    String DOUBLE_TYPE = "double";
    String FLOAT_TYPE = "float";
    String FONT_TYPE = "font";
    String INT_TYPE = "int";
    String LOCALE_TYPE = "locale";
    String LONG_TYPE = "long";
    String OBFUSCATED_TYPE = "obfuscated";
    String SHORT_TYPE = "short";
    String STRING_TYPE = "string";
    String TIME_TYPE = "time";
    String TYPE_SUFFIX = "$type";

    void clear();

    boolean containsKey(String key);

    void deleteTree(String key, boolean deleteRoot);

    boolean getBoolean(String key, boolean defaultValue);

    char getChar(String key, char defaultValue);

    String[] getChildrenKeys(String key, boolean recurse);

    Color getColor(String key, Color defaultValue);

    Color[] getColorArray(String key, Color[] defaultValue);

    Color[] getColorArray(String key);

    Converter getConverter(String type);

    Converter getConverter(Class someClass);

    Date getDate(String key);

    Date getDate(String key, Date defaultValue);

    double getDouble(String key, double defaultValue);

    float getFloat(String key, float defaultValue);

    Font getFont(String key, Font defaultValue);

    int getInt(String key, int defaultValue);

    int[] getIntArray(String key);

    Object getLoadedValue(String stringValue, String type);

    Locale getLocale(String key);

    Locale getLocale(String key, Locale defaultValue);

    long getLong(String key, long defaultValue);

    Object getObject(String key, Object defaultValue);

    ConfigOptions getOptions();

    Object getProperty(String key);

    String getPropertyComments(String key);

    String getPropertyType(String key);

    short getShort(String key, short defaultValue);

    String getStoredValue(Object val, String type);

    String getString(String key);

    String getString(String key, String defaultValue);

    String[] getStringArray(String key, String[] defaultValue);

    String[] getStringArray(String key);

    Time getTime(String key);

    Time getTime(String key, Time defaultValue);

    boolean isSupportedClass(Class aClass);

    Set<String> keySet();

    void load() throws ConfigurationException;

    void remove(String key);

    void setArray(String key, Object[] arr, char sep);

    void setBoolean(String key, boolean value);

    void setChar(String key, char value);

    void setColor(String key, Color value);

    void setColorArray(String key, Color[] value);

    void setConfidential(String key, String confidential);

    void setDate(String key, Date date);

    void setDouble(String key, double value);

    void setFloat(String key, float value);

    void setFont(String key, Font value);

    void setInt(String key, int value);

    void setIntArray(String key, int[] intArray);

    void setLocale(String key, Locale locale);

    void setLong(String key, long value);

    void setObfuscated(String key, String value);

    void setProperty(String key, Object value);

    void setPropertyComments(String key, String comments);

    void setShort(String key, short value);

    void setString(String key, String value);

    void setStringArray(String key, String[] value, char sep);

    void setTime(String key, Time date);

    void store() throws IOException;

    Map toMap();
}
