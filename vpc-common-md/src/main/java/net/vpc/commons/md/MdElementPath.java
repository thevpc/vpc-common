/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md;

/**
 *
 * @author vpc
 */
public class MdElementPath<T extends MdElement>{

    public static final MdElementPath ROOT = new MdElementPath();
    private MdElementPath parent;
    private T element;

    private MdElementPath(T element) {
        this(element, null);
    }

    private MdElementPath() {
    }

    private MdElementPath(T element, MdElementPath parent) {
        if (element == null) {
            throw new NullPointerException();
        }
        if (this.parent!=null && parent.isRoot()) {
            parent=null;
        }
        this.element = element;
        this.parent = parent;
    }

    public boolean isRoot() {
        return parent == null && element==null;
    }
    
    public boolean isFirstLevel() {
        return parent == null && parent!=null;
    }

    public MdElementPath getParentPath() {
        return parent;
    }

    public T getElement() {
        return element;
    }

    public MdElementPath append(MdElement e) {
        if (this.element == null) {
            return new MdElementPath(e);
        }
        return new MdElementPath(e, this);
    }

    @Override
    public String toString() {
        String prefix="";
        if(parent!=null){
            prefix=parent.toString()+"/";
        }
        switch(element.getElementType()){
            case XML:{
                return prefix+"<"+element.asXml().getTag()+">";
            }
            case SEQ:{
                return prefix+String.valueOf(element.getElementType())+"[..."+element.asSeq().getElements().length+"]";
            }
            case CODE:{
                return prefix+String.valueOf(element.getElementType())+"("+element.asCode().getLanguage()+")";
            }
        }
        return prefix+String.valueOf(element.getElementType());
    }
    
}
