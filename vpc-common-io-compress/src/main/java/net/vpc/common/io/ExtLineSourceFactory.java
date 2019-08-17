package net.vpc.common.io;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

public class ExtLineSourceFactory implements LineSourceFactory {

    @Override
    public int getSupport(String fileName, String mimetype) {
//        if(mimetype!=null){
//            if("application/tar+gzip".equals(mimetype)){
//                return 1;
//            }
//            if("application/gzip".equals(mimetype)){
//                return 1;
//            }
//        }
        if (fileName != null) {
            if (".tar.gz".equals(mimetype)) {
                return 1;
            }
        }
        return -1;
    }

    @Override
    public LineSource createLineSource(InputStream stream, String mimeType) {
        throw new IllegalArgumentException("Not yet supported");
    }

    @Override
    public LineSource createLineSource(File file) {
        return new TarGzLineReader(file);
    }

    @Override
    public LineSource createLineSource(Path file) {
        return new TarGzLineReader(file.toFile());
    }

}
