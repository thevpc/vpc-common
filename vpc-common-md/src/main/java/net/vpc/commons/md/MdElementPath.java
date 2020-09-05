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
public class MdElementPath {

    public static final MdElementPath ROOT = new MdElementPath();
    private MdElementPath parent;
    private MdElement element;

    private MdElementPath(MdElement element) {
        this(element, null);
    }

    private MdElementPath() {
    }

    private MdElementPath(MdElement element, MdElementPath parent) {
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

    public MdElementPath getParent() {
        return parent;
    }

    public MdElement getItem() {
        return element;
    }

    public MdElementPath append(MdElement e) {
        if (this.element == null) {
            return new MdElementPath(e);
        }
        return new MdElementPath(e, this);
    }

}
