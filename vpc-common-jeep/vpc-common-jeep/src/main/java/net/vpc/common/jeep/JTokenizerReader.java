/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

/**
 *
 * @author vpc
 */
public interface JTokenizerReader {

    int read();

    void unread(char c);

    void unread(char[] c);
}
