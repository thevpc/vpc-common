package net.thevpc.common.log;

public interface LogConfig {
//	void addConfigurationChangeListener(ConfigurationChangeListener listener);
//	void removeConfigurationChangeListener(ConfigurationChangeListener listener);
	public int getInt(String key, int defaultValue);
	public long getLong(String key, long defaultValue);
	public String getString(String key, String defaultValue);
	public String getString(String key);
	public boolean getBoolean(String key, boolean defaultValue);
	public void setInt(String key, int defaultValue);
	public void setLong(String key, long defaultValue);
	public void setString(String key, String defaultValue);
	public void setBoolean(String key, boolean defaultValue);
}
