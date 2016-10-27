package net.vpc.common.config;

/**
 * User: taha
 * Date: 8 janv. 2005
 * Time: 22:46:12
 */
public interface RegConfigurationStoreFactory {
    public abstract long getLong(String key, long def);
    public abstract void putLong(String key, long value);
    public abstract double getDouble(String key, double def);
    public abstract void putDouble(String key, double value);
    public abstract float getFloat(String key, float def);
    public abstract void putFloat(String key, float value);
    public abstract int getInt(String key, int def);
    public abstract void putInt(String key, int value);
    public abstract String getString(String key, String def);
    public abstract void putString(String key, String value);
    public abstract boolean getBoolean(String key, boolean def);
    public abstract void putBoolean(String key, boolean value);

    public abstract byte[] getByteArray(String key, byte[] def);
    public abstract void putByteArray(String key, byte[] value);

    public abstract void remove();
}
