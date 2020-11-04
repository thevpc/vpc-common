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
package net.thevpc.common.swings.util.jcmd;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public class ExpectOption {
    private String shortName;
    private String longName;
    private boolean booleanValue;
    private boolean acceptValue;
    private int minFollowingParamsCount;
    private int maxFollowingParamsCount;
    private boolean valueIsFirstParam;
//    String[][] validValues;

    protected ExpectOption(String shortName, String longName, boolean booleanValue, boolean acceptValue, boolean valueIsFirstParam, int minFollowingParamsCount, int maxFollowingParamsCount
//                           , String[][] validValues
    ) {
        this.shortName = shortName;
        this.longName = longName;
        this.booleanValue = booleanValue;
        this.acceptValue = acceptValue;
        this.minFollowingParamsCount = minFollowingParamsCount;
        this.maxFollowingParamsCount = maxFollowingParamsCount;
        this.valueIsFirstParam = valueIsFirstParam;
//        this.validValues = validValues;
    }

    public boolean isAcceptValue() {
        return acceptValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public String getLongName() {
        return longName;
    }

    public int getMaxFollowingParamsCount() {
        return maxFollowingParamsCount;
    }

    public int getMinFollowingParamsCount() {
        return minFollowingParamsCount;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isValueIsFirstParam() {
        return valueIsFirstParam;
    }
    
}
