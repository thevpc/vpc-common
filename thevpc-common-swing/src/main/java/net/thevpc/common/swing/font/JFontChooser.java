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
// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3)
// Source File Name:   JFontChooser.java

package net.thevpc.common.swing.font;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import net.thevpc.common.swing.SwingUtilities3;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  13 juil. 2006 22:14:21
 */
public class JFontChooser extends JComponent
        implements ActionListener, ListSelectionListener {

    public JFontChooser(Font font) {
        this.font = font;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        Font[] fontList = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        Vector<Font> fonts = new Vector<Font>(1, 1);
        Vector<String> names = new Vector<String>(1, 1);
        for (Font aFontList : fontList) {
            String fontName = aFontList.getFamily();
            if (!names.contains(fontName)) {
                names.addElement(fontName);
                fonts.addElement(aFontList);
            }
        }
        if(font==null){
            font=getFont();
        }

        availableFonts = new Font[fonts.size()];
        for (int i = 0; i < fonts.size(); i++)
            availableFonts[i] = (Font) fonts.elementAt(i);

        fontNames = new JList(names);
        JScrollPane fontNamesScroll = new JScrollPane(fontNames);
        fontNames.addListSelectionListener(this);
        Object[] styles = {
                "Regular", "Bold", "Italic", "BoldItalic"
        };
        fontStyles = new JList(styles);
        JScrollPane fontStylesScroll = new JScrollPane(fontStyles);
        fontStyles.setSelectedIndex(0);
        fontStyles.addListSelectionListener(this);
        String[] sizes = new String[69];
        for (int i = 3; i < 72; i++)
            sizes[i - 3] = (new Integer(i + 1)).toString();

        fontSizes = new JList(sizes);
        JScrollPane fontSizesScroll = new JScrollPane(fontSizes);
        fontSizes.addListSelectionListener(this);
        currentSize = new JTextField(5);
        currentSize.setText((new Integer(font.getSize())).toString());
        currentSize.addActionListener(this);
        GridBagLayout g2 = new GridBagLayout();
        GridBagConstraints c2 = new GridBagConstraints();
        JPanel sizePane = new JPanel(g2);
        c2.gridx = 0;
        c2.gridy = 0;
        c2.insets = new Insets(2, 5, 2, 5);
        c2.anchor = 17;
        sizePane.add(currentSize);
        g2.setConstraints(currentSize, c2);
        sizePane.add(fontSizesScroll);
        c2.gridy++;
        c2.fill = 2;
        g2.setConstraints(fontSizesScroll, c2);
        preview = new JFontPreviewPanel(font);
        okay = new JButton("ok");
        okay.addActionListener(this);
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        GridBagLayout g = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        JPanel top = new JPanel(g);
        c.anchor = 17;
        c.fill = 3;
        c.insets = new Insets(2, 5, 2, 5);
        c.gridx = 0;
        c.gridy = 0;
        top.add(fontNamesScroll);
        g.setConstraints(fontNamesScroll, c);
        c.gridx++;
        top.add(fontStylesScroll);
        g.setConstraints(fontStylesScroll, c);
        c.gridx++;
        top.add(sizePane);
        g.setConstraints(sizePane, c);
        add(top, BorderLayout.PAGE_START);
        add(preview, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(2));
        buttons.add(okay);
        buttons.add(cancel);
        add(buttons, BorderLayout.PAGE_END);
        fontSizes.setSelectedValue((new Integer(font.getSize())).toString(), true);
        fontNames.setSelectedValue(font.getFamily(), true);
        if (font.getStyle() == 0)
            fontStyles.setSelectedValue("Regular", false);
        else if (font.getStyle() == 2)
            fontStyles.setSelectedValue("Italic", false);
        else if (font.getStyle() == 1)
            fontStyles.setSelectedValue("Bold", false);
        else if (font.getStyle() == 3)
            fontStyles.setSelectedValue("BoldItalic", false);
    }

    private void updateFont(Font f) {
        font = f;
        preview.setFont(font);
    }

    private void updateFontSize(int size) {
        updateFont(font.deriveFont((new Integer(size)).floatValue()));
    }

    private void updateFontStyle(int style) {
        updateFont(font.deriveFont(style));
    }

    public Font getSelectedFont() {
        return font;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okay) {
            returnValue = 2;
            if (dialog != null)
                dialog.setVisible(false);
            acceptSelection();
        }
        if (e.getSource() == cancel) {
            returnValue = 4;
            if (dialog != null)
                dialog.setVisible(false);
            cancelSelection();
        }
        if (e.getSource() == currentSize)
            fontSizes.setSelectedValue(currentSize.getText(), true);
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == fontNames) {
            Font f = availableFonts[fontNames.getSelectedIndex()];
            f = new Font(f.getFontName(), font.getStyle(), font.getSize());
            updateFont(f);
        }
        if (e.getSource() == fontSizes) {
            currentSize.setText((String) fontSizes.getSelectedValue());
            updateFontSize((new Integer(currentSize.getText())).intValue());
        }
        if (e.getSource() == fontStyles) {
            int style = 0;
            String selection = (String) fontStyles.getSelectedValue();
            if (selection.equals("Regular"))
                style = 0;
            if (selection.equals("Bold"))
                style = 1;
            if (selection.equals("Italic"))
                style = 2;
            if (selection.equals("BoldItalic"))
                style = 3;
            updateFontStyle(style);
        }
    }

    public int showDialog(Component parent, String title) {
        returnValue = 0;
        Frame frame = (parent instanceof Frame) ? (Frame) parent : (Frame) SwingUtilities.getAncestorOfClass(java.awt.Frame.class, parent);
        if (SwingUtilities3.isAncestorDialog(parent)) {
            dialog = new JDialog(SwingUtilities3.getDialogAncestor(parent), title, true);
        } else {
            dialog = new JDialog(SwingUtilities3.getFrameAncestor(parent), title, true);
        }
        dialog.getContentPane().add(this, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return returnValue;
    }

    public void acceptSelection() {
    }

    public void cancelSelection() {
    }


    private Font font;
    private JList fontNames;
    private JList fontSizes;
    private JList fontStyles;
    private JTextField currentSize;
    private JButton okay;
    private JButton cancel;
    private Font[] availableFonts;
    private JFontPreviewPanel preview;
    private JDialog dialog;
    private int returnValue;
    public static final int ERROR_OPTION = 0;
    public static final int ACCEPT_OPTION = 2;
    public static final int CANCEL_OPTION = 4;
}
