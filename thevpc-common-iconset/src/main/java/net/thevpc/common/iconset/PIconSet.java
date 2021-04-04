/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import javax.swing.ImageIcon;

import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.ObservableValue;

/**
 *
 * @author thevpc
 */
public interface PIconSet {

    WritableValue<String> id();

    ObservableValue<ImageIcon> icon(String id);
}
