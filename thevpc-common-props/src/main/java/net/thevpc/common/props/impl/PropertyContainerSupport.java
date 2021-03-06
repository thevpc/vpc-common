//package net.thevpc.common.props.impl;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import net.thevpc.common.props.*;
//
//public class PropertyContainerSupport implements PropertyContainer {
//    protected DefaultPropertyListeners listeners = new DefaultPropertyListeners(this);
//    private List<PropertyContainer> containers = new ArrayList<>();
//    private List<Property> properties = new ArrayList<>();
//    private String name;
//    private PropertyContainer container;
//    private DelegatePropertyListener delegateListener = new DelegatePropertyListener();
//
//    public PropertyContainerSupport(String name, PropertyContainer container) {
//        this.name = name;
//        this.container = container;
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
//    @Override
//    public AppPropertyBinding[] getProperties() {
//        List<AppPropertyBinding> all = new ArrayList<>();
//        for (Property property : properties) {
//            all.add(new AppPropertyBinding(property, property.propertyName()));
//        }
//        for (PropertyContainer container : containers) {
//            for (AppPropertyBinding property : container.getProperties()) {
//                all.add(new AppPropertyBinding(property.getProperty(), PropsHelper.buildPath(name,property.getPath())));
//            }
//        }
//        return all.toArray(new AppPropertyBinding[0]);
//    }
//
//    @Override
//    public PropertyListeners listeners() {
//        return listeners;
//    }
//
//
//    private class DelegatePropertyListener implements PropertyListener {
//        @Override
//        public void propertyUpdated(PropertyEvent event) {
//            PropertyEvent event2 = new PropertyEvent(
//                    event.getProperty(), event.getIndex(),
//                    event.getOldValue(), event.getNewValue(),
//                    PropsHelper.buildPath(name,event.getPath()),
//                    event.getAction(),false
//            );
//            listeners.firePropertyUpdated(event2);
//        }
//    }
//}
