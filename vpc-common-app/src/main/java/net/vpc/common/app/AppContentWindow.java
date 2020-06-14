/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.app;

import javax.swing.JComponent;
import net.vpc.common.props.WritablePValue;

/**
 *
 * @author vpc
 */
public interface AppContentWindow {

    String id();

    WritablePValue<Boolean> active();

    WritablePValue<String> title();

    WritablePValue<String> icon();

    WritablePValue<Boolean> closable();
    
    WritablePValue<JComponent> component();

    WritablePValue<AppWindowStateSet> state();
}
