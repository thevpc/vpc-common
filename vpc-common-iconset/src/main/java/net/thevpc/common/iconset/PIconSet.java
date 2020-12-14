/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import javax.swing.ImageIcon;

import net.thevpc.common.props.PValue;
import net.thevpc.common.props.WritablePValue;

/**
 *
 * @author thevpc
 */
public interface PIconSet {

    WritablePValue<String> id();

    PValue<ImageIcon> icon(String id);
}
