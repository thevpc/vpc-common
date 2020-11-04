/**
 * ====================================================================
 *                        vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * ====================================================================
 */
package net.thevpc.common.prs;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 11 avr. 2007 17:46:14
 */
public final class Version implements Comparable<Version>, Serializable {

    private String value;

    public static Map<String, Version> loadVersions(URL url) throws IOException {
        Map<String, Version> h = new HashMap<String, Version>();
        BufferedReader b = null;
        try {
            b = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = b.readLine()) != null) {
                line = line.trim();
                StringBuilder sb = new StringBuilder(20);
                if (line.startsWith("--") || line.startsWith("//") || line.startsWith("#")) {
                    line = "";
                }
                int i = 0;
                boolean comments = false;
                while (i < line.length()) {
                    if (comments) {
                        if (line.charAt(i) == '/' && i > 1 && line.charAt(i - 1) == '*') {
                            comments = false;
                        }
                    } else {
                        if (line.charAt(i) == '*' && i > 1 && line.charAt(i - 1) == '/') {
                            sb.delete(sb.length() - 1, sb.length());
                            comments = true;
                        } else {
                            sb.append(line.charAt(i));
                        }
                    }
                    i++;
                }
                line = sb.toString().trim();
                int x = line.indexOf('=');
                if (x < 0) {
                    h.put("#DEFAULT#", new Version(line));
                } else {
                    String id = line.substring(0, x).trim();
                    String val = line.substring(x + 1).trim();
                    h.put(id, new Version(val));
                }
            }
            return h;
        } finally {
            if (b != null) {
                try {
                    b.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    /**
     * never use this constructor
     */
    private Version() {
    }

    public Version(URL url, String id) throws IOException {
        Map<String, Version> versions = loadVersions(url);
        if (id == null) {
            if (versions.size() > 0) {
                Version[] versions1 = versions.values().toArray(new Version[versions.size()]);
                this.value = versions1[0].value;
            }
        } else {
            Version version = versions.get(id);
            this.value = version == null ? "" : version.value;
        }
    }

    public Version(String value) {
        this.value = value;
    }

    public void store(File out, String id, String desc) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(out);
            store(os, id, desc);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public void store(OutputStream out, String id, String desc) {
        PrintStream ps = new PrintStream(out);
        ps.println("#" + desc);
        ps.println(id + " = " + value);
    }

    public int compareTo(Version o) {
        if (o == null) {
            return 1;
        }
        String[] v10 = toArray();
        String[] v20 = o.toArray();
        for (int i = 0; i < v10.length; i++) {
            if (v20.length <= i) {
                return 1;
            }
            int x = 0;
            int a = 0;
            int b = 0;
            boolean aInt = false;
            boolean bInt = false;
            try {
                a = Integer.parseInt(v10[i]);
                aInt = true;
            } catch (NumberFormatException nfe) {
                //
            }
            try {
                b = Integer.parseInt(v20[i]);
                bInt = true;
            } catch (NumberFormatException nfe) {
                //
            }
            if (aInt && bInt) {
                x = a - b;
            } else if (aInt) {
                //we suppose that integer is ALWAYS greater than alpha
                x = 1;
            } else if (bInt) {
                //we suppose that integer is ALWAYS greater than alpha
                x = -1;
            } else {
                //we do lexycographic comparaison
                //ideally ALPHA<BETA<RC<final
                x = v10[i].compareTo(v20[i]);
            }
            if (x != 0) {
                return x;
            }
        }
        if (v20.length > v10.length) {
            return -1;
        }
        return 0;
    }

    public String[] toArray() {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> a = new ArrayList<String>();
        int i = 0;
        while (i < value.length()) {
            char c = value.charAt(i++);
            if (c != ' ') {
                if (Character.isLetterOrDigit(c)) {
                    if (sb.length() == 0) {
                        sb.append(c);
                    } else {
                        char old = sb.charAt(0);
                        if (Character.isDigit(c) != Character.isDigit(old)) {
                            a.add(sb.toString());
                            sb.delete(0, sb.length());
                        }
                        sb.append(c);
                    }
                } else {
                    if (sb.length() > 0) {
                        a.add(sb.toString());
                        sb.delete(0, sb.length());
                    }
                }
            }
        }
        if (sb.length() > 0) {
            a.add(sb.toString());
        }
        return a.toArray(new String[a.size()]);
    }

    public Version increment() {
        String[] varray = toArray();
        for (int i = varray.length - 1; i >= 0; i--) {
            String s = varray[i];
            try {
                int build = Integer.parseInt(s);
                build++;
                return deriveVersion(i, String.valueOf(build));
            } catch (NumberFormatException e) {
                //
            }
        }
        throw new IllegalArgumentException("How do you increment such version '" + this + "' ???");
    }

    public Version deriveVersion(int position, String newValue) {
        StringBuilder fullValue = new StringBuilder();
        int cposition = 0;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < value.length()) {
            char c = value.charAt(i++);
            if (c != ' ') {
                if (Character.isLetterOrDigit(c)) {
                    if (sb.length() == 0) {
                        sb.append(c);
                    } else {
                        char old = sb.charAt(0);
                        if (Character.isDigit(c) != Character.isDigit(old)) {
                            if (cposition == position) {
                                fullValue.append(newValue);
                            } else {
                                fullValue.append(sb.toString());
                            }
                            sb.delete(0, sb.length());
                            sb.append(c);
                            cposition++;
                        } else {
                            sb.append(c);
                        }
                    }
                } else {
                    if (sb.length() > 0) {
                        if (cposition == position) {
                            fullValue.append(newValue);
                        } else {
                            fullValue.append(sb.toString());
                        }
                        sb.delete(0, sb.length());
                        cposition++;
                    }
                    fullValue.append(c);
                }
            } else {
                if (sb.length() > 0) {
                    if (cposition == position) {
                        fullValue.append(newValue);
                    } else {
                        fullValue.append(sb.toString());
                    }
                    sb.delete(0, sb.length());
                    cposition++;
                }
                fullValue.append(c);
            }
        }
        if (sb.length() > 0) {
            if (cposition == position) {
                fullValue.append(newValue);
            } else {
                fullValue.append(sb.toString());
            }
        }
        return new Version(fullValue.toString());
    }

    public boolean isEmpty() {
        return value.length() == 0;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Version other = (Version) obj;
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    public static Version parse(String str) {
        return (str == null || str.trim().length() == 0) ? null : new Version(str);
    }

    public Version head(int count) {
        StringBuilder sb = new StringBuilder();
        String[] a = toArray();
        int xx = count > a.length ? a.length : count;
        boolean first = true;
        for (int i = 0; i < xx; i++) {
            if (first) {
                first = false;
            } else {
                sb.append(".");
            }
            sb.append(a[i]);
        }
        return new Version(sb.toString());
    }


    public Version tail(int count) {
        StringBuilder sb = new StringBuilder();
        String[] a = toArray();
        int xx = count > a.length ? a.length : count;
        boolean first = true;
        for (int i = 0; i < xx; i++) {
            if (first) {
                first = false;
            } else {
                sb.append(".");
            }
            sb.append(a[a.length - xx + i]);
        }
        return new Version(sb.toString());
    }
}
