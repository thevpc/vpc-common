//package net.thevpc.common.props.impl;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.thevpc.common.props.*;
//
//public class DefaultAppPropertyContainer extends SimpleProperty {
//    protected DefaultPropertyListeners listeners=new DefaultPropertyListeners(this);
//    private List<PropertyContainer> containers = new ArrayList<>();
//    private List<Property> properties = new ArrayList<>();
//    private DelegatePropertyListener delegateListener = new DelegatePropertyListener();
//
//    public DefaultAppPropertyContainer(String name) {
//        super(name);
//    }
//
//    public void add(PropertyContainer c) {
//        if (!containers.contains(c)) {
//            containers.add(c);
//            c.onChange(delegateListener);
//        }
//    }
//
//    public void add(Property c) {
//        if (!properties.contains(c)) {
//            properties.add(c);
//            c.onChange(delegateListener);
//        }
//    }
//
////    @Override
////    public AppPropertyBinding[] getProperties() {
////        List<AppPropertyBinding> all = new ArrayList<>();
////        for (Property property : properties) {
////            all.add(new AppPropertyBinding(property, property.propertyName()));
////        }
////        for (PropertyContainer container : containers) {
////            for (AppPropertyBinding property : container.getProperties()) {
////                all.add(new AppPropertyBinding(property.getProperty(), PropsHelper.buildPath(name,property.getPath())));
////            }
////        }
////        return all.toArray(new AppPropertyBinding[0]);
////    }
//
////    @Override
////    public PropertyListeners listeners() {
////        return listeners;
////    }
////
////
////
////    protected void firePropertyUpdated(PropertyEvent event) {
////        if (listeners != null) {
//////            if (!Objects.equals(old, v)) {
////            PropertyEvent event2 = new PropertyEvent(
////                    event.getProperty(), event.getIndex(),
////                    event.getOldValue(), event.getNewValue(),
////                    PropsHelper.buildPath(name,event.getPath()),
////                    event.getAction(),false
////            );
////            listeners.firePropertyUpdated(event2);
//////            }
////        }
////    }
////
////    private class DelegatePropertyListener implements PropertyListener {
////        @Override
////        public void propertyUpdated(PropertyEvent event) {
////            firePropertyUpdated(event);
////        }
////    }
//}
