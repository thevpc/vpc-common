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
package net.thevpc.common.swing.util;

import java.io.PrintStream;
import java.text.MessageFormat;

/**
 * @author thevpc
 *         Date: 29 janv. 2005
 *         Time: 12:48:28
 */
public class LoggerTaskMonitor extends TaskMonitor {
    private long max;
    private long index;
    private long lastLogTime;
    private long logInterval;
    private PrintStream out;

    public LoggerTaskMonitor(long logInterval, PrintStream out) {
        this.logInterval = logInterval;
        lastLogTime = -1;
        this.out = out;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getMax() {
        return max;
    }

    public void progressImpl(long index, String progressKey, Object[] progressParams) {
        this.index = index;
        long nowTime = System.currentTimeMillis();
        if (index == 0 || index == max || lastLogTime < 0 || logInterval <= 0 || (nowTime - lastLogTime) >= logInterval)
        {
            lastLogTime = nowTime;
            String str = MessageFormat.format(progressKey,progressParams);
            out.println(progressParams[PERCENT_PARAM] + " : " + str);
        }

    }

    public long getIndex() {
        return index;
    }
}
