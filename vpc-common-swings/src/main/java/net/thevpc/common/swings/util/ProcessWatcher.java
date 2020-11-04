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
package net.thevpc.common.swings.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * <pre>
 *      Process process = Runtime.getRuntime().exec(new String[]{"/bin/java","-version"}, null, new File("."));
 *      ProcessWatcher w = new ProcessWatcher(process, new ProcessWatcherHandler() {
 *          public void started(Process process) {
 *              System.out.println("Prcess started");
 *          }
 *
 *          public void stdout(Process process, String line) {
 *              System.out.println(line);
 *          }
 *
 *          public void stderr(Process process, String line) {
 *              System.err.println(line);
 *          }
 *
 *          public void ended(Process process, int value) {
 *              System.out.println("Process Shutdown. Exit Value :" + value);
 *          }
 *
 *          public void error(Process process, Throwable th) {
 *              System.err.println(th);
 *          }
 *      });
 *      w.start();
 * </pre>
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 27 juin 2007 12:08:13
 */
public class ProcessWatcher {
    private Process process;
    private Thread end;
    private Thread out;
    private Thread err;
    private int result;
    private boolean stopped=false;
    private ProcessWatcherHandler handler;


    public ProcessWatcher(Process theProcess, ProcessWatcherHandler theHandler) {
        this.process = theProcess;
        this.handler = theHandler;
        end=new Thread(){
            @Override
            public void run() {
                try {
                    result = process.waitFor();
                    handler.ended(process,result);
                } catch (Throwable e) {
                    handler.error(process,e);
                } finally{
                    stopped=true;
                }
            }
        };
        out=new Thread(){
            @Override
            public void run() {
                String read;
                BufferedReader in=new BufferedReader(new InputStreamReader(process.getInputStream()));
                while (!stopped) {
                    try {
                        read = in.readLine();
                        if (read == null) {
                            break;
                        }
                        handler.stdout(process,read);
                    } catch (Throwable e) {
                        handler.error(process,e);
                        break;
                    }
                }
            }
        };
        err=new Thread(){
            @Override
            public void run() {
                String read;
                BufferedReader in=null;
                try {
                    in = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    while (!stopped) {
                        try {
                            read = in.readLine();
                            if (read == null) {
                                break;
                            }
                            handler.stderr(process,read);
                        } catch (Throwable e) {
                            handler.error(process,e);
                            break;
                        }
                    }
                } catch(Throwable e) {
                    handler.error(process,e);
                } finally {
                    if (in!=null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            handler.error(process,e);
                        }
                    }
                }
            }
        };
    }

    public void start(){
        handler.started(process);
        end.start();
        out.start();
        err.start();
    }

    public int waitfor(){
        while(!stopped){
            Thread.yield();
        }
        return result; 
    }
}
