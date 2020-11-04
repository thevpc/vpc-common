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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author vpc
 */
public class TextMonitor implements Iterable<String> {

    private List<TextListener> listeners = new ArrayList<TextListener>();
    private int periodInSeconds = 5;
    private boolean closed;
    private InputStreamSource source;

    public TextMonitor(InputStreamSource source) {
        this.source = source;
    }

//    public static void main(String[] args) {
//        try {
//            TextMonitor tail = new TextMonitor(IOUtils.toInputStreamSource(new File("/home/vpc/.IntelliJIdea2017.3/system/tomcat/Unnamed_vain-ruling/logs/catalina.2017-12-24.log"))
//            );
//            tail.addListener(new TextListener() {
//                @Override
//                public void onText(TextEvent event) {
//                    System.out.println(event.getLine());
//                }
//            });
//            tail.process();
//        } catch (IOException ex) {
//            Logger.getLogger(TextMonitor.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    public void addListener(TextListener listener) {
        listeners.add(listener);
    }

    public void removeListener(TextListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Iterator<String> iterator() {
        try {
            return new TextMonitorIterator();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public void process() throws IOException {
        for (String line : this) {
            //do nothing!
        }
    }

    private class TextMonitorIterator implements Iterator<String> {

        private BufferedReader input = null;
        private String currentLine = null;

        public TextMonitorIterator() throws IOException {
            input = new BufferedReader(new InputStreamReader(TextMonitor.this.source.open()));
        }

        @Override
        public boolean hasNext() {
            while (true) {
                try {
                    currentLine = input.readLine();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
                if (currentLine != null) {
                    return true;
                }
                if (closed) {
                    return false;
                }
                try {
                    Thread.sleep(periodInSeconds * 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    closed = true;
                    return false;
                }
            }
        }

        @Override
        public String next() {
            if (currentLine != null) {
                for (TextListener listener : listeners) {
                    TextEvent event = new TextEvent(this, currentLine);
                    listener.onText(event);
                }
            }
            return currentLine;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        @Override
        protected void finalize() throws Throwable {
            if (input != null) {
                input.close();
            }
        }
    }
}
