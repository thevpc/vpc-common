/**
 * ====================================================================
 *            Nuts : Network Updatable Things Service
 *                  (universal package manager)
 *
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
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
package net.thevpc.common.commandline;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vpc
 */
public class DefaultNonOption implements NonOption {
    public static final NonOption NAME=new DefaultNonOption("name");
    public static final NonOption VALUE=new DefaultNonOption("value");

    private String name;

    public DefaultNonOption(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<ArgumentCandidate> getValues() {
        List<ArgumentCandidate> list = new ArrayList<>();
        list.add(new DefaultArgumentCandidate("<" + getName() + ">"));
        return list;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

}
