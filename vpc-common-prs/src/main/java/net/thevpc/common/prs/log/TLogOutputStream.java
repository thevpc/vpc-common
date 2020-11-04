/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.prs.log;


import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 4 juin 2007 17:00:26
 */
public class TLogOutputStream extends OutputStream {
    private final TLog log;

    public TLogOutputStream(TLog log) {
        this.log = log;
    }

    @Override
    public void write(int b) throws IOException {
        log.trace((char) b);
    }
}
