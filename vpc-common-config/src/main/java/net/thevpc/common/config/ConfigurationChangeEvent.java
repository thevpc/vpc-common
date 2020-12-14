/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.config;

/**
 *
 * @author thevpc
 */
public class ConfigurationChangeEvent {
    private String id;
    private Configuration oldValue;
    private Configuration newValue;

    public ConfigurationChangeEvent(String id, Configuration oldValue, Configuration newValue) {
        this.id = id;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getId() {
        return id;
    }

    public Configuration getNewValue() {
        return newValue;
    }

    public Configuration getOldValue() {
        return oldValue;
    }
    
}
