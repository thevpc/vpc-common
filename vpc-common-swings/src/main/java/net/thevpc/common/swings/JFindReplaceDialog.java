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
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 3 sept. 2005 01:22:28
 */
public class JFindReplaceDialog extends JDialog {
    private JPanel all;
    private JTextComponent textComponent;
    private JTextField findText;
//    private JButton hilightButton;
    private JButton unhilightButton;
    private JButton findNextButton;
    private JButton cancelButton;
    private JCheckBox caseSensitive;
    JCheckBox regularExpressionOption;
    JCheckBox holeWordOption;
    JCheckBox hilightAllOption;
    JCheckBox documentStartOption;
    // i have no other hiliters
    private Highlighter.HighlightPainter highlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.magenta.brighter());

    public JFindReplaceDialog(Frame owner, JTextComponent comp) throws HeadlessException {
        super(owner);
        this.textComponent = comp;
        setLayout(new BorderLayout());

        Box n = Box.createHorizontalBox();
        n.setBorder(BorderFactory.createTitledBorder("Find"));
        n.add(new JLabel("search for "));
        n.add(findText = new JTextField());
        setTitle("Find Text");
        findText.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
//                System.out.println("keyTyped e = " + e);
            }

            public void keyPressed(KeyEvent e) {
//                System.out.println("keyPressed e = " + e);
            }

            public void keyReleased(KeyEvent e) {
//                System.out.println("keyReleased e = " + e);
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    JFindReplaceDialog.this.setVisible(false);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    find();
                }
            }
        });
        JComponent c = new JPanel(new GridLayout(2, 3));
        c.setBorder(BorderFactory.createTitledBorder("Options"));
        c.add(caseSensitive = new JCheckBox("Case Sensitive"));

        c.add(holeWordOption = new JCheckBox("Hole Words Only"));
        holeWordOption.setEnabled(false);

        c.add(regularExpressionOption = new JCheckBox("Regular Expression"));
        regularExpressionOption.setEnabled(false);

        c.add(documentStartOption = new JCheckBox("Entire Scope"));
        c.add(hilightAllOption = new JCheckBox("Hilight All"));

        setTitle("Find Text");


        JPanel s = new JPanel(new DumbGridBagLayout(
                "[==nothing][find][removeHL][cancel]"
        ).setInsets(".*",new Insets(4,4,4,4)));

        s.add(new JPanel(),"nothing");
        s.add(findNextButton = new JButton("Find"),"find");
//        s.add(hilightButton = new JButton("Highlight"));
        s.add(unhilightButton = new JButton("Remove Highlights"),"removeHL");
        s.add(cancelButton = new JButton("Cancel"),"cancel");
        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                find();
