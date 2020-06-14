/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import java.io.Reader;
import java.util.function.Supplier;

/**
 *
 * @author vpc
 */
public interface JSource {
    String name();

    Reader reader();

    String text();

    char[] charArray();

    JCharRange range(int from, int to);
}
