/**
 * ====================================================================
 *                        vpc-commons library
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
package net.thevpc.common.swing.util;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 3 janv. 2006 21:40:14
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
