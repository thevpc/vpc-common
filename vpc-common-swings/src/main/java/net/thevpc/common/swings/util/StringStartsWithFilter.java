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
public class StringStartsWithFilter implements StringFilter {
    String pattern;
    boolean caseSensitive;
    public StringStartsWithFilter(String pattern,boolean caseSensitive) {
        this.pattern = caseSensitive?pattern:pattern.toLowerCase();
        this.caseSensitive = caseSensitive;
    }


    public boolean accept(String value) {
        if (value == null) {
            value = "";
        }
        String input = value;
        if(caseSensitive){
            return input.startsWith(pattern);
        }else{
            return input.toLowerCase().startsWith(pattern);
        }
    }
}
