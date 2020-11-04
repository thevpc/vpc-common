/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */

package net.thevpc.common.swings.plaf;

import javax.swing.*;

/**
 *
 * @author vpc
 */
public class NonThemablePlafHandler implements PlafHandler{
    private static final PlafItem[] NO_PLAF_ITEMS=new PlafItem[0];
    public NonThemablePlafHandler() {
    }

    public int accept(String plaf) {
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            if(lookAndFeelInfo.getClassName().equals(plaf)){
                return 1;
            }
        }
        return -1;
    }

    public PlafItem getPlafItem(String item) {
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            if(lookAndFeelInfo.getClassName().equals(item)){
                return new PlafItem(item, lookAndFeelInfo.getClassName(), null, lookAndFeelInfo.getName());
            }
        }
        throw new UnsupportedOperationException("Plaf Not supported : "+item);
    }
    

    public void apply(PlafItem plafItem) throws ClassNotFoundException, 
               InstantiationException, 
               IllegalAccessException,
               UnsupportedLookAndFeelException  {
        UIManager.setLookAndFeel(plafItem.getPlaf());
    }

    public void install() {
        //
    }

    public PlafItem[] loadItems() {
        return NO_PLAF_ITEMS;
    }

    
}
