/**
 * ====================================================================
 * vpc-swingext library
 * <p>
 * Description: <start><end>
 *
 * <br>
 * <p>
 * Copyright [2020] [thevpc] Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * <br> ====================================================================
 */
package net.thevpc.common.swing.color;

import net.thevpc.common.swing.icon.ColorSwatch;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com) %creationtime 8 févr. 2007
 * 20:08:21
 */
public class JButtonColorChooser extends JButton {

    public static final String PROPERTY_COLOR_CHANGED = "ColorChanged";
    public static final String PROPERTY_COLOR_SET = "ColorSet";
    private Color color;
    private boolean nullable;

    public JButtonColorChooser(Color startupColor, boolean nullable) {
        this.color = startupColor;
        this.nullable = nullable;
        setIcon(new ColorSwatch(this::isEnabled, this::getValue));
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDialog();
            }
        });
    }

    public void showDialog() {
        Color clr = JColorChooser.showDialog(
                JButtonColorChooser.this, null,
                color
        );
        if (nullable || clr != null) {
            Color old = color;
            color = clr;
            firePropertyChange(PROPERTY_COLOR_CHANGED, old, color);
            firePropertyChange(PROPERTY_COLOR_SET, false, true);
        }
    }

    public void supportFirePropertyChange(String s, Object obj, Object obj1) {
        firePropertyChange(s, obj, obj1);
    }

    public Color getValue() {
        return color;
    }

    public void setValue(Color obj) {
        color = obj;
        repaint();
    }

}
