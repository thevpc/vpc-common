package net.thevpc.common.prs.plugin;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class PluginManagerCache implements Serializable {
    private static final long serialVersionUID = -1961088227968987704L;
    private Map<String, PluginCache> plugins = new HashMap<String, PluginCache>();
    private Map<String, ClassCache> classes = new HashMap<String, ClassCache>();
    private Set<String> allExtensions =new HashSet<String>();
    private List<ImplementationCache> allImplementations;
    private List<ImplementationCache> implementationFactories;
    private int implIndex;
    private int implFactIndex;

    public class ImplementationCache implements Serializable{
        private static final long serialVersionUID = -1961088227968987709L;
        private String implementationName;
        private String extensionName;
        private int index;
        private int priority;
        private PluginCache pluginCache;

        public ImplementationCache(PluginCache pluginCache,String implementationName, String extensionName, int index,int priority) {
            this.implementationName = implementationName;
            this.extensionName = extensionName;
            this.pluginCache = pluginCache;
            this.index = index;
            this.priority = priority;
        }

        public int getPriority() {
            return priority;
        }
        
        public String getImplementationName() {
            return implementationName;
        }
        public String getExtensionName() {
            return extensionName;
        }

        public int getIndex() {
            return index;
        }

        public PluginCache getPluginCache() {
            return pluginCache;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ImplementationCache that = (ImplementationCache) o;

            if (index != that.index) return false;
            if (!implementationName.equals(that.implementationName)) return false;
            if (!extensionName.equals(that.extensionName)) return false;
            if (!pluginCache.equals(that.pluginCache)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = implementationName==null?0:implementationName.hashCode();
            result = 31 * result + (extensionName==null?0:extensionName.hashCode());
            result = 31 * result + index;
            result = 31 * result + (pluginCache==null?0:pluginCache.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "ImplementationCache{" + "implementationName=" + implementationName + ", extensionName=" + extensionName + ", index=" + index + ", priority=" + priority + '}';
        }
    }
    
    public class ClassCache implements Serializable {
        private static final long serialVersionUID = -1961088227968987708L;
        private LinkedHashMap<String, List<FieldCache>> fields = new LinkedHashMap<String, List<FieldCache>>();
        private LinkedHashMap<String, List<MethodCache>> methods = new LinkedHashMap<String, List<MethodCache>>();

        public void addField(FieldCache field, String annotationClass) {
            List<FieldCache> list = fields.get(annotationClass);
            if (list == null) {
                list = new LinkedList<FieldCache>();
                fields.put(annotationClass, list);
            }
            list.add(field);
        }

        public void addField(String declaringClassName, String fieldName, String annotationClass) {
            addField(new FieldCache(declaringClassName, fieldName), annotationClass);
        }

        public void addMethod(MethodCache methodCache, String annotationClass) {
            List<MethodCache> list = methods.get(annotationClass);
            if (list == null) {
                list = new LinkedList<MethodCache>();
                methods.put(annotationClass, list);
            }
            list.add(methodCache);
        }

        public void addMethod(String declaringClassName, String methodName, String[] paramClassNames,String annotationClass) {
            addMethod(new PluginManagerCache.MethodCache(
                    declaringClassName,
                    methodName,
                    paramClassNames
            ), annotationClass);
        }

        public List<FieldCache> getFields(String annotationClass) {
            List<FieldCache> list = fields.get(annotationClass);
            return list==null?Collections.EMPTY_LIST:Collections.unmodifiableList(list);
        }

        public List<MethodCache> getMethods(String annotationClass) {
            List<MethodCache> list = methods.get(annotationClass);
            return list==null?Collections.EMPTY_LIST:Collections.unmodifiableList(list);
        }
    }

    public class FieldCache implements Serializable {
        private static final long serialVersionUID = -1961088227968987705L;
        private String className;
        private String fieldName;

        public FieldCache(String className, String fieldName) {
            this.className = className;
            this.fieldName = fieldName;
        }

        public String getClassName() {
            return className;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Field toField(ClassLoader loader) throws ClassNotFoundException, NoSuchFieldException {
            return Class.forName(getClassName(),true,loader).getDeclaredField(getFieldName());
        }
    }

    public class MethodCache implements Serializable {
        private static final long serialVersionUID = -1961088227968987706L;
        private String className;
        private String methodName;
        private String[] arguments;

        public MethodCache(String className, String methodName, String[] arguments) {
            this.className = className;
            this.methodName = methodName;
            this.arguments = arguments;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        public String[] getArguments() {
            return arguments;
        }

        public Method toMethod(ClassLoader loader) throws ClassNotFoundException, NoSuchMethodException {
            String[] args = getArguments();
            Class[] types=new Class[args.length];
            for (int i = 0; i < types.length; i++) {
                types[i]=Class.forName(args[i],true,loader);
            }
            return Class.forName(getClassName(),true,loader).getDeclaredMethod(getMethodName(),types);
        }
    }

    public class PluginCache implements Serializable {
        private static final long serialVersionUID = -1961088227968496604L;
        private Set<String> extensions=Collections.EMPTY_SET;
        private Map<String, Set<ImplementationCache>> implementations=new HashMap<String, Set<ImplementationCache>>();
        private Map<String, Set<ImplementationCache>> implFactories=new HashMap<String, Set<ImplementationCache>>();
        private String pluginClassName;
        private String messageSet;
        private String iconSet;
        private String id;
        private boolean enableLoading;

        private PluginCache(String id) {
            this.id=id;
        }

        public String getId() {
            return id;
        }

        public void setExtensions(Set<String> extensions) {
            this.extensions = extensions;
        }

        public void addImplementation(String implementationName, String extensionName,int priority) {
            Set<ImplementationCache> list = implementations.get(extensionName);
            if(list==null){
                list=new HashSet<ImplementationCache>();
                implementations.put(extensionName,list);
            }
            list.add(new ImplementationCache(this,implementationName,extensionName,implIndex++,priority));
        }

        public void addImplementationFactory(String implementationFactoryName, String extensionName,int priority) {
            Set<ImplementationCache> list = implFactories.get(extensionName);
            if(list==null){
                list=new HashSet<ImplementationCache>();
                implFactories.put(extensionName,list);
            }
            list.add(new ImplementationCache(this,implementationFactoryName,extensionName,implFactIndex++,priority));
        }

        public void setPluginClassName(String pluginClassName) {
            this.pluginClassName = pluginClassName;
        }

        public void setMessageSet(String messageSet) {
            this.messageSet = messageSet;
        }

        public void setIconSet(String iconSet) {
            this.iconSet = iconSet;
        }

        public void setEnableLoading(boolean enableLoading) {
            this.enableLoading = enableLoading;
        }

        public Set<ImplementationCache> getImplementations(String implementationName) {
            return getImplementations().get(implementationName);
        }

        public Set<ImplementationCache> getImplementations(Class implementation) {
            return getImplementations().get(implementation.getName());
        }

        public Map<String, Set<ImplementationCache>> getImplementations() {
            return implementations;
        }

        public Map<String, Set<ImplementationCache>> getImplementationFactories() {
            return implFactories;
        }

        public Set<String> getExtensions() {
            return extensions;
        }

        public String getMessageSet() {
            return messageSet;
        }

        public String getIconSet() {
            return iconSet;
        }

        public boolean isEnableLoading() {
            return enableLoading;
        }

        public String getPluginClassName() {
            return pluginClassName;
        }
    }

    public PluginCache getPluginCache(String id) {
        PluginManagerCache.PluginCache cache = plugins.get(id);
        if (cache == null) {
            cache = new PluginCache(id);
            plugins.put(id, cache);
        }
        return cache;
    }

    public Map<String, PluginCache> getPlugins() {
        return plugins;
    }

    public List<ImplementationCache> getImplementations() {
        return allImplementations;
    }

    public List<ImplementationCache> getImplementationFactories() {
        return implementationFactories;
    }

    public ClassCache getClassCache(String className, boolean autoCreate) {
        ClassCache classCache = classes.get(className);
        if (classCache == null && autoCreate) {
            classCache = new ClassCache();
            classes.put(className, classCache);
        }
        return classCache;
    }

    public Set<String> getExtensions() {
        return allExtensions;
    }

    public void build(Comparator<ImplementationCache> priorityComparator){
        allImplementations=new ArrayList<ImplementationCache>();
        for (Map.Entry<String,PluginManagerCache.PluginCache> pluginsEntry : getPlugins().entrySet()) {
            PluginManagerCache.PluginCache cache = pluginsEntry.getValue();
            for (Map.Entry<String, Set<ImplementationCache>> entry : cache.getImplementations().entrySet()) {
                allImplementations.addAll(entry.getValue());
            }
        }
        Collections.sort(allImplementations,priorityComparator);

        implementationFactories=new ArrayList<ImplementationCache>();
        for (Map.Entry<String,PluginManagerCache.PluginCache> pluginsEntry : getPlugins().entrySet()) {
            PluginManagerCache.PluginCache cache = pluginsEntry.getValue();
            for (Map.Entry<String, Set<ImplementationCache>> entry : cache.getImplementationFactories().entrySet()) {
                implementationFactories.addAll(entry.getValue());
            }
        }
        Collections.sort(implementationFactories,priorityComparator);
    }
}
