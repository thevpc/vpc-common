/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.list;

import java.util.function.Function;
import javax.swing.JComponent;

/**
 *
 * @author vpc
 */
public interface JComponentListLayout {

    JComponent doLayout(JComponent[] allComponents,Function<Integer,Object> valueMapper);
    
}
