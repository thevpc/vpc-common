package net.vpc.common.prs.factory;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)  alias vpc
* @creationtime 2009/08/15 17:43:38
*/
public final class ImplementationDescriptor<T> implements Comparable<ImplementationDescriptor>, Cloneable {

    Class implementationType;
    ExtensionDescriptor extension;
    Object owner;

    public ImplementationDescriptor(Class<? extends T> impl, Object owner) {
        this.implementationType = impl;
        this.owner = owner;
    }

    public Object getOwner() {
        return owner;
    }

    public ExtensionDescriptor getExtension() {
        return extension;
    }

    public Class getImplementationType() {
        return implementationType;
    }

    public int compareTo(ImplementationDescriptor o) {
        return this.implementationType.getName().compareTo(o.implementationType.getName());
    }

    @Override
    protected ImplementationDescriptor<T> clone() {
        try {
            return (ImplementationDescriptor<T>) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException("No Way to get this Error");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImplementationDescriptor other = (ImplementationDescriptor) obj;
        if (this.implementationType != other.implementationType && (this.implementationType == null || !this.implementationType.equals(other.implementationType))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.implementationType != null ? this.implementationType.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return implementationType.getSimpleName();
    }

//    public T newInstance() {
//        try {
//            Class<T> cls=implementationType;
//            T obj = cls.newInstance();
//            if(extension.getFactory().listeners!=null){
//                FactoryEvent e=null;
//                for (FactoryListener listener : extension.getFactory().listeners) {
//                    if(e==null){
//                        e=new FactoryEvent(extension.getFactory(), extension, null, this,obj,Factory.OBJECT_CREATED,null,obj,owner);
//                    }
//                    listener.instanceCreated(e);
//                }
//            }
//            return obj;
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        }
//    }
}
