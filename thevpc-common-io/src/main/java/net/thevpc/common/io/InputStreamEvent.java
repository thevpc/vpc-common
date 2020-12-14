/**
 * ====================================================================
 *            vpc-common-io : common reusable library for
 *                          input/output
 *
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
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
package net.thevpc.common.io;

/**
 *
 * @author thevpc
 */
public class InputStreamEvent {
    
    private final Object source;
    private final String sourceName;
    private final long globalCount;
    private final long globalMillis;
    private final long partialCount;
    private final long partialMillis;
    private final long length;
    private final Throwable exception;

    public InputStreamEvent(Object source, String sourceName, long globalCount, long globalMillis, long partialCount, long partialMillis,long length,Throwable exception) {
        this.source = source;
        this.length = length;
        this.sourceName = sourceName;
        this.globalCount = globalCount;
        this.globalMillis = globalMillis;
        this.partialCount = partialCount;
        this.partialMillis = partialMillis;
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }

    public long getLength() {
        return length;
    }

    
    public Object getSource() {
        return source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public long getGlobalCount() {
        return globalCount;
    }

    public long getGlobalMillis() {
        return globalMillis;
    }

    public long getPartialCount() {
        return partialCount;
    }

    public long getPartialMillis() {
        return partialMillis;
    }
    
}
