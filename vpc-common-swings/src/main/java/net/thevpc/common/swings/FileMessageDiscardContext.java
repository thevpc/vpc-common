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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.swings;

import net.thevpc.common.swings.dialog.MessageDiscardContext;
import net.thevpc.common.swings.util.SwingPrivateIOUtils;


import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author vpc
 */
public class FileMessageDiscardContext implements MessageDiscardContext {
    private File file;
    private String parameter;

    public FileMessageDiscardContext(File file, String parameter) {
        this.file = file;
        this.parameter = parameter;
    }

    public boolean isDiscarded() {
        try {
            Properties p = SwingPrivateIOUtils.loadXMLProperties(file);
            return Boolean.valueOf(p.getProperty(parameter));
        } catch (IOException ex) {
            return false;
            //Logger.getLogger(FileMessageDiscardContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setDiscarded(boolean val) {
        Properties p=new Properties();
        try {
            File pp=file.getParentFile();
            if(pp!=null){
                pp.mkdirs();
            }
            p = SwingPrivateIOUtils.loadXMLProperties(file);
        } catch (IOException ex) {
            //Logger.getLogger(FileMessageDiscardContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        p.put(parameter, String.valueOf(val));
        try {
            SwingPrivateIOUtils.storeXMLProperties(file,p,"FileMessageDiscardContext store file ** do not edit manually **");
        } catch (IOException ex) {
            //Logger.getLogger(FileMessageDiscardContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
