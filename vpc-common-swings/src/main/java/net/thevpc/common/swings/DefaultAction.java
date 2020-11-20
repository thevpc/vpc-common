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

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  13 juil. 2006 22:14:21
 */
public abstract class DefaultAction extends AbstractAction {
    public DefaultAction(String name, KeyStroke... keyStroke) {
        super(name);
        putValue(ACTION_COMMAND_KEY, name);
        putValue("KeyStroke", keyStroke);
        putValue(Action.ACCELERATOR_KEY, keyStroke.length==0?null:keyStroke[0]);
        final SwingComponentConfigurer r = SwingComponentConfigurerFactory.getInstance().get(DefaultAction.class);
        if(r!=null){
            r.onCreateComponent(this);
        }
    }

    public KeyStroke[] getKeyStrokes() {
        return (KeyStroke[]) getValue("KeyStroke");
    }
}
