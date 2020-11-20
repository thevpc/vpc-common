/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author vpc
 */
public class Teee {

    public static void main(String[] args) throws IOException {
        File file = new File("/run/media/vpc/Seagate Backup Plus Drive/acs-jboss-2019-03-22-17_17_17.064.jmx-mon");
        InputStream s = new BufferedInputStream(new FileInputStream(file));
        int c = 0;
        long index = 0;
        while (((c = s.read()) == 0)) {
            //
            index++;
            if ((index % (1024*1024)) == 0) {
                System.out.println(index/(1024*1024));
            }
        }
        System.out.println("ok : " + index+"::"+c);
    }
}
