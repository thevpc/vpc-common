/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.combo;

import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import net.thevpc.common.swing.NamedValue;

/**
 *
 * @author vpc
 */
public class ExtendedComboBoxModel extends DefaultComboBoxModel<Object> {
    
    public ExtendedComboBoxModel() {
    }

    public ExtendedComboBoxModel(Object[] es) {
        super(es);
    }

    public ExtendedComboBoxModel(Vector<Object> vector) {
        super(vector);
    }

    @Override
    public void setSelectedItem(Object anObject) {
        if (anObject instanceof NamedValue) {
            NamedValue v = (NamedValue) anObject;
            if (v.isGroup()) {
                int index = getIndexOf(anObject);
                if (index < getSize()) {
                    setSelectedItem(getElementAt(index + 1));
                }
            } else {
                super.setSelectedItem(anObject);
            }
        }
    }
    
}
