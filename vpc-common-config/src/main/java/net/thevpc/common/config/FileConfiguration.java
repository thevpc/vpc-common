package net.thevpc.common.config;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Taha BEN SALAH (thevpc@walla.com) @creationtime 13 juil. 2006
 * 22:14:21
 */
public class FileConfiguration extends AbstractConfiguration {

    private static boolean static_persistenceEnabled = true;
    private Map<String, Object> props = new HashMap<String, Object>();
    private Map<String, String> descs = new HashMap<String, String>();
    private Map<String, String> types = new HashMap<String, String>();
    private ConfigOptions options = new ConfigOptions();

    @Override
    public ConfigOptions getOptions() {
        return options;
    }

    @Override
    public void clear() {
        props.clear();
        descs.clear();
        types.clear();
    }

    public static void setPersistenceEnabled(boolean e) {
        static_persistenceEnabled = e;
    }

    public static boolean isPersistenceEnabled() {
        return static_persistenceEnabled;
    }

    public FileConfiguration() {

        options.helpAsEntry = false;
        options.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss+SSS");
        options.autoSave = true;
    }

    public FileConfiguration(Map m) {
        this();
        java.util.Map.Entry e;
        for (Iterator i = m.entrySet().iterator(); i.hasNext(); setProperty((String) e.getKey(), e.getValue())) {
            e = (java.util.Map.Entry) i.next();
        }
        options.autoSave = true;
    }

    public FileConfiguration(File file)
            throws IOException, ParseException {
        this();
        options.setFile(file);
        load();
        options.autoSave = true;
    }

    @Override
    public void load()
            throws ConfigurationException {
        if (options.file == null) {
            throw new ConfigurationException("No config file to load");
        } else if (!options.file.exists()) {
            boolean b = false;
            try {
                File p = options.file.getParentFile();
                if (p != null) {
                    p.mkdirs();
                }
                b = options.file.createNewFile();
            } catch (IOException e) {
                //
            }
            if (!b) {
                String canonicalPath = null;
                try {
                    canonicalPath = options.file.getCanonicalPath();
                } catch (IOException ex) {
                    canonicalPath = options.file.getAbsolutePath();
                }
                throw new ConfigurationException(canonicalPath + " does not exist and could not be created as a configuration file");
            }
        }
        try {
            load(options.file);
        } catch (IOException ex) {
            throw new ConfigurationException(ex);
        } catch (ParseException ex) {
            throw new ConfigurationException(ex);
        }
        System.out.println("Configuration loaded");
    }

    @Override
    public void store()
            throws IOException {
        if (isPersistenceEnabled() && options.file != null) {
            store(options.file, null);
        }
    }

    @Override
    public boolean containsKey(String key) {
        return props.containsKey(key);
    }

    @Override
    public synchronized void deleteTree(String key, boolean deleteRoot) {
        for (Iterator i = props.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String entryKey = (String) entry.getKey();
            if (key.equals(entryKey)) {
                if (deleteRoot) {
                    i.remove();
                }
            } else if (entryKey.startsWith(key + ".")) {
                i.remove();
            }
        }
    }

    @Override
    public String[] getChildrenKeys(String key, boolean recurse) {
        ArrayList<String> al = new ArrayList<String>();
        for (Iterator i = props.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String entryKey = (String) entry.getKey();
            if (entryKey.startsWith(key + ".")) {
                if (recurse || entryKey.substring(key.length() + 2).indexOf('.') < 0) {
                    al.add(entryKey);
                }
            }
        }
        return al.toArray(new String[al.size()]);
    }

    @Override
    public Set keySet() {
        TreeSet<String> ts = new TreeSet<String>();
        for (String k : props.keySet()) {
            if (!k.endsWith("$type") && !k.endsWith("$comments")) {
                ts.add(k);
            }
        }
        return ts;
    }

    public void load(File inFile)
            throws IOException, ParseException {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(inFile);
            p.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        postLoad(p);
    }

    private void postLoad(Properties p) {
        for (Iterator i = p.entrySet().iterator(); i.hasNext();) {
            java.util.Map.Entry e = (java.util.Map.Entry) i.next();
            String keyType = (String) e.getKey();
            String key;
            String type;
            int x = keyType.indexOf(36);
            if (x > 0) {
                key = keyType.substring(0, x);
                type = keyType.substring(x + 1);
            } else {
                key = keyType;
                type = "string";
            }
            String stringValue = (String) e.getValue();
            Object value = getLoadedValue(stringValue, type);
            props.put(key, value);
            if (type != null) {
                types.put(key, type);
            } else {
                types.remove(key);
            }
        }
    }

