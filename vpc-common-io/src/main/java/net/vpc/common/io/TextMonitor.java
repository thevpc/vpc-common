/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        return new TextMonitorIterator();
    }

    public void process() throws IOException {
        for (String line : this) {
            //do nothing!
        }
    }

    private class TextMonitorIterator implements Iterator<String> {
        private BufferedReader input = null;
        private String currentLine = null;

        {
            try {
                input = new BufferedReader(new InputStreamReader(TextMonitor.this.source.open()));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
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
