/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.jshell;

/**
 *
 * @author vpc
 */
public interface JShellFileSystem {

    String normalizeWorkingDir(String cwd);

    String getDefaultWorkingDir();
    
    String getAbsolutePath(String path);

}