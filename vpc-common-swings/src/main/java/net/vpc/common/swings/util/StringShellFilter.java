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


/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 3 janv. 2006 21:40:14
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
