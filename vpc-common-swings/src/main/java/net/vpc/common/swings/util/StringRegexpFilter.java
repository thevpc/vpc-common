/**
 * ====================================================================
 *                        vpc-commons library
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
package net.vpc.common.swings.util;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
 */
public class StringRegexpFilter implements StringFilter {
    private boolean multilineMode = true;
    private Pattern pattern;

    public StringRegexpFilter(String pattern, boolean caseSensitive) {
        int flags = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
        this.pattern = Pattern.compile(pattern, flags);
    }


    public boolean accept(String value) {
        if (value == null) {
            value = "";
        }
        String input = value;
        if (!multilineMode) {
            Matcher matcher = pattern.matcher(input);
            return matcher.matches();
        } else {
            StringTokenizer st = new StringTokenizer(input, "\n\r");
            while (st.hasMoreTokens()) {
                Matcher matcher = pattern.matcher(st.nextToken());
                if (matcher.matches()) {
                    return true;
                }
            }
            return false;
        }
    }
}
