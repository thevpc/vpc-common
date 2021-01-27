/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/

package net.thevpc.common.swing.plaf;

import javax.swing.*;
import java.util.Vector;

/**
 *
 * @author thevpc
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
