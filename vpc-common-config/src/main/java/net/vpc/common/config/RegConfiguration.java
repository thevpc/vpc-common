package net.vpc.common.config;

import java.io.*;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;
import java.util.prefs.Preferences;

public class RegConfiguration extends AbstractConfiguration implements Configuration {

    private static final boolean DEBUG = true;
    protected boolean infoFileLoaded = false;
    protected String installationUser = null;
    protected boolean doEncryptFlag = false;

    public static enum StoreType {

        SYSTEM_PREFERENCES_IMPL,
        USER_PREFERENCES_IMPL,
        USER_DIR_FILE_IMPL,
        USER_HOME_FILE_IMPL
    }
    private StoreType storeType = StoreType.SYSTEM_PREFERENCES_IMPL;
    private RegConfigurationStoreFactory systemRoot;
    private String root;

    public RegConfiguration(String root, StoreType storeType, boolean secure) {
        this.root = root;
        this.storeType = storeType;
        this.doEncryptFlag = secure;
    }

    protected void close() {
    }

    private String encrypt(String s) {
        String x = doEncryptFlag ? (s == null ? null : Base64.encode(s.getBytes())) : s;
        if (x != null && x.endsWith("\n")) {
            x = x.substring(0, x.length() - 1);
        }
        return x;
    }

    private String decrypt(String s) {
        return doEncryptFlag ? (s == null ? null : new String(Base64.decode(s))) : s;
    }

