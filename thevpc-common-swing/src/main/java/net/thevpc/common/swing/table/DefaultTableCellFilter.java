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
 * %creationtime 6 mai 2006 18:12:54
 */
public abstract class DefaultTableCellFilter implements TableCellFilter {

    private StringFormatter DEFAULT_STRING_FORMATTER = new DefaultStringFormatter();
    private String pattern;
    private boolean caseInsensitive = true;
    private StringFormatter stringFormatter = DEFAULT_STRING_FORMATTER;

    public DefaultTableCellFilter(String pattern, boolean caseInsensitive) {
        this.pattern = pattern;
        this.caseInsensitive = caseInsensitive;
    }

    public boolean acceptCell(Object value, int rowIndex, int columnIndex, TableModel model) {
        return acceptCell(stringFormatter.format(value, rowIndex, columnIndex, model), rowIndex, columnIndex, model);
    }

    public abstract boolean acceptCell(String value, int rowIndex, int columnIndex, TableModel model);

    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public StringFormatter getStringFormatter() {
        return stringFormatter;
    }

    public void setStringFormatter(StringFormatter stringFormatter) {
        this.stringFormatter = stringFormatter;
    }

    public static interface StringFormatter {
        public String format(Object value, int rowIndex, int columnIndex, TableModel model);
    }

    public static class DefaultStringFormatter implements StringFormatter {
        public String format(Object value, int rowIndex, int columnIndex, TableModel model) {
            return (value != null ? String.valueOf(value) : "");
        }
    }
}
