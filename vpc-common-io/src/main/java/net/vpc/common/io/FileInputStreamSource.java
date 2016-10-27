/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//    public static void main(String[] args) {

////        System.out.println(getPathInfo("/home/vpc/Data/eniso/teaching-load/2014-2015/generated/v12.zip"));
//        try {
//            zip("/home/vpc/v15.zip", new ZipOptions().setSkipRoots(true), "/home/vpc/Data/eniso/teaching-load/2014-2015/generated/v15");
//        } catch (IOException ex) {
//            Logger.getLogger(IOUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
class FileInputStreamSource implements InputStreamSource {
    private final File file;

    public FileInputStreamSource(File file) {
        this.file = file;
    }

    @Override
    public InputStream open() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public Object getSource() throws IOException {
        return file;
    }
    
}
