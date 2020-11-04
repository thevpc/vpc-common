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
package net.thevpc.common.prs.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 1 déc. 2006 00:58:29
 */
public interface TLog {
    
    public void trace(Object msg);

    public void error(Object msg);

    public void error(String message, Object msg);

    public void warning(Object msg);

    public void debug(Object msg);

    public void close();


    public static final TLog STD = new TLog() {
        public String format(Object msg) {
            if (msg instanceof Throwable) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ((Throwable) msg).printStackTrace(new PrintStream(out));
                return (out.toString());
            }
            return String.valueOf(msg);
        }

        public void trace(Object msg) {
            System.out.println(format(msg));
        }

        public void error(Object msg) {
            System.err.println(format(msg));
        }

        public void warning(Object msg) {
            System.err.println(format(msg));
        }

        public void debug(Object msg) {
            System.out.println(format(msg));
        }

        public void error(String message, Object msg) {
            System.err.println(format(message));
            System.err.println(format(msg));
        }

        public void close() {
            //do nothing
        }
    };

    public static final TLog NULL = new TLog() {

        public void trace(Object msg) {
        }

        public void error(Object msg) {
        }

        public void warning(Object msg) {
        }

        public void debug(Object msg) {
        }

        public void error(String message, Object msg) {

        }
        public void close() {
            //do nothing
        }
    };
}
