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
package net.thevpc.common.swings.dialog;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;
import net.thevpc.common.swings.FileMessageDiscardContext;

/**
 *
 * @author thevpc
 */
public class DefaultMessageDialogManager<App> implements MessageDialogManager<App> {

    private App application;

    public void init(App app) {
        this.application = app;
    }

    public App getDialogOwner() {
        return application;
    }

    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type) {
        return showMessage(parentComponent, message, type, null, null, (MessageDiscardContext)null);
    }

    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type, String title) {
        return showMessage(parentComponent, message, type, title, null, (MessageDiscardContext)null);
    }

    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type, String title, Throwable th) {
        return showMessage(parentComponent, message, type, title, th, (MessageDiscardContext)null);
    }

    
    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type, String title, Throwable th, String context) {
        return showMessage(parentComponent, message, type, title, th, context==null?null: new FileMessageDiscardContext(new File(System.getProperty("user.home")+"/.java-apps/.MessageDiscardContext"), context));
    }

    
    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type, String title, Throwable th, MessageDiscardContext context) {
        if (context != null && context.isDiscarded()) {
            return ReturnType.DISCARDED;
        }
        MessageDialogPanel panel = new MessageDialogPanel();
        panel.setDiscardEnabled(context!=null);
        panel.setTitle(title);
        panel.setType(type);
        panel.setMessage(message);
        panel.setDetails(throwableToString(th));
        if(message==null && th!=null){
            if(th.getMessage()!=null){
                panel.setMessage(th.getMessage());
            }else{
                panel.setMessage(th.toString());
            }
        }
        boolean ok=panel.showDialog(parentComponent);
        if(context!=null && panel.isDiscardSelected()){
            context.setDiscarded(true); 
        }
        return ok?MessageDialogManager.ReturnType.OK:MessageDialogManager.ReturnType.CANCEL;
    }

    public void showMessage_old(Component parentComponent, String message, MessageDialogType type, String title, Throwable th, MessageDiscardContext context) {
        if (context != null && context.isDiscarded()) {
            return;
        }
        int optionPaneType = JOptionPane.ERROR_MESSAGE;
        int optionPaneButtons = JOptionPane.OK_CANCEL_OPTION;
        String optionPaneLongMessage = "Some error occurs. see details...";
        String optionPaneTitle = title;
        if (optionPaneTitle == null) {
            switch (type) {
                case ERROR:
                    {
                        optionPaneTitle = "Error";
                        optionPaneType = JOptionPane.ERROR_MESSAGE;
                        optionPaneButtons = JOptionPane.OK_CANCEL_OPTION;
                        optionPaneLongMessage = "Some error occurs. see details...";
                        break;
                    }
                case WARNING:
                    {
                        optionPaneTitle = "Warning";
                        optionPaneType = JOptionPane.WARNING_MESSAGE;
                        optionPaneButtons = JOptionPane.OK_OPTION;
                        optionPaneLongMessage = "Some warnings. see details...";
                        break;
                    }
            }
        }
        if (th == null) {
            JOptionPane.showMessageDialog(parentComponent, message, optionPaneTitle, optionPaneType);
            return;
        }
        if (message == null) {
            message = th.getMessage();
            if(message==null){
               message= th.getClass().getSimpleName();
            }
            //if (message == null) {
            //    message = optionPaneLongMessage;
            //}
        }
        Object bestMsg = message.length() > 80 ? optionPaneLongMessage : message;
        th.printStackTrace();
        JCheckBox discardCheck = null;
        if (context != null) {
            JPanel pp = new JPanel();
            pp.add(new JLabel((String) bestMsg), BorderLayout.CENTER);
            discardCheck = new JCheckBox("Discard");
            pp.add(discardCheck, BorderLayout.PAGE_END);
            bestMsg = pp;
        }
        int x = JOptionPane.showOptionDialog(parentComponent, bestMsg, optionPaneTitle, optionPaneButtons, optionPaneType, null, new Object[]{"Ok", "Show Details"}, 0);
        if (discardCheck != null && discardCheck.isSelected()) {
            context.setDiscarded(true);
        }
        if (x == 1) {
            JTextArea a = new JTextArea();
            a.setEditable(false);
            JScrollPane p = new JScrollPane(a);
            p.setPreferredSize(new Dimension(500, 400));
            p.setMaximumSize(new Dimension(800, 600));
            a.setText(throwableToString(th));
            JOptionPane.showMessageDialog(parentComponent, p, optionPaneTitle, optionPaneType);
        }
    }

    public static String throwableToString(Throwable th) {
        StringBuilder sb = new StringBuilder();
        if(th!=null){
            sb.append(th.getMessage()).append("\n");
            if (th instanceof SQLException) {
                SQLException s = (SQLException) th;
                while (s != null) {
                    if (sb.length() > 0) {
                        sb.append("\n------------------\n");
                    }
                    sb.append(throwableToString0(s));
                    s = s.getNextException();
                }
            } else {
                sb.append(throwableToString0(th));
            }
        }
        return sb.toString();
    }

    public static String throwableToString0(Throwable th) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        th.printStackTrace(new PrintStream(out));
        return (out.toString());
    }
}
