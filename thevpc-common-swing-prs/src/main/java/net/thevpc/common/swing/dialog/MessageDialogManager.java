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

package net.thevpc.common.swing.dialog;

import java.awt.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 3 dec. 2006 23:18:36
 */
public interface MessageDialogManager<App>{
    public static enum ReturnType{
        OK, CANCEL,DISCARDED
    }
    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type);
    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type, String title);
    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type, String title, Throwable th);
    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type, String title, Throwable th, MessageDiscardContext context);
    public ReturnType showMessage(Component parentComponent, String message, MessageDialogType type, String title, Throwable th, String context);

    void init(App application);

    App getDialogOwner();
}
