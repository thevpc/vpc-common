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
package net.thevpc.common.swings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 mai 2006 18:47:41
 */
public class JURLTextField extends JPanel {
    public static final String SELECTED_URL ="SELECTED_URL";
    private JButton configButton;
    private JTextField textField;
    private JFileChooser jfc;

    public JURLTextField() {
        configButton = new JButton("...");
        configButton.setMargin(new Insets(0, 0, 0, 0));
        textField = new JTextField("");
        textField.setColumns(20);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(textField);
        add(configButton);
        setBorder(BorderFactory.createEtchedBorder());
        configButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectURL();
            }
        });
    }

    public void selectURL() {
        String s = textField.getText();
        JFileChooser jfc = getJFileChooser();
        jfc.setSelectedFile((s == null || s.trim().length() == 0) ? null : new File(s));
        if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(configButton)) {
            File selectedFile = jfc.getSelectedFile();
            try {
                setURL(selectedFile==null?null:selectedFile.toURL());
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(this,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public JFileChooser getJFileChooser() {
        if (jfc == null) {
            jfc = new JFileChooser();
        }
        return jfc;
    }

    public String getText() {
        return textField.getText();
    }

    public URL getURL() throws MalformedURLException {
        return textField.getText().length() == 0 ? null : new URL(textField.getText());
    }

    public String getURLPath() {
        return textField.getText().length() == 0 ? null : textField.getText();
    }

    public void setText(String file) {
        textField.setText(file);
    }

    public void setURL(URL file) throws MalformedURLException {
        URL old = getURL();
        textField.setText(file == null ? "" : file.toString());
        firePropertyChange(SELECTED_URL,old,file);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textField.setEnabled(enabled);
        configButton.setEnabled(enabled);
    }

    public JTextField getTextField() {
        return textField;
    }
}