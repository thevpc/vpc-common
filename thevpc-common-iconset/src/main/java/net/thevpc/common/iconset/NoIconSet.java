/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import javax.swing.ImageIcon;

/**
 *
 * @author vpc
 */
public class NoIconSet implements IconSet {

    private String id;

    public NoIconSet(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ImageIcon getIcon(String id, IconSetConfig config) {
        return null;
    }

}
