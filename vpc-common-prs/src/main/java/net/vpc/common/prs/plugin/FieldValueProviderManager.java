package net.vpc.common.prs.plugin;

import net.vpc.common.prs.factory.Factory;

import java.lang.reflect.Field;
import java.util.*;

public class FieldValueProviderManager {
    private Map<Class, ValueProvider> providers=new HashMap<Class, ValueProvider>();
    private Factory factory;

    public void addProvider(ValueProvider provider){
        providers.put(provider.getType(),provider);
    }

    public Object getValue(Class type, PluginDescriptor pluginDescriptor){
        ValueProvider p = getValueProvider(type);
        if(p!=null){
            return p.getValue(pluginDescriptor);
        }
        throw new NoSuchElementException("no provider for class "+type);
    }

    public ValueProvider getValueProvider(Class clazz){
        ValueProvider p = providers.get(clazz);
        if(p!=null){
            return p;
        }
        List<Class> found=new ArrayList<Class>();
        for (Class clz : providers.keySet()) {
            if(clazz.isAssignableFrom(clz)){
                found.add(clz);
            }
        }
        int size = found.size();
        switch (size){
            case 0:{
                return null;
            }
            case 1:{
                return providers.get(found.get(0));
            }
            default:{
                System.err.println("Ambiguous Provider for "+clazz+" found equivalent "+found);
                return providers.get(found.get(0));
            }
        }
    }

    public Object createValue(Field field, PluginDescriptor pluginInfo){
        if(factory!=null){
            return factory.newInstance(field.getType());
        }
        throw new NoSuchElementException("no provider for "+field.getType());
    }

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }
}
