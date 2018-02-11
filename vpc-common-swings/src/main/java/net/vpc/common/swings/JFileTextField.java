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
package net.vpc.common.swings;

import net.vpc.common.swings.util.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.io.File;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 mai 2006 18:47:41
 */
public class JFileTextField extends JPanel {
    public static final String PRE_SELECT_FILE="PRE_SELECT_FILE";
    public static final String SELECTED_FILE="SELECTED_FILE";
    private JButton configButton;
    private JTextField textField;
    private JFileChooser jfc;
    private File defaultFolder;
    private File value;

    public JFileTextField() {
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
                selectFile();
            }
        });
        textField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                firePropertyChange(PRE_SELECT_FILE, false, true);
                //??
            }

            public void focusLost(FocusEvent e) {
                setFile(textField.getText());
            }
        });
    }

    public void selectFile() {
        firePropertyChange(PRE_SELECT_FILE, false, true);
        String s = textField.getText();
        JFileChooser jfc = getJFileChooser();
        File selectedfolder=(s == null || s.trim().length() == 0) ? null : new File(s);
        File df = getDefaultFolder();
        if(selectedfolder==null){
            selectedfolder=df;
        }else if(!selectedfolder.isAbsolute() && df!=null){
            selectedfolder=new File(df,selectedfolder.getPath());
        }
        jfc.setSelectedFile(selectedfolder);
        if(selectedfolder==null){
            if (df!=null) {
                jfc.setCurrentDirectory(df);
            }
        }
        if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(configButton)) {
            setFile(jfc.getSelectedFile());
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

    public File getFile() {
        return textField.getText().length() == 0 ? null : new File(textField.getText());
    }

    public File getAbsoluteFile() {
        File f=getFile();
        if(f==null){
            return getDefaultFolder();
        }
        File df = getDefaultFolder();
        if(df!=null && !f.isAbsolute()){
            return new File(df,f.getPath());
        }
        return f;
    }

    public String getFilePath() {
        return textField.getText().length() == 0 ? null : textField.getText();
    }

    public void setText(String file) {
        setFile(file);
    }

    public void setFile(String file) {
        setFile(file==null?null:new File(file));
    }
    
    public void setFile(File file) {
        File old = value;
        File df = getDefaultFolder();
        if(file == null || df==null){
            textField.setText(file == null ? "" : file.getPath());
        }else if(file.isAbsolute()){
            String path = IOUtils.getRelativePath(df, file);
            textField.setText(path==null?file.getPath():path);
        }else{
            textField.setText(file.getPath());
        }
        value=file;
        firePropertyChange(SELECTED_FILE,old,file);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textField.setEnabled(enabled);
        configButton.setEnabled(enabled);
    }

    public JTextField getTextField() {
        return textField;
    }

    public File getDefaultFolder() {
        return defaultFolder;
    }

    public void setDefaultFolder(File defaultFolder) {
        File old=this.defaultFolder;
        this.defaultFolder = defaultFolder;
        firePropertyChange("defaultFolder",old,defaultFolder);
    }
}
