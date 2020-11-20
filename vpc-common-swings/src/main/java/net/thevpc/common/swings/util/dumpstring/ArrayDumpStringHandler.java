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
package net.thevpc.common.swings.util.dumpstring;

import java.lang.reflect.Array;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 1 juin 2007 16:24:38
 */
public class ArrayDumpStringHandler implements DumpStringHandler {

    public ArrayDumpStringHandler() {
    }

    public String getDumpString(Object object) {
        DumpStringHelper h = new DumpStringHelper("");
        int len = Array.getLength(object);
        for (int i = 0; i < len; i++) {
            h.add(Array.get(object, i));
        }
        return h.toString();
    }
}