    private static String nonull(String s) {
        return s == null ? "" : s;
    }

//    public void setString(String param_name, String value) {
//        checkReadOnlyProperty(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        pref.putString(encrypt(param_name), encrypt(value));
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").setString("+param_name+","+value+")");
//        }
//    }
//
//    public String getString(String param_name, String value) {
//        RegConfigurationStoreFactory pref = getRoot();
//        String ret = decrypt(pref.getString(encrypt(param_name), encrypt(value)));
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getString("+param_name+","+value+")="+ret);
//        }
//        return ret;
//    }
//
//    public void setInt(String param_name, int value) {
//        checkReadOnlyProperty(param_name);
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        if (doEncryptFlag) {
//            pref.putString(name, encrypt(String.valueOf(value)));
//        } else {
//            pref.putInt(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").setInt("+param_name+","+value+")");
//        }
//    }
//
//    public int getInt(String param_name, int value) {
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        int ret = 0;
//        if (doEncryptFlag) {
//            String v = decrypt(pref.getString(name, null));
//            ret = ((v == null) ? value : new Integer(v).intValue());
//        } else {
//            ret = pref.getInt(name, value);
//        }
////        if(DEBUG){
////            System.out.println("AppInfos("+registryId+").getInt("+param_name+","+value+")="+ret);
////        }
//        return ret;
//    }
//
//    public void setBoolean(String param_name, boolean value) {
//        checkReadOnlyProperty(param_name);
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        if (doEncryptFlag) {
//            pref.putString(name, encrypt(value ? "true" : "false"));
//        } else {
//            pref.putBoolean(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").setBoolean("+param_name+","+value+")");
//        }
//    }
//
//    public boolean getBoolean(String param_name, boolean value) {
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        boolean ret = true;
//        if (doEncryptFlag) {
//            ret = new Boolean(decrypt(pref.getString(name, null))).booleanValue();
//        } else {
//            ret = pref.getBoolean(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getBoolean("+param_name+","+value+")="+ret);
//        }
//        return ret;
//    }
//
//    public void setByteArray(String param_name, byte[] value) {
//        checkReadOnlyProperty(param_name);
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        pref.putByteArray(name, value);
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").setByteArray("+param_name+","+new String(value)+")");
//        }
//    }
//
//    public byte[] getByteArray(String param_name, byte[] value) {
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        byte[] ret = pref.getByteArray(name, value);
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getByteArray("+param_name+","+new String(value)+")="+new String(ret));
//        }
//        return ret;
//    }
//
//    public void setDouble(String param_name, double value) {
//        checkReadOnlyProperty(param_name);
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        if (doEncryptFlag) {
//            pref.putString(name, encrypt("" + value));
//        } else {
//            pref.putDouble(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getDouble("+param_name+","+value+")");
//        }
//    }
//
//    public double getDouble(String param_name, double value) {
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        double ret = 0;
//        if (doEncryptFlag) {
//            String v = decrypt(pref.getString(name, null));
//            ret = (v == null ? value : new Double(v).doubleValue());
//        } else {
//            ret = pref.getDouble(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getDouble("+param_name+","+value+")="+ret);
//        }
//        return ret;
//    }
//
//    public void setFloat(String param_name, float value) {
//        checkReadOnlyProperty(param_name);
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        if (doEncryptFlag) {
//            pref.putString(name, encrypt("" + value));
//        } else {
//            pref.putFloat(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").setFloat("+param_name+","+value+")");
//        }
//    }
//
//    public float getFloat(String param_name, float value) {
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        float ret = 0f;
//        if (doEncryptFlag) {
//            String v = decrypt(pref.getString(name, null));
//            ret = (v == null ? value : new Float(v).floatValue());
//        } else {
//            ret = pref.getFloat(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getFloat("+param_name+","+value+")="+ret);
//        }
//        return ret;
//    }
//
//    public void setLong(String param_name, long value) {
//        checkReadOnlyProperty(param_name);
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        if (doEncryptFlag) {
//            pref.putString(name, encrypt("" + value));
//        } else {
//            pref.putLong(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").setLong("+param_name+","+value+")");
//        }
//    }
//
//    public long getLong(String param_name, long value) {
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        long ret = 0;
//        if (doEncryptFlag) {
//            String v = decrypt(pref.getString(name, null));
//            ret = (v == null ? value : new Long(v).longValue());
//        } else {
//            ret = pref.getLong(name, value);
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getLong("+param_name+","+value+")="+ret);
//        }
//        return ret;
//    }
//
//    public void setDate(String param_name, Date value) {
//        checkReadOnlyProperty(param_name);
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        if (doEncryptFlag) {
//            pref.putString(name, encrypt("" + value.getTime()));
//        } else {
//            pref.putLong(name, value == null ? -1 : value.getTime());
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getDate("+param_name+","+value+")");
//        }
//    }
//
//    public Date getDate(String param_name, Date value) {
//        String name = encrypt(param_name);
//        RegConfigurationStoreFactory pref = getRoot();
//        Date ret = null;
//        if (doEncryptFlag) {
//            String d = pref.getString(name, null);
//            if (d == null) {
//                ret = value;
//            } else {
//                ret = new Date(new Long(decrypt(d)).longValue());
//            }
//        } else {
//            long l = pref.getLong(name, -1);
//            ret = (l == -1 ? null : new Date(l));
//        }
//        if (DEBUG) {
//            //System.out.println("AppInfos("+registryId+").getFloat("+param_name+","+value+")="+ret);
//        }
//        return ret;
//    }

    protected RegConfigurationStoreFactory getRoot() {

        if (systemRoot == null) {
            String fileName = new BigInteger(encrypt(root).getBytes()).toString(16).toUpperCase();
            StringBuilder ss = new StringBuilder();
            int c = 0;
            for (char cc : fileName.toCharArray()) {
                if (cc != '=') {
                    if (c > 0 && c % 8 == 0) {
                        ss.append("-");
                    }
                    ss.append(Character.toUpperCase(cc));
                    c++;
                }
            }
            fileName = ss.toString() + ".pif";
            switch (storeType) {
                case USER_PREFERENCES_IMPL: {
                    systemRoot = new RegConfigurationPreferencesStoreFactory(Preferences.userRoot().node(fileName));
                    break;
                }
                case USER_DIR_FILE_IMPL: {
                    systemRoot = new RegConfigurationPropertiesStoreFactory(new File(System.getProperty("user.dir") + "/.java-apps/" + fileName));
                    break;
                }
                case USER_HOME_FILE_IMPL: {
                    systemRoot = new RegConfigurationPropertiesStoreFactory(new File(System.getProperty("user.home") + "/.java-apps/" + fileName));
                    break;
                }
                case SYSTEM_PREFERENCES_IMPL:
                default: {
                    systemRoot = new RegConfigurationPreferencesStoreFactory(Preferences.systemRoot().node(fileName));
                    break;
                }
            }
        }
        return systemRoot;
    }

    public void checkReadOnlyProperty(String name) {
        // do nothing
//        if(!installationInProgress){
//            if (
//                    REG_HOME.equals(name)
//                    || REG_DATE.equals(name)
//                    || REG_USER.equals(name)
//            ) {
//                throw new RuntimeException("This property is read only");
//            }
//        }
    }

    @Override
    public void deleteTree(String key, boolean deleteRoot) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getChildrenKeys(String key, boolean recurse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map toMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
        RegConfigurationStoreFactory root = getRoot();
        root.remove();
        infoFileLoaded = false;
    }

    @Override
    public void load() throws ConfigurationException {
    }

    public void store() throws IOException {
    }

    public boolean containsKey(String key) {
        RegConfigurationStoreFactory pref = getRoot();
        return pref.getString(encrypt(key + ".value"), null) != null;
    }

    @Override
    protected String getPropertyDescImpl(String key) {
        RegConfigurationStoreFactory pref = getRoot();
        return decrypt(pref.getString(encrypt(key + ".desc"), null));
    }

    @Override
    protected String getPropertyTypeImpl(String key) {
        RegConfigurationStoreFactory pref = getRoot();
        return decrypt(pref.getString(encrypt(key + ".type"), null));
    }

    @Override
    protected void setPropertyDescImpl(String key, String type) {
        RegConfigurationStoreFactory pref = getRoot();
        pref.putString(encrypt(key + ".desc"), encrypt(type));
    }

    @Override
    protected Object getPropertyValueImpl(String key) {
        RegConfigurationStoreFactory pref = getRoot();
        String stringValue = decrypt(pref.getString(encrypt(key + ".value"), null));
        if (stringValue == null) {
            return null;
        }
        return getLoadedValue(stringValue, getPropertyType(key));
    }

    @Override
    protected void setPropertyValueImpl(String key, Object value, String type) {
        RegConfigurationStoreFactory pref = getRoot();
        if (value == null) {
            pref.putString(encrypt(key + ".value"), null);
            pref.putString(encrypt(key + ".type"), null);
        } else {
            pref.putString(encrypt(key + ".value"), encrypt(getStoredValue(value, type)));
            pref.putString(encrypt(key + ".type"), encrypt(type));
        }

    }
}
