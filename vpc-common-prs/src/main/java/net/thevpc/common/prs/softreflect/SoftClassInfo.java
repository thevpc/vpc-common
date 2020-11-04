/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.prs.softreflect;

import java.util.List;

/**
 *
 * @author vpc
 */
public class SoftClassInfo {

    private List<SoftField> fields;
    private List<SoftMethod> methods;
    private List<SoftClass> parentClasses;
    private List<SoftClass> parentInterfaces;

    public SoftClassInfo() {
    }

    public List<SoftField> getFields() {
        return fields;
    }

    public void setFields(List<SoftField> fields) {
        this.fields = fields;
    }

    public List<SoftMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<SoftMethod> methods) {
        this.methods = methods;
    }

    public List<SoftClass> getParentClasses() {
        return parentClasses;
    }

    public void setParentClasses(List<SoftClass> parentClasses) {
        this.parentClasses = parentClasses;
    }

    public List<SoftClass> getParentInterfaces() {
        return parentInterfaces;
    }

    public void setParentInterfaces(List<SoftClass> parentInterfaces) {
        this.parentInterfaces = parentInterfaces;
    }
}
