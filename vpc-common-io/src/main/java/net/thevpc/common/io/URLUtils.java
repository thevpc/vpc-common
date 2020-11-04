/**
 * ====================================================================
 * vpc-common-io : common reusable library for
 * input/output
 * <br>
 * is a new Open Source Package Manager to help install packages and libraries
 * for runtime execution. Nuts is the ultimate companion for maven (and other
 * build managers) as it helps installing all package dependencies at runtime.
 * Nuts is not tied to java and is a good choice to share shell scripts and
 * other 'things' . Its based on an extensible architecture to help supporting a
 * large range of sub managers / repositories.
 * <br>
 * Copyright (C) 2016-2020 thevpc
 * <br>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * <br>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <br>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.io;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class URLUtils {

    public static boolean isURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    public static String getURLProtocol(String path) {
        if (path == null) {
            return null;
        }
        int i = path.indexOf("://");
        if (i > 0) {
            String protocol = path.substring(0, i).trim();
            if (protocol.matches("[a-zA-Z0-9_-]+")) {
                return protocol;
            }
        }
        return null;
    }

    public static File toFile(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        if (url.getProtocol().equals("file")) {
            try {
                return Paths.get(url.toURI()).toFile();
            } catch (URISyntaxException ex) {
                throw new IOException(ex.toString());
            }
        }
        throw new IOException("Not a file  : " + url);
    }

    public static Path toPath(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        if (url.getProtocol().equals("file")) {
            try {
                return Paths.get(url.toURI());
            } catch (URISyntaxException ex) {
                throw new IOException(ex.toString());
            }
        }
        throw new IOException("Not a file  : " + url);
    }
    public static Path toFilePath(URL url) throws IOException {
        if (url == null) {
            return null;
        }
        if (url.getProtocol().equals("file")) {
            try {
                //double conversion to be sure it is a file!!
                return Paths.get(url.toURI()).toFile().toPath();
            } catch (URISyntaxException ex) {
                throw new IOException(ex.toString());
            }
        }
        throw new IOException("Not a file  : " + url);
    }

    public static String getURLFilePath(String path) {
        if (path == null) {
            return null;
        }
        int i = path.indexOf("://");
        if (i > 0) {
            return path.substring(i + 3);
        }
        return path;
    }

    public static URL toURL(File file) throws MalformedURLException {
        if (file == null) {
            return null;
        }
        return file.toURI().toURL();
    }

    public static boolean isFileURL(URL repositoryURL) {
        return "file".equalsIgnoreCase(repositoryURL.getProtocol());
    }

    public static boolean isFileURL(String repositoryURL) {
        return repositoryURL != null && repositoryURL.startsWith("file:");
    }

    public static String buildUrl(String url, String path) {
        if (!url.endsWith("/")) {
            if (path.startsWith("/")) {
                return url + path;
            } else {
                return url + "/" + path;
            }
        } else {
            if (path.startsWith("/")) {
                return url + path.substring(1);
            } else {
                return url + path;
            }
        }
    }

//    public static void main(String[] args) {
//        System.out.println(parseHttpDate("Wed, 21 Oct 2015 07:28:00 GMT"));
//    }
    public static Date parseHttpDate(String date) {
        if (date == null) {
            return null;
        }
        date = date.trim();
        if (date.length() == 0) {
            return null;
        }
        // Wed, 21 Oct 2015 07:28:00 GMT
        int d = Integer.parseInt(date.substring(5, 7));
        String Ms = date.substring(8, 11);
        int M = Calendar.JANUARY;
        switch (Ms.toLowerCase()) {
            case "jan": {
                M = Calendar.JANUARY;
                break;
            }
            case "feb": {
                M = Calendar.FEBRUARY;
                break;
            }
            case "mar": {
                M = Calendar.MARCH;
                break;
            }
            case "apr": {
                M = Calendar.APRIL;
                break;
            }
            case "may": {
                M = Calendar.MAY;
                break;
            }
            case "jun": {
                M = Calendar.JULY;
                break;
            }
            case "jul": {
                M = Calendar.JULY;
                break;
            }
            case "aug": {
                M = Calendar.AUGUST;
                break;
            }
            case "sep": {
                M = Calendar.SEPTEMBER;
                break;
            }
            case "oct": {
                M = Calendar.OCTOBER;
                break;
            }
            case "nov": {
                M = Calendar.NOVEMBER;
                break;
            }
            case "dec": {
                M = Calendar.DECEMBER;
                break;
            }
        }
        int y = Integer.parseInt(date.substring(12, 16));
        int h = Integer.parseInt(date.substring(17, 19));
        int m = Integer.parseInt(date.substring(20, 22));
        int s = Integer.parseInt(date.substring(23, 25));
        Calendar i = Calendar.getInstance();
        i.set(Calendar.MONTH, M);
        i.set(Calendar.YEAR, y);
        i.set(Calendar.HOUR_OF_DAY, h);
        i.set(Calendar.MINUTE, m);
        i.set(Calendar.SECOND, s);
        i.set(Calendar.MILLISECOND, 0);
        i.setTimeZone(TimeZone.getTimeZone("GMT"));
        return i.getTime();
    }

    public static URLHeaderInfo getURLHeader(URL url) throws IOException {
        if (url.getProtocol().equals("file")) {
            File f = toFile(url);
            URLHeaderInfo info = new URLHeaderInfo(url.toString());
            info.setContentLength(f.length());
            info.setLastModified(new Date(f.lastModified()));
            return info;
        }
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            final String f = conn.getHeaderField("Last-Modified");
            final URLHeaderInfo info = new URLHeaderInfo(url.toString());
            info.setContentType(conn.getContentType());
            info.setContentEncoding(conn.getContentEncoding());
            info.setContentLength(conn.getContentLengthLong());
//            info.setLastModified(parseHttpDate(f));
            long m = conn.getLastModified();
            info.setLastModified(m == 0 ? null : new Date(m));
            return info;
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    public static boolean isRemoteURL(String url) {
        if (url == null) {
            return false;
        }
        url = url.toLowerCase();
        return (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("ftp://"));
    }

    public static String getURLName(URL url) {
        String path = url.getFile();
        String name;
        int index = path.lastIndexOf('/');
        if (index < 0) {
            name = path;
        } else {
            name = path.substring(index + 1);
        }
        index = name.indexOf('?');
        if (index >= 0) {
            name = name.substring(0, index);
        }
        name = name.trim();
        return name;
    }
}
