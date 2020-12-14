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

package net.thevpc.common.swings.plaf;

import javax.swing.*;

/**
 *
 * @author thevpc
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
