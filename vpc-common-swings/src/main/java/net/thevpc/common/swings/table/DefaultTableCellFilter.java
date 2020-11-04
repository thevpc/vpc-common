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
package net.thevpc.common.swings.table;

import javax.swing.table.TableModel;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 6 mai 2006 18:12:54
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
