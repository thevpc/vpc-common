/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.filetemplate;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author thevpc
 */
public interface StreamExecutor {
    public Object execute(InputStream source, OutputStream target, FileTemplater context) ;
}
