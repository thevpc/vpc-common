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
package net.thevpc.common.swings.util;


/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 3 janv. 2006 21:40:14
 */
public class StringShellFilter extends StringRegexpFilter {
    public StringShellFilter(String pattern,boolean caseSensitive) {
        super(shellToRegexpPattern(pattern),caseSensitive);
    }

    public static String sqlToRegexpPattern(String dosLikePattern) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dosLikePattern.length(); i++) {
            char c = dosLikePattern.charAt(i);
            switch (c) {
                case'%': {
                    sb.append(".*");
                    break;
                }
                case'?': {
                    sb.append("_");
                    break;
                }
                case'.': {
                    sb.append("\\.");
                    break;
                }
                case')': {
                    sb.append("\\)");
                    break;
                }
                case'(': {
                    sb.append("\\(");
                    break;
                }
                case'\\': {
                    sb.append("\\");
                    i++;
                    c = dosLikePattern.charAt(i);
                    sb.append(c);
                    break;
                }
                default: {
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }

    public static boolean shellMatches(String dosLikePattern,String string) {
        return string.matches(shellToRegexpPattern(dosLikePattern));
    }
    
    public static String shellToRegexpPattern(String dosLikePattern) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dosLikePattern.length(); i++) {
            char c = dosLikePattern.charAt(i);
            switch (c) {
                case'*': {
                    sb.append(".*");
                    break;
                }
                case'?': {
                    sb.append(".");
                    break;
                }
                case'.': {
                    sb.append("\\.");
                    break;
                }
                case')': {
                    sb.append("\\)");
                    break;
                }
                case'(': {
                    sb.append("\\(");
                    break;
                }
                case'\\': {
                    sb.append("\\");
                    i++;
                    c = dosLikePattern.charAt(i);
                    sb.append(c);
                    break;
                }
                default: {
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }
}
