/**
 * ====================================================================
 *            thevpc-common-md : Simple Markdown Manipulation Library
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
package net.thevpc.commons.md;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMdWriter implements MdWriter{
    private Writer writer;


    public final void write(MdElement element) {
        writeImpl(element);
    }

    public abstract void writeImpl(MdElement element) ;

    public AbstractMdWriter(Writer writer) {
        this.writer = writer;
    }

    protected void write(String text){
        try {
            writer.write(text);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected void writeln(){
        try {
            writer.write("\n");
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    protected void writeln(String text){
        try {
            writer.write(text);
            writer.write("\n");
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
