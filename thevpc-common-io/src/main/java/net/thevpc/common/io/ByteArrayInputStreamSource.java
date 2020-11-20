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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class ByteArrayInputStreamSource implements InputStreamSource {
    private final String name;
    private final byte[] bytes;
    private final Object src;

    public ByteArrayInputStreamSource(byte[] bytes, String name, Object src) {
        this.bytes = bytes;
        this.name = name;
        this.src = src;
    }

    @Override
    public InputStream open() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void copyTo(Path path) throws IOException {
        Files.write(path, bytes);
    }
    

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getSource() {
        return src;
    }

    @Override
    public String toString() {
        return "ByteArrayInputStreamSource{" + "name=" + name + ", bytes=" + bytes.length + ", src=" + src + '}';
    }
    
}
