/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
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
package net.thevpc.common.swing.table;

import net.thevpc.common.swing.iswing.IJTable;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author thevpc
 */
public class JTableImpl extends JTable implements IJTable {
    public JTableImpl() {
    }
    
    public Component getComponent(){
        return this;
    }

    public int convertRowIndexToModel(int i) {
        return i;
    }

    public int convertRowIndexToView(int i) {
        return i;
    }

}
