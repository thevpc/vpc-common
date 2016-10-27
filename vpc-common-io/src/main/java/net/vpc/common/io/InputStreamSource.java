/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public interface InputStreamSource {
    public InputStream open() throws  IOException;
    public Object getSource() throws  IOException;
}
