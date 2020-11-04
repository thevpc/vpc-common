/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
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