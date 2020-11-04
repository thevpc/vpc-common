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
 * Copyright (C) 2016-2020 thevpc
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * <br>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <br>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

class InputStreamStringIterator implements Iterator<String> {
    private BufferedReader r;
    private String line;

    public InputStreamStringIterator(InputStream is) {
        r = new BufferedReader(new InputStreamReader(is));
    }

    @Override
    public boolean hasNext() {
        if (r == null) {
            return false;
        }
        try {
            line = r.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return line != null;
    }

    @Override
    public String next() {
        return line;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void finalize() throws Throwable {
        if (r != null) {
            r.close();
            r = null;
        }
    }
}
