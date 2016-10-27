/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.io;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class ZipOptions {
    private boolean skipRoots=false;
    private boolean tempFile=false;

    public ZipOptions() {
    }

    public boolean isSkipRoots() {
        return skipRoots;
    }

    public ZipOptions setSkipRoots(boolean skipRoots) {
        this.skipRoots = skipRoots;
        return this;
    }

    public boolean isTempFile() {
        return tempFile;
    }

    public ZipOptions setTempFile(boolean tempFile) {
        this.tempFile = tempFile;
        return this;
    }
    
    
}
