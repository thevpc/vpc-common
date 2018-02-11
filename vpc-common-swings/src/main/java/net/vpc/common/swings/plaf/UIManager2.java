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

package net.vpc.common.swings.plaf;

import javax.swing.*;
import java.util.Vector;

/**
 *
 * @author vpc
 */
public class UIManager2 {

    private static Vector<PlafHandler> handlers = new Vector<PlafHandler>();
    private static NonThemablePlafHandler defaultHandler = new NonThemablePlafHandler();

    static {
        registerHandler(new MetalPlafHandler());
    }

    public static void registerHandler(PlafHandler h) {
        handlers.add(h);
        h.install();
    }

    public static void unregister(PlafHandler h) {
        handlers.remove(h);
    }

    public static PlafItem getPlafItem(String plafItem) {
        if(plafItem==null || plafItem.trim().length()==0){
            return null;
        }
        PlafHandler h=getPlafHandler(plafItem);
        if(h==null){
            return null;
        }
        return h.getPlafItem(plafItem);
    }

    public static void apply(PlafItem plafItem) throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            UnsupportedLookAndFeelException {
        getPlafHandler(plafItem.getPlaf()).apply(plafItem);
    }

    public static PlafHandler getPlafHandler(String name) {
        int max = -1;
        PlafHandler best = null;
        for (PlafHandler plafHandler : handlers) {
            int x = plafHandler.accept(name);
            if (x > max) {
                max = x;
                best = plafHandler;
            }
        }

        if (best == null) {
            best = defaultHandler;
        }
        return best;
    }
}