//                JFindReplaceDialog.this.setVisible(false);
            }
        });
        unhilightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeHighlights();
                JFindReplaceDialog.this.setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFindReplaceDialog.this.setVisible(false);
            }
        });

        all=new JPanel(new BorderLayout());
        all.add(n, BorderLayout.PAGE_START);
        all.add(c, BorderLayout.CENTER);
        all.add(s, BorderLayout.PAGE_END);

        add(all, BorderLayout.CENTER);
        ActionMap actionMap = textComponent.getActionMap();
        InputMap inputMap = textComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        Action action;
        KeyListener keyListener=new KeyListener() {
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()==KeyEvent.VK_ESCAPE || e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    setVisible(false);
                }
            }
            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), action = new AbstractAction("ECHAP") {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        unhilightButton.addKeyListener(keyListener);
        findNextButton.addKeyListener(keyListener);
        cancelButton.addKeyListener(keyListener);
        caseSensitive.addKeyListener(keyListener);
        regularExpressionOption.addKeyListener(keyListener);
        holeWordOption.addKeyListener(keyListener);
        hilightAllOption.addKeyListener(keyListener);
        documentStartOption.addKeyListener(keyListener);

//        this.addComponentListener(new ComponentAdapter() {
//            public void componentShown(ComponentEvent ce) {
//                findNextButton.requestFocus();
//            }
//        });

        actionMap.put(action, action);
//        addKeyListener(new KeyListener() {
//            public void keyTyped(KeyEvent e) {
//                System.out.println("keyTyped e = " + e);
//            }
//
//            public void keyPressed(KeyEvent e) {
//                System.out.println("keyTyped e = " + e);
//            }
//
//            public void keyReleased(KeyEvent e) {
//                System.out.println("keyTyped e = " + e);
//            }
//        });
    }

    public void showDialog() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        findText.requestFocus();
    }

    public void setTextToFind(String t) {
        findText.setText(t);
    }

    public void highlight() {
        // First remove all old highlights
        removeHighlights();
        String pattern = findText.getText();
        int start = documentStartOption.isSelected() ? 0 : textComponent.getCaretPosition();

        if (pattern.length() > 0) {
            try {
                Highlighter hilite = textComponent.getHighlighter();
                Document doc = textComponent.getDocument();
                String text = doc.getText(0, doc.getLength());
                int pos = start;

                // Search for pattern
                pos = 0;
                while ((pos = text.indexOf(pattern, pos)) >= 0) {
                    // Create highlighter using private painter and apply around pattern
                    hilite.addHighlight(pos, pos + pattern.length(), highlighter);
                    pos += pattern.length();
                }
                textComponent.repaint();
            } catch (BadLocationException e) {
                System.out.println(e);
            }
        }
    }

    public void find() {
        if (hilightAllOption.isSelected()) {
            highlight();
        } else {
            findNext();
        }
    }

    public void findNext() {
        // First remove all old highlights
        removeHighlights();
        String pattern = findText.getText();
        int start = documentStartOption.isSelected() ? 0 : textComponent.getCaretPosition();
        if (pattern.length() > 0) {
            try {
//                Highlighter hilite = textComponent.getHighlighter();
                Document doc = textComponent.getDocument();
                String text = doc.getText(0, doc.getLength());
                if (!caseSensitive.isSelected()) {
                    text = text.toLowerCase();
                    pattern = pattern.toLowerCase();
                }
                int pos = start;


                pos = text.indexOf(pattern, pos);
                textComponent.select(0, 0);
                if (pos >= 0) {
//                    hilite.addHighlight(pos, pos + pattern.length(), highlighter);
                    textComponent.setCaretPosition(pos + pattern.length());
                    textComponent.select(pos, pos + pattern.length());
                }
                textComponent.repaint();
            } catch (BadLocationException e) {
                System.out.println(e);
            }
        }
    }

    // Removes only our private highlights
    public void removeHighlights() {
        Highlighter hilite = textComponent.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (Highlighter.Highlight hilite1 : hilites) {
            if (hilite1.getPainter() instanceof DefaultHighlighter.DefaultHighlightPainter) {
                hilite.removeHighlight(hilite1);
            }
        }
    }

    public void installKeyBindings() {
        InputMap inputMap = textComponent.getInputMap(JComponent.WHEN_FOCUSED);

        if (inputMap != null) {
            ActionMap actionMap = textComponent.getActionMap();
            DefaultAction[] actions = new DefaultAction[]
                    {
                            new DefaultAction("actionFind", KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK)) {
                                public void actionPerformed(ActionEvent e) {
                                    String s = textComponent.getSelectedText();
                                    if (s != null && s.length() > 0) {
                                        setTextToFind(s);
                                    }
                                    showDialog();
                                }
                            }
                            , new DefaultAction("actionFindNext", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0)) {
                        public void actionPerformed(ActionEvent e) {
                            findNext();
                        }
                    }
                            , new DefaultAction("actionFindHightlight", KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_MASK)) {
                        public void actionPerformed(ActionEvent e) {
                            String s = textComponent.getSelectedText();
                            if (s != null && s.length() > 0) {
                                setTextToFind(s);
                                highlight();
                            }
                        }
                    }
                            , new DefaultAction("actionFindNoHightlight", KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK)) {
                        public void actionPerformed(ActionEvent e) {
                            removeHighlights();
                        }
                    }
                            , new DefaultAction("actionAllHighlits", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)) {
                        public void actionPerformed(ActionEvent e) {
                            removeHighlights();
                        }
                    }
                    };
            for (DefaultAction action : actions) {
                KeyStroke[] keyStrokes = action.getKeyStrokes();
                for (KeyStroke keyStroke : keyStrokes) {
                    inputMap.put(keyStroke, action);
                }
                if (actionMap != null) {
                    actionMap.put(action, action);
                }
            }
        }
    }

    public void updateUI() {
        if (all!=null) {
            all.updateUI();
        }
    }
}
