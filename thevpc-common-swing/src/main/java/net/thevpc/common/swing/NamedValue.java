/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing;

/**
 *
 * @author thevpc
 */
public class NamedValue {

    boolean group;
    private String id;
    private String icon;
    private String name;
    private int preferredOrder;

    public NamedValue(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public NamedValue(boolean group, String id, String name, String icon, int preferredOrder) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.icon = icon;
        this.preferredOrder = preferredOrder;
    }

    public int getPreferredOrder() {
        return preferredOrder;
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
