/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class ZipUtils {

    public static void zip(String target, ZipOptions options, String... source) throws IOException {
        if (options == null) {
            options = new ZipOptions();
        }
        File targetFile = new File(target);
        File f = options.isTempFile() ? File.createTempFile("zip", ".zip") : targetFile;

        ZipOutputStream zip = null;
        FileOutputStream fW = null;
        try {
            fW = new FileOutputStream(f);
            zip = new ZipOutputStream(fW);
            try {
                if (options.isSkipRoots()) {
                    for (String s : source) {
                        File file1 = new File(s);
                        if (file1.isDirectory()) {
                            for (File file : file1.listFiles()) {
                                add("", file.getPath(), zip);
                            }
                        } else {
                            add("", file1.getPath(), zip);
                        }
                    }
                } else {
                    for (String s : source) {
                        add("", s, zip);
                    }
                }
            } finally {
                if (zip != null) {
                    zip.close();
                }
            }
        } finally {
            if (fW != null) {
                fW.close();
            }
        }
        if (options.isTempFile()) {
            if (!f.renameTo(targetFile)) {
                FileUtils.copy(f, targetFile, null);
            }
        }
    }

//    private static void zipDir(String dirName, String nameZipFile) throws IOException {
//        ZipOutputStream zip = null;
//        FileOutputStream fW = null;
//        fW = new FileOutputStream(nameZipFile);
//        zip = new ZipOutputStream(fW);
//        addFolderToZip("", dirName, zip);
//        zip.close();
//        fW.close();
//    }
    private static void add(String path, String srcFolder, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFolder);
        if (folder.isDirectory()) {
            addFolderToZip(path, srcFolder, zip);
        } else {
            addFileToZip(path, srcFolder, zip, false);
        }
    }

    private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFolder);
        if (folder.list().length == 0) {
            addFileToZip(path, srcFolder, zip, true);
        } else {
            for (String fileName : folder.list()) {
                if (path.equals("")) {
                    addFileToZip(folder.getName(), concatPath(srcFolder, fileName), zip, false);
                } else {
                    addFileToZip(concatPath(path, folder.getName()), srcFolder + "/" + fileName, zip, false);
                }
            }
        }
    }

    private static String concatPath(String a, String b) {
        if (a.endsWith("/")) {
            if (b.startsWith("/")) {
                return a + b.substring(1);
            } else {
                return a + b;
            }
        } else {
            if (b.startsWith("/")) {
                return a + b;
            } else {
                return a + "/" + b;
            }
        }
    }

    private static void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
        File folder = new File(srcFile);
        String pathPrefix = path;
        if (!pathPrefix.endsWith("/")) {
            pathPrefix = pathPrefix + "/";
        }
        if (!pathPrefix.startsWith("/")) {
            pathPrefix = "/" + pathPrefix;
        }
        if (flag) {
//            System.out.println("[FOLDER ]" + pathPrefix + folder.getName());
            zip.putNextEntry(new ZipEntry(pathPrefix + folder.getName() + "/"));
        } else {
            if (folder.isDirectory()) {
                addFolderToZip(pathPrefix, srcFile, zip);
            } else {
//                System.out.println("[FILE  ]" + pathPrefix + folder.getName() + " - " + srcFile);
                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(pathPrefix + folder.getName()));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }
            }
        }
    }

    public static boolean visitZipStream(InputStream zipFile, PathFilter possiblePaths, InputStreamVisitor visitor) throws IOException {
//        byte[] buffer = new byte[4 * 1024];

        //get the zip file content
        ZipInputStream zis = null;

        try {
            zis = new ZipInputStream(zipFile);
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();
            final ZipInputStream finalZis = zis;
            InputStream entryInputStream = new InputStream() {
                @Override
                public int read() throws IOException {
                    return finalZis.read();
                }

                @Override
                public int read(byte[] b) throws IOException {
                    return finalZis.read(b);
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return finalZis.read(b, off, len);
                }

                @Override
                public void close() throws IOException {
                    finalZis.closeEntry();
                }
            };

            while (ze != null) {

                String fileName = ze.getName();
                if (!fileName.endsWith("/")) {
                    if (possiblePaths.accept(fileName)) {
                        if (!visitor.visit(fileName, entryInputStream)) {
                            break;
                        }
                    }
                }
                ze = zis.getNextEntry();
            }
        } finally {
            if (zis != null) {
                zis.close();
            }
        }

        return false;
    }
}
