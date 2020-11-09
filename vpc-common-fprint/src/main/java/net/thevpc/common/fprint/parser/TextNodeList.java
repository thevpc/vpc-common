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
package net.thevpc.common.fprint.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vpc on 5/23/17.
 */
public class TextNodeList implements TextNode, Iterable<TextNode> {
    private List<TextNode> children = new ArrayList<TextNode>();

    public TextNodeList(TextNode... children) {
        for (TextNode c : children) {
            add(c);
        }
    }

    public void add(TextNode item) {
        children.add(item);
    }

    public TextNode get(int index) {
        return children.get(index);
    }

    public int size() {
        return children.size();
    }

    @Override
    public Iterator<TextNode> iterator() {
        return children.iterator();
    }

    @Override
    public String toString() {
        return children.toString();
    }
}
