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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by vpc on 3/7/17.
 */
public abstract class AbstractCommandAutoComplete implements CommandAutoComplete {

    private LinkedHashMap<String, ArgumentCandidate> candidates = new LinkedHashMap<>();

    @Override
    public List<ArgumentCandidate> getCandidates() {
        return new ArrayList<>(candidates.values());
    }

    @Override
    public void addCandidate(String value, String display) {
        if (value!=null && !value.trim().isEmpty()) {
            addCandidatesImpl(new DefaultArgumentCandidate(value));
        }
    }

    @Override
    public void addCandidate(ArgumentCandidate value) {
        if (value!=null && !value.getValue().trim().isEmpty()) {
            addCandidatesImpl(value);
        }
    }

    protected ArgumentCandidate addCandidatesImpl(ArgumentCandidate value) {
        return candidates.put(value.getValue(), value);
    }

    @Override
    public void addExpectedTypedValue(String type, String name) {
        addCandidate("", "<" + name + ">");
    }

}
