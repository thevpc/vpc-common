/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.cron.test;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.vpc.common.cron.Crontab;

/**
 *
 * @author vpc
 */
public class TestCron {

    public static void main(String[] args) {
        Crontab t = new Crontab();
        t.add("* * * * *", () -> System.out.println(new Date()));
        t.start();
        synchronized(t){
            try {
                t.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(TestCron.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
