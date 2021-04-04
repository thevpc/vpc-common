/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

/**
 *
 * @author vpc
 */
public class NamedValue {
    
    boolean group;
    private String id;
    private String icon;
    private String name;

    public NamedValue(boolean group, String id, String name, String icon) {
        this.group = group;
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isGroup() {
        return group;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
    
}
