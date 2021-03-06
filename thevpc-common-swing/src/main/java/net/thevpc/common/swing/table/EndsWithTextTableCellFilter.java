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
package net.thevpc.common.swing.table;

import javax.swing.table.TableModel;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 6 mai 2006 18:47:41
 */
public class EndsWithTextTableCellFilter extends DefaultTableCellFilter {
    public EndsWithTextTableCellFilter(String pattern, boolean caseInsensitive) {
        super(pattern, caseInsensitive);
    }

    public boolean acceptCell(String value, int rowIndex, int columnIndex, TableModel model) {
        if (isCaseInsensitive()) {
            return value.endsWith(getPattern());
        } else {
            return value.toLowerCase().endsWith(getPattern().toLowerCase());
        }
    }
}
