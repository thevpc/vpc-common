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

import java.io.PrintStream;
import java.util.Locale;

public class NullPrintStream extends PrintStream {

    public static final PrintStream INSTANCE = new NullPrintStream();

    private NullPrintStream() {
        super(NullOutputStream.INSTANCE);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean checkError() {
        return false;
    }

    @Override
    public void write(int b) {

    }

    @Override
    public void write(byte[] buf, int off, int len) {
    }

    @Override
    public void print(boolean b) {
    }

    @Override
    public void print(char c) {
    }

    @Override
    public void print(int i) {
    }

    @Override
    public void print(long l) {
    }

    @Override
    public void print(float f) {
    }

    @Override
    public void print(double d) {
    }

    @Override
    public void print(char[] s) {
    }

    @Override
    public void print(String s) {
    }

    @Override
    public void print(Object obj) {
    }

    @Override
    public void println() {
    }

    @Override
    public void println(boolean x) {
    }

    @Override
    public void println(char x) {
    }

    @Override
    public void println(int x) {
    }

    @Override
    public void println(long x) {
    }

    @Override
    public void println(float x) {
    }

    @Override
    public void println(double x) {
    }

    @Override
    public void println(char[] x) {
    }

    @Override
    public void println(String x) {
    }

    @Override
    public void println(Object x) {
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        return this;
    }

    @Override
    public PrintStream printf(Locale l, String format, Object... args) {
        return this;
    }

    @Override
    public PrintStream append(CharSequence csq) {
        return this;
    }

    @Override
    public PrintStream append(CharSequence csq, int start, int end) {
        return this;
    }

    @Override
    public PrintStream append(char c) {
        return this;
    }
}
