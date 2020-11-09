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
