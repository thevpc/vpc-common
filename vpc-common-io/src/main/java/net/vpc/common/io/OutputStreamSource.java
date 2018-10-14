/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public interface OutputStreamSource {

    OutputStream open() throws IOException;

    Object getSource() throws IOException;
}
