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


import javax.swing.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
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
