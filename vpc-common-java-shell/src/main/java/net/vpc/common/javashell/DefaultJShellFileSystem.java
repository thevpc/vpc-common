/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.javashell;

import java.io.File;
import net.vpc.common.javashell.util.ShellUtils;

/**
 *
 * @author vpc
 */
public class DefaultJShellFileSystem implements JShellFileSystem {

    @Override
    public String getDefaultWorkingDir() {
        return System.getProperty("user.dir");
    }

    @Override
    public String normalizeWorkingDir(String cwd) {
        File f = new File(cwd);
        File f2 = null;
        if (f.isAbsolute()) {
            f2 = f;
        } else {
            f2 = new File(new File(cwd), f.getPath());
        }
        if (f2.exists() && f2.isDirectory()) {
            return cwd;
        } else {
            throw new RuntimeException("Unable to change to " + f2.getPath());
        }
    }

    @Override
    public String getAbsolutePath(String path) {
        return ShellUtils.getAbsolutePath(path);
    }
}
