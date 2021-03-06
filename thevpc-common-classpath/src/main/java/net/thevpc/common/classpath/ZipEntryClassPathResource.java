/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.classpath;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class ZipEntryClassPathResource implements ClassPathResource {

    private String path;
    private ZipEntry nextEntry;
    private ZipInputStream inputStream;

    public ZipEntryClassPathResource(String path, ZipEntry nextEntry, ZipInputStream inputStream) {
        this.path = path;
        this.nextEntry = nextEntry;
        this.inputStream = inputStream;
    }

    public String getPath() {
        return path;
    }

    public InputStream open() throws IOException {
        return inputStream;
    }

    @Override
    public String toString() {
        return path;
    }

}
