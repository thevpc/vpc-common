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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.swing;

import net.thevpc.common.swing.dialog.MessageDiscardContext;
import net.thevpc.common.swing.util.SwingPrivateIOUtils;


import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author thevpc
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
