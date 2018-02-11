/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.strings;

/**
 *
 * @author vpc
 */
public class StringLocation {

    private int start;
    private int end;

    public StringLocation(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int length() {
        return end - start;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String substring(String line) {
        return line.substring(start, end);
    }

    public String extractFrom(String line) {
        return line.substring(0, start) + line.substring(end + 1);
    }

}
