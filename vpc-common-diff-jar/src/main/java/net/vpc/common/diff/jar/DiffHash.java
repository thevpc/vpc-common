/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.diff.jar;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author vpc
 */
public interface DiffHash {
    String hash(InputStream inputStream) throws IOException;
}
