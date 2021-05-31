package net.thevpc.common.props;

public class PropertyEvent {

    private Property property;
    private Object index;
    private Object oldValue;
    private Object newValue;
    private Path path;
    private PropertyUpdate eventType;
    private String changeId;
    private boolean immediate;

    public PropertyEvent(Property property, Object index, Object oldValue, Object newValue, Path path, PropertyUpdate eventType, boolean immediate) {
        this(property, index, oldValue, newValue, path, eventType,null,immediate);
    }
    
    public PropertyEvent(Property property, Object index, Object oldValue, Object newValue, Path path, PropertyUpdate eventType, String changeId, boolean immediate) {
        this.immediate = immediate;
        this.property = property;
        this.index = index;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.path = path;
        if(path==null){
            throw new IllegalArgumentException("invalid property event path is null");
        }
//        if(Objects.equals(oldValue,newValue)){
//            throw new IllegalArgumentException("invalid property event values");
//        }
        this.eventType = eventType;
        this.changeId = changeId;
    }

    public String changeId() {
        return changeId;
    }
    

    public PropertyUpdate eventType() {
        return eventType;
    }

    public Path eventPath() {
        return path;
    }

    public <T extends Property> T property() {
        return (T) property;
    }

    public <T> T index() {
        return (T) index;
    }

    public <T> T oldValue() {
        return (T) oldValue;
    }

//    /**
//     * convenient method to return newValue
//     * @param <T>
//     * @return newValue
//     */
//    public <T> T getValue() {
//        return (T) newValue;
//    }

    public <T> T newValue() {
        return (T) newValue;
    }

    @Override
    public String toString() {
        return String.valueOf(eventType) + "{"
                + (immediate?" immediate":" propagated")
                + ", path='" + path + '\''
                + ", property=" + property.propertyName()
                + ", oldValue=" + oldValue
                + ", newValue=" + newValue
                + (index == null ? "" : (", index=" + index))
                + '}';
    }

    public boolean immediate() {
        return immediate;
    }

    public boolean propagated() {
        return !immediate;
    }
}
