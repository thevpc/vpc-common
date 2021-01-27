/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import java.awt.Dimension;
import javax.swing.ImageIcon;

/**
 *
 * @author thevpc
 */
public interface IconSet {

    Dimension getSize();

    String getId();

    ImageIcon getIcon(String id);
}
