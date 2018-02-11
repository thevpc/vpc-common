package net.vpc.common.prs.plugin;

import java.util.logging.Level;
import net.vpc.common.prs.softreflect.SoftClass;
import net.vpc.common.prs.softreflect.SoftClassVisitor;
import net.vpc.common.prs.reflect.Reflector;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.logging.Logger;
import net.vpc.common.prs.log.LoggerProvider;
import net.vpc.common.prs.softreflect.SoftReflector;
import net.vpc.common.prs.softreflect.SoftAnnotation;
import net.vpc.common.prs.softreflect.SoftAnnotationAttribute;
import net.vpc.common.prs.softreflect.SoftClassFilter;
import net.vpc.common.prs.softreflect.SoftClassInfo;
import net.vpc.common.prs.softreflect.SoftField;
import net.vpc.common.prs.softreflect.SoftFieldFilter;
import net.vpc.common.prs.softreflect.SoftMethod;
import net.vpc.common.prs.softreflect.SoftMethodFilter;

class ImplementationsAutoLoadVisitor implements SoftClassVisitor {
    private PluginManagerCache pluginManagerCache;
    private PluginManagerCache.PluginCache pluginCache;
    private SoftMethodFilter methodsFilter;
    private SoftFieldFilter fieldsFilter;
    private SoftClassFilter ignoreImplementationFilter;
//    private ClassFilter implementationFilter;

    private Logger logger;

    ImplementationsAutoLoadVisitor(PluginManagerCache.PluginCache pluginCache, PluginManagerCache pluginManagerCache,SoftClassFilter ignoreImplementationFilter,SoftMethodFilter methodsFilter,SoftFieldFilter fieldsFilter,LoggerProvider loggerProvider) {
        this.pluginCache = pluginCache;
        this.pluginManagerCache = pluginManagerCache;
        this.logger = loggerProvider.getLogger(getClass().getName());
        this.methodsFilter=methodsFilter;
        this.fieldsFilter=fieldsFilter;
        this.ignoreImplementationFilter=ignoreImplementationFilter;
    }

    public void visit(SoftClass clazz) {
//        if(clazz.getSimpleName().contains("DBCAboutAction")){
//            System.out.println("????");
//        }
        final Class[] classes0 = new Class[0];
        boolean ignoreClass =
                clazz.isInterface()
                        || Modifier.isAbstract(clazz.getModifiers())
                        || ignoreImplementationFilter.accept (clazz)
                        || !Modifier.isPublic(clazz.getModifiers())
                        || Modifier.isInterface(clazz.getModifiers());
        if (ignoreClass) {
            return;
        }
        if(!clazz.hasBeanConstructor()){
            return;
        }

        PluginManagerCache.ClassCache classCache = pluginManagerCache.getClassCache(clazz.getName(), false);
        if (classCache == null) {
            classCache = pluginManagerCache.getClassCache(clazz.getName(), true);
            SoftClassInfo classInfo = SoftReflector.getClassInfo(clazz, fieldsFilter, methodsFilter, Reflector.DEFAULT);
            int priority=0;
            for (SoftAnnotation a : clazz.getAnnotations()) {
                if(a.getName().equals(Implementation.class.getName())){
                    for (SoftAnnotationAttribute t : a.getAtributes()) {
                        if(t.getName().equals("priority")){
                            priority=t.getValue()==null?0:(Integer)(t.getValue());
                          break;  
                        }
                    }
                    break;
                }
            }
            
            Set<String> allInterfaces = pluginManagerCache.getExtensions();
            for (SoftClass anInterface : classInfo.getParentInterfaces()) {
                String in = anInterface.getName();
                if (allInterfaces.contains(in)) {
                    pluginCache.addImplementation(clazz.getName(),in,priority);
                    logger.log(Level.FINER, "Detected Implementation {0} of {1}", new Object[]{clazz.getName(), in});
                }else if(anInterface.getName().equals(ExtensionFactory.class.getName())){
                    int factPriority=0;
                    String annotation_type=null;
                    for (SoftAnnotation a : clazz.getAnnotations()) {
                        if(a.getName().equals(ExtensionFactoryType.class.getName())){
                            for (SoftAnnotationAttribute t : a.getAtributes()) {
                                if(t.getName().equals("type")){
                                    annotation_type=t.getValue()==null?"":String.valueOf(t.getValue());
                                }else if(t.getName().equals("priority")){
                                    factPriority=t.getValue()==null?0:(Integer)(t.getValue());
                                }
                            }
                            break;
                        }
                    }
                    //ExtensionFactoryType annotation = (ExtensionFactoryType)clazz.getAnnotation(ExtensionFactoryType.class);
                    if(annotation_type==null || annotation_type.length()==0){
                        throw new IllegalArgumentException(clazz.getName()+" must provide @ExtensionFactoryType annotation with a valid valude");
                    }
                    pluginCache.addImplementationFactory(clazz.getName(),annotation_type,factPriority);
                    logger.log(Level.FINER, "Detected Implementation Factory {0} of {1}", new Object[]{clazz.getName(), in});
                }
            }
            for (SoftClass cls : classInfo.getParentClasses()) {
                String in = cls.getName();
                if (allInterfaces.contains(in)) {
                    pluginCache.addImplementation(clazz.getName(),in,priority);
                    logger.log(Level.FINER, "Detected Implementation {0} of {1}", new Object[]{clazz.getName(), in});
                }
            }
            for (SoftField field : classInfo.getFields()) {
                for (SoftAnnotation annotation : field.getAnnotations()) {
                    classCache.addField(field.getDeclaringClassName(), field.getName(), annotation.getName());
                    logger.log(Level.FINER, "Detected Field {0}", new Object[]{field.toString()});
                }
            }
            for (SoftMethod method : classInfo.getMethods()) {
                for (SoftAnnotation annotation : method.getAnnotations()) {
                    classCache.addMethod(method.getDeclaringClassName(),method.getName(),method.getParameterTypes(), annotation.getName());
                    logger.log(Level.FINER, "Detected Method {0}", new Object[]{method.toString()});
                }
            }
        }
    }
}
