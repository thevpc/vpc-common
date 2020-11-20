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
package net.thevpc.common.swings.border;

import net.thevpc.common.prs.iconset.IconSet;
import net.thevpc.common.prs.messageset.MessageSet;

import javax.swing.border.Border;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 27 août 2007 13:11:28
 */
public interface PRSBorder extends Border {
    public void update(IconSet iconSet);
    public void update(MessageSet messageSet);
}
