/**
 * ====================================================================
 *                        vpc-prs library
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
package net.thevpc.common.swing.border;

import net.thevpc.common.prs.iconset.IconSet;
import net.thevpc.common.prs.messageset.MessageSet;

import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 27 août 2007 13:13:36
 */
public class PRSBorderImpl implements PRSBorder{
    private String id;
    private TitledBorder titledBorder;

    public PRSBorderImpl(String id, TitledBorder titledBorder) {
        this.id = id;
        this.titledBorder = titledBorder;
    }

    public void update(MessageSet messageSet) {
        titledBorder.setTitle(messageSet.get(id, true));
    }


    public void update(IconSet iconSet) {
        // what to do?
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        titledBorder.paintBorder(c, g, x, y, width, height);
    }

    public Insets getBorderInsets(Component c) {
        return titledBorder.getBorderInsets(c);
    }

    public boolean isBorderOpaque() {
        return titledBorder.isBorderOpaque();
    }
}
