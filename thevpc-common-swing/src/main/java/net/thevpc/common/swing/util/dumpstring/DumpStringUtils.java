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
package net.thevpc.common.swing.util.dumpstring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.thevpc.common.swing.util.ClassMap;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * %creationtime 1 juin 2007 16:10:45
 */
public class DumpStringUtils {
    private static DumpStringHandler nullHandler = new NullDumpStringHandler();
    private static DumpStringHandler defaultHandler = new ObjectDumpStringHandler();
    private static DumpStringHandler arrayHandler = new ArrayDumpStringHandler();
    private static ClassMap<DumpStringHandler> registered = new ClassMap<DumpStringHandler>();
    private static Map<Class, DumpStringHandler> cached = new HashMap<Class, DumpStringHandler>();

    static {
        register(DumpString.class, new DumpStringDumpStringHandler());
        register(Map.class, new MapDumpStringHandler());
        register(Collection.class, new CollectionDumpStringHandler());
    }

//    public static void main(String[] args) {
//        Hashtable t=new Hashtable();
//        TreeSet ts=new TreeSet();
//        ts.add("a");
//        ts.add("b");
//        t.put("x",ts);
//        t.put("y",ts);
//        System.out.println(getDumpString(t));
//    }

    public static void register(Class clz, DumpStringHandler h) {
        cached.clear();
        if (clz == null) {
            nullHandler = h;
        } else {
            registered.put(clz, h);
        }
    }

    public static String getDumpString(Object o) {
        if (o == null) {
            return nullHandler.getDumpString(null);
        }
        DumpStringHandler h = cached.get(o.getClass());
        if (h == null) {
            if (o.getClass().isArray()) {
                return arrayHandler.getDumpString(o);
            }
            h = registered.getBest(o.getClass());
            if (h != null) {
                cached.put(o.getClass(), h);
            } else {
                h = defaultHandler;
            }
        }
        return h.getDumpString(o);
    }
}
