/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.html;

import java.util.HashMap;
import javax.swing.ButtonModel;

/**
 *
 * @author thevpc
 */
public class HtmlComponent {

    private int index;
    private String id;
    private Object model;
    private String type;
    private HashMap<Object, Object> properties;

    public HtmlComponent(int index, String id, Object model, String type, HashMap<Object, Object> properties) {
        this.index = index;
        this.id = id;
        this.model = model;
        this.type = type;
        this.properties = properties;
    }

    public String getId() {
        return id;
    }

    public Object getModel() {
        return model;
    }

    public HashMap<Object, Object> getProperties() {
        return properties;
    }

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public Boolean isSelected() {
        if (getModel() instanceof ButtonModel) {
            return ((ButtonModel) getModel()).isSelected();
        }
        return false;
    }

    public Boolean isEnabled() {
        if (getModel() instanceof ButtonModel) {
            return ((ButtonModel) getModel()).isEnabled();
        }
        return false;
    }
}
