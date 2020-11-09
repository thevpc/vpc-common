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
