/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import javax.swing.ImageIcon;

/**
 *
 * @author thevpc
 */
public interface IconSet {

    String getId();

    ImageIcon getIcon(String id, IconSetConfig config);
}
