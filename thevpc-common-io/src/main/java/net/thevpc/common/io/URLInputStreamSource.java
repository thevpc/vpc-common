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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author taha.bensalah@gmail.com
 */
class URLInputStreamSource implements InputStreamSource {

    private final URL url;

    public URLInputStreamSource(URL url) {
        this.url = url;
    }

    @Override
    public void copyTo(Path path) throws IOException {
        Files.copy(open(), path);
    }

    @Override
    public InputStream open() throws IOException {
        InputStream s = null;
        s = url.openStream();
        if (s != null) {
            return s;
        }
        throw new IOException("Invalid URL " + url);
    }

    @Override
    public Object getSource() {
        return url;
    }

    @Override
    public String getName() {
        return URLUtils.getURLName(url);
    }

    @Override
    public String toString() {
        return url.toString();
    }

}
