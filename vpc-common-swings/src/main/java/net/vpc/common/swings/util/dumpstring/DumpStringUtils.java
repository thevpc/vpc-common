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
package net.vpc.common.swings.util.dumpstring;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.vpc.common.swings.util.ClassMap;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 1 juin 2007 16:10:45
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
