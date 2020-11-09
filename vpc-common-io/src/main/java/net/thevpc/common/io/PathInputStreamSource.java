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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

class PathInputStreamSource implements InputStreamSource {

    private final Path path;

    public PathInputStreamSource(Path file) {
        this.path = file;
    }

    @Override
    public InputStream open() throws IOException {
        return Files.newInputStream(path);
    }

    @Override
    public void copyTo(Path path) throws IOException {
        Files.copy(path, path);
    }

    @Override
    public Object getSource() {
        return path;
    }

    @Override
    public String getName() {
        return path.getFileName().toString();
    }

    @Override
    public String toString() {
        return path.toString();
    }

}
