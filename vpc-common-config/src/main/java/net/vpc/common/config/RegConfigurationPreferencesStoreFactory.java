package net.vpc.common.config;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * User: taha
 * Date: 8 janv. 2005
 * Time: 22:51:26
 */
public class RegConfigurationPreferencesStoreFactory implements RegConfigurationStoreFactory{
    Preferences root;

    public RegConfigurationPreferencesStoreFactory(Preferences root) {
        this.root = root;
    }

    public long getLong(String key, long def) {
        return root.getLong(key, def);
    }

    public void putLong(String key, long value) {
        root.putLong(key, value);
        try {
            root.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public double getDouble(String key, double def) {
        return root.getDouble(key, def);
    }

    public void putDouble(String key, double value) {
        root.putDouble(key, value);
        try {
            root.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public float getFloat(String key, float def) {
        return root.getFloat(key, def);
    }

    public void putFloat(String key, float value) {
        root.putFloat(key, value);
        try {
            root.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public int getInt(String key, int def) {
        return root.getInt(key, def);
    }

    public void putInt(String key, int value) {
        root.putLong(key, value);
        try {
            root.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public String getString(String key, String def) {
        return root.get(key, def);
    }

    public void putString(String key, String value) {
        root.put(key, value);
        try {
            root.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public boolean getBoolean(String key, boolean def) {
        return root.getBoolean(key, def);
    }

    public void putBoolean(String key, boolean value) {
        root.putBoolean(key, value);
        try {
            root.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public byte[] getByteArray(String key, byte[] def) {
        return root.getByteArray(key, def);
    }

    public void putByteArray(String key, byte[] value) {
        root.putByteArray(key, value);
        try {
            root.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void remove() {
        try {
            root.removeNode();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }
}
