/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
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
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 26 avr. 2006 01:48:56
 */
public class TextManipSupport {

    private JPopupMenu popup;
    private JTextComponent txtcomponent;
    private boolean clearAlwaysEnabled;
    private static javax.swing.text.DefaultEditorKit.CutAction swingCutAction = new javax.swing.text.DefaultEditorKit.CutAction();
    private static javax.swing.text.DefaultEditorKit.CopyAction swingCopyAction = new javax.swing.text.DefaultEditorKit.CopyAction();
    private static javax.swing.text.DefaultEditorKit.PasteAction swingPastAction = new javax.swing.text.DefaultEditorKit.PasteAction();

    private DateFormat dateFormat = DateFormat.getDateInstance();
    private DateFormat timeFormat = DateFormat.getTimeInstance();
    private DateFormat dateTimeFormat = DateFormat.getDateTimeInstance();
    private ArrayList<Action> actions = new ArrayList<Action>();

    public TextManipSupport(JTextComponent comp) {
        txtcomponent = comp;
        txtcomponent.setComponentPopupMenu(getPopup());
    }

    public void updateUI() {
        if (popup != null) {
            SwingUtilities.updateComponentTreeUI(popup);
            SwingUtilities3.applyOrientation(popup);
        }
    }

    public JPopupMenu getPopup() {
        if (popup == null) {
            popup = new JPopupMenu("actions");
            SwingUtilities3.applyOrientation(popup);
            addAction(copyAction);
            addAction(cutAction);
            addAction(pasteAction);
            popup.addSeparator();
            addAction(pasteDateAction);
            addAction(pasteTimeAction);
            addAction(pasteDateTimeAction);
            popup.addSeparator();
            addAction(clearAction);
        }
        return popup;
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actions);
    }

    protected void addAction(TextAction a) {
        actions.add(a);
        popup.add(a);
        a.refresh();
    }

    public void paste(String someString) {
        int p = txtcomponent.getCaretPosition();
        try {
            txtcomponent.getDocument().insertString(p, someString, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public boolean isClearAlwaysEnabled() {
        return clearAlwaysEnabled;
    }

    public void setClearAlwaysEnabled(boolean clearAlwaysEnabled) {
        this.clearAlwaysEnabled = clearAlwaysEnabled;
    }

    public static abstract class TextAction extends AbstractAction {

        protected TextAction() {
            prepare("TextAction");
        }

        protected TextAction(String name) {
            super(name);
            prepare(name);
        }

        protected TextAction(String name, Icon icon) {
            super(name, icon);
            prepare(name);
        }

        private void prepare(String id) {
            putValue(Action.NAME, id);
            final SwingComponentConfigurer c = SwingComponentConfigurerFactory.getInstance().get(TextAction.class);
            if (c != null) {
                c.onCreateComponent(this);
            }
        }

        public void refresh() {

        }
    }

    private abstract class UpdateTextAction extends TextAction {

        PropertyChangeListener refreshener;

        protected UpdateTextAction() {
            refreshener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    refresh();
                }
            };
            txtcomponent.addPropertyChangeListener("enabled", refreshener);
            txtcomponent.addPropertyChangeListener("editable", refreshener);
        }

        protected UpdateTextAction(String name) {
            super(name);
        }

        protected UpdateTextAction(String name, Icon icon) {
            super(name, icon);
        }

        public void refresh() {
            setEnabled(txtcomponent.isEnabled() && txtcomponent.isEditable());
        }
    }

    private TextAction copyAction = new TextAction("Copy") {
        public void actionPerformed(ActionEvent e) {
            swingCopyAction.actionPerformed(new ActionEvent(txtcomponent, 1, "copy"));
        }

        public void putValue(String key, Object newValue) {
            super.putValue(key, newValue);
        }
    };

    TextAction cutAction = new UpdateTextAction("Cut") {

        public void actionPerformed(ActionEvent e) {
            swingCutAction.actionPerformed(new ActionEvent(txtcomponent, 1, "cut"));
        }

    };
    TextAction pasteAction = new UpdateTextAction("Paste") {

        public void actionPerformed(ActionEvent e) {
            swingPastAction.actionPerformed(new ActionEvent(txtcomponent, 1, "paste"));
        }

    };

    TextAction clearAction = new UpdateTextAction("Clear") {

        public void actionPerformed(ActionEvent e) {
            txtcomponent.setText("");
        }

        public void refresh() {
            setEnabled(
                    Boolean.TRUE.equals(txtcomponent.getClientProperty("clearEnabled"))
                    || isClearAlwaysEnabled()
                    || (txtcomponent.isEnabled() && txtcomponent.isEditable()));
        }
    };

    TextAction pasteDateAction = new UpdateTextAction("PasteDate") {

        public void actionPerformed(ActionEvent e) {
            paste(getDateFormat().format(new Date()));
        }

    };

    TextAction pasteTimeAction = new UpdateTextAction("PasteTime") {

        public void actionPerformed(ActionEvent e) {
            paste(getTimeFormat().format(new Date()));
        }

    };
    TextAction pasteDateTimeAction = new UpdateTextAction("PasteDateTime") {

        public void actionPerformed(ActionEvent e) {
            paste(getDateTimeFormat().format(new Date()));
        }

    };

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateFormat getDateTimeFormat() {
        return dateTimeFormat;
    }

    public void setDateTimeFormat(DateFormat dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public DateFormat getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(DateFormat timeFormat) {
        this.timeFormat = timeFormat;
    }
}
