/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.strings;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public interface StringTransform {

    public String transform(String s);

    public StringTransform apply(final StringTransform base);

}
