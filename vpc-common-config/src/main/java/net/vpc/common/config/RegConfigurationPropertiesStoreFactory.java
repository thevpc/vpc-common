package net.vpc.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * User: taha
 * Date: 8 janv. 2005
 * Time: 23:17:08
 */
public class RegConfigurationPropertiesStoreFactory implements RegConfigurationStoreFactory {
    private File file;
    private Properties cache;

    public RegConfigurationPropertiesStoreFactory(File file) {
        this.file = file;
    }

    public Properties getProperties() {
        if(cache==null){
        	
            if(!file.exists()){
               file.getParentFile().mkdirs();
               cache=new Properties();
               return cache;
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                Properties p = new Properties();
                p.load(fis);
                cache=p;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }finally{
                if(fis!=null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        return cache;
    }

    private void flush(){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            cache.store(fos,"AppInfoPropertiesStoreFactory");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally{
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public String getString(String key, String def) {
        String s=getProperties().getProperty(key);
        if(s==null){
            return def;
        }
        return s;
    }

    public void putString(String key, String value) {
        if (value==null) {
            getProperties().remove(key);
        }else{
            getProperties().setProperty(key,value);
        }
        flush();
    }

    public long getLong(String key, long def) {
        return Long.parseLong(getString(key,String.valueOf(def)));
    }

    public void putLong(String key, long value) {
        putString(key,String.valueOf(value));
    }

    public int getInt(String key, int def) {
        return Integer.parseInt(getString(key,String.valueOf(def)));
    }

    public void putInt(String key, int value) {
        putString(key,String.valueOf(value));
    }

    public double getDouble(String key, double def) {
        return Double.parseDouble(getString(key,String.valueOf(def)));
    }

    public void putDouble(String key, double value) {
        putString(key,String.valueOf(value));
    }

    public float getFloat(String key, float def) {
        return Float.parseFloat(getString(key,String.valueOf(def)));
    }

    public void putFloat(String key, float value) {
        putString(key,String.valueOf(value));
    }

    public boolean getBoolean(String key, boolean def) {
        return new Boolean(getString(key,String.valueOf(def))).booleanValue();
    }

    public void putBoolean(String key, boolean value) {
        putString(key,String.valueOf(value));
    }

    private byte[] __str2bytes(String s){
        return (s==null || s.length()==0)?null: s.getBytes();
    }

    private String __bytes2str(byte[] s){
        return s==null ? "": new String(s);
    }

    public byte[] getByteArray(String key, byte[] def) {
        return __str2bytes(getString(key,__bytes2str(def)));
    }

    public void putByteArray(String key, byte[] value) {
        putString(key,__bytes2str(value));
    }

    public void remove() {
        file.delete();
    }
}
