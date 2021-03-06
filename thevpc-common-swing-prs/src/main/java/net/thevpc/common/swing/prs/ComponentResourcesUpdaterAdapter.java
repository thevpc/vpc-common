/**
 * ====================================================================
 *                        vpc-prs library
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
package net.thevpc.common.swing.prs;

import net.thevpc.common.prs.iconset.IconSet;
import net.thevpc.common.prs.messageset.MessageSet;
import net.thevpc.common.swing.iconset.ComponentIconSetUpdater;
import net.thevpc.common.swing.messageset.ComponentMessageSetUpdater;

import javax.swing.*;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  17 juil. 2006 09:08:00
 */
public class ComponentResourcesUpdaterAdapter implements ComponentIconSetUpdater, ComponentMessageSetUpdater {
    private ComponentResourcesUpdater base;

    public ComponentResourcesUpdaterAdapter(ComponentResourcesUpdater base) {
        this.base = base;
    }

    public void updateIconSet(JComponent comp, String id, IconSet iconSet) {
        base.update(comp,id,null,iconSet);
    }

    public void install(JComponent comp, String id) {
        base.install(comp,id);
    }

    public void uninstall(JComponent comp, String id) {
        base.uninstall(comp,id);
    }

    public void updateMessageSet(JComponent comp, String id, MessageSet messageSet) {
        base.update(comp,id,messageSet,null);
    }
}