    public void load(InputStream in)
            throws IOException, ParseException {
        Properties p = new Properties();
        p.load(in);
        postLoad(p);
    }

    public void load(String inFile)
            throws IOException, ParseException {
        File fileInstance = new File(inFile);
        if (!fileInstance.exists()) {
            fileInstance.createNewFile();
        }
        load(fileInstance);
    }

    public void load(URL inURL)
            throws IOException, ParseException {
        load(inURL.openStream());
    }

   

//    private void autoSaveIfNecessary() {
//        try {
//            if (options.isAutoSave() && options.getFile() != null) {
//                store();
//            }
//        } catch (Exception e) {
//            System.err.println("Could not autosave Configuration to " + options.file.getAbsolutePath());
//        }
//    }
//
    public void store(File out, String header) throws IOException {
        FileOutputStream fos;
        fos = new FileOutputStream(out);
        try {
            store(fos, header);
        } finally {
            fos.close();
        }
    }

    public void store(OutputStream out, String header)
            throws IOException {
        BufferedWriter awriter = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));
        if (header != null) {
            awriter.write("# " + header);
            awriter.newLine();
        }
        awriter.write("# Last Modified " + (new Date()).toString());
        awriter.newLine();
        awriter.newLine();
        for (Iterator i = keySet().iterator(); i.hasNext(); awriter.flush()) {
            String key = (String) i.next();
            Object val = props.get(key);
            String comments = getOptions().isStoreComments() ? getPropertyComments(key) : null;
            String type = getPropertyType(key);
            //String stringVal = val != null ? "date".equals(type) ? getDateFormat().format((Date) val) : "confidential".equals(type) ? SecurityTools.lightWeightEncrypt((String) val) : "obfuscated".equals(type) ? (String) val : !"string".equals(type) && !"long".equals(type) && !"int".equals(type) && !"short".equals(type) && !"float".equals(type) && !"double".equals(type) && !"boolean".equals(type) && !"locale".equals(type) ? "cssa".equals(type) ? Utils.toString((String[]) val, ';') : (String) val : String.valueOf(val) : (String) null;
            String stringVal = getStoredValue(val, type);
            if (comments != null) {
                for (StringTokenizer st = new StringTokenizer(comments, "\n\r"); st.hasMoreTokens(); awriter.newLine()) {
                    awriter.write("# " + st.nextToken());
                }

                if (options.isHelpAsEntry()) {
                    awriter.write(toSpecialString(key + "$comments", true) + "=" + toSpecialString(comments, false));
                    awriter.newLine();
                }
            }
            awriter.write(toSpecialString(key, true) + (type != null ? "$" + toSpecialString(type, false) : "") + "=" + toSpecialString(stringVal, false));
            awriter.newLine();
            awriter.newLine();
        }

    }

    public void store(String outFile, String header)
            throws IOException {
        File fileInstance = new File(outFile);
        if (!fileInstance.exists()) {
            fileInstance.createNewFile();
        }
        FileOutputStream out = null;
        try {
            store(out = new FileOutputStream(outFile), header);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void store(URL out, String header)
            throws IOException {
        store(out.openConnection().getOutputStream(), header);
    }

    @Override
    public Map toMap() {
        return props;
    }

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

    @Override
    protected String getPropertyDescImpl(String key) {
        return descs.get(key);
    }

    @Override
    protected Object getPropertyValueImpl(String key) {
        return props.get(key);
    }

    @Override
    protected String getPropertyTypeImpl(String key) {
        return types.get(key);
    }

    @Override
    protected void setPropertyDescImpl(String key, String type) {
        if (type == null) {
            descs.remove(key);
        } else {
            descs.put(key, type);
        }
    }

    @Override
    protected void setPropertyValueImpl(String key, Object value, String type) {
        if (value == null) {
            props.remove(key);
            types.remove(key);
        } else {
            props.put(key, value);
            if(type!=null && type.equals("string")){
                types.remove(key);
            }else{
                types.put(key, type);
            }
        }
    }
    
    
}
