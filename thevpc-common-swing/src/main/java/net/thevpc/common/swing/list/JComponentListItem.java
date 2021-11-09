/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.list;

import javax.swing.JComponent;

/**
 *
 * @author thevpc
 */
public interface JComponentListItem<V> {

    void setEditable(JComponent component,boolean editable,int pos, int size);
    
    JComponent createComponent(int pos, int size);

    void setComponentValue(JComponent comp, V value, int pos, int size);

    V getComponentValue(JComponent comp, int pos);

    void uninstallComponent(JComponent comp);
    
}
