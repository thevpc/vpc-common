package net.vpc.common.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLog extends Log {

    public static final String SEQUENCE_PATTERN = "<SEQ>";
    public static final String DATE_PATTERN = "<DATE>";
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss");
    private PrintStream writer;
    private long maxLogFile;
    private long currentFileSize;
    private File currentFile;
    private String fileName;

    public FileLog(String name, String fileName) {
        this(name, name, name, DEFAULT_PATTERN, null, fileName, 100);
    }

    public FileLog(String name, String title, String desc, String pattern,
            String[] acceptedTypes, String fileName, int maxLogFileMega) {
        super(name, title, desc, "File", pattern, acceptedTypes);
        currentFileSize = 0L;
        this.fileName = fileName;
        if (fileName == null) {
            throw new IllegalArgumentException("Null file pattern");
        } else {
            String s = getDefaultFileName();
            if (s.length() == 0) {
                throw new IllegalArgumentException("Empty file pattern");
            }
            int s1 = fileName.indexOf(SEQUENCE_PATTERN);
            int s2 = fileName.lastIndexOf(SEQUENCE_PATTERN);
            int d1 = fileName.indexOf(DATE_PATTERN);
            int d2 = fileName.lastIndexOf(DATE_PATTERN);

//			if (s1 < 0 && d1 < 0) {
//				fileName = fileName + SEQUENCE_PATTERN;
            // throw new IllegalArgumentException("file pattern must include
            // either "+SEQUENCE_PATTERN+" or "+DATE_PATTERN);
//			}
            if ((s1 >= 0 && d1 >= 0) || (s1 >= 0 && s1 != s2)
                    || (d1 >= 0 && d1 != d2)) {
                throw new IllegalArgumentException(
                        "file pattern must include only one occurence of "
                        + SEQUENCE_PATTERN + " or " + DATE_PATTERN);
            }
        }
        this.maxLogFile = maxLogFileMega * 1024 * 1024;
    }

    public String getFileNameMask() {
        return fileName;
    }

    public void processLog(String type, String message, int logLevel,
            Date date, long delta, Thread thread, StackTrace stack,
            String user_id, String user_name) {
        if (writer==null || !currentFile.exists() || (currentFileSize > maxLogFile)) {
            updateWriter();
        }
        if (writer == null) {
            System.out.println("writer is null for " + getName()
                    + " cuurent dir ");
            System.out.println("   --> log file name is " + fileName);
            System.out.println("   --> current dir is "
                    + getBestPathFor(new File(".")));
        } else {
            PrintStream ps = writer;
            ps.println(buildMessage(type, message, logLevel, date, delta,
                    thread, stack, getName(), getPattern(), user_id,
                    user_name));
            // ps.flush();
            currentFileSize += (message.length() + 1);
        }
    }

    public void loadConfig() {
        super.loadConfig();
        if (Log.getConfig() != null) {
            setMaxFileSize(Log.getConfig().getInt(
                    getConfigurationKey("max_file_size"), getMaxFileSize()));
        }
    }

    public void activate() {
        updateWriter();
    }

    public int getMaxFileSize() {
        return (int) (maxLogFile / (1024 * 1024));
    }

    public void setMaxFileSize(int size) {
        maxLogFile = size * 1024 * 1024;
        if (this.currentFileSize > maxLogFile) {
            updateWriter();
        }
        if (Log.getConfig() != null) {
            Log.getConfig().setInt(
                    getConfigurationKey("max_file_size"), getMaxFileSize());
        }
    }

    protected synchronized void updateWriter() {
        if (writer != null) {
            try {
                writer.close();
                writer = null;
            } catch (Exception e) {
            }
        }
        String dfn = null;
        try {
            String cfn = new File(computeFileName()).getCanonicalPath();
            dfn = new File(getDefaultFileName()).getCanonicalPath();
            currentFile = new File(dfn);
            if (!(currentFile).exists()) {
                File p = currentFile.getParentFile();
                if(p!=null){
                    p.mkdirs();
                }
                currentFile.createNewFile();
            }
            if (!cfn.equals(dfn)) {
                copy(currentFile, new File(cfn), 512);
                (currentFile).delete();
            }
            writer = new PrintStream(new FileOutputStream(currentFile, true));
            currentFileSize = 0L;
        } catch (Exception e) {
            throw new RuntimeException("can't setWriter (" + dfn + ") : " + e);
        }
    }

    private void copy(File is, File os, int size) throws IOException,
            FileNotFoundException {
        FileInputStream fis = new FileInputStream(is);
        FileOutputStream fos = new FileOutputStream(os);
        try {
            int r;
            byte[] b = new byte[size];
            while ((r = fis.read(b)) > 0) {
                fos.write(b, 0, r);
            }
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public synchronized void clear() {
        if (writer != null) {
            try {
                writer.close();
                writer = null;
            } catch (Exception e) {
            }
        }
        int loopTest = 0;
        String dfn = getDefaultFileName();
        String comp;
        do {
            comp = computeFileName();
            (new File(comp)).delete();
        } while (!comp.equals(dfn) && ++loopTest < 1000);
        try {
            currentFile = new File(dfn);
            writer = new PrintStream(new FileOutputStream(currentFile, false));
            currentFileSize = 0L;
        } catch (Exception e) {
            throw new RuntimeException("can't setWriter (" + dfn + "): " + e);
        }
    }

    protected String computeFileName() {
        String goodFile = getDefaultFileName();
        int index = 0;
        Date date = new Date();
        File f = new File(goodFile);
        do {
            if (f.exists() && f.length() >= maxLogFile) {
                String d = fileName;
                d = d.replace(DATE_PATTERN, DATE_FORMAT.format(date));
                d = d.replace(SEQUENCE_PATTERN, String.valueOf(index));
                // StringBuffer sb = new StringBuffer();
                // boolean ok = false;
                // for (int i = 0; i < fileName.length(); i++)
                // switch (fileName.charAt(i)) {
                // case 63: // '?'
                // sb.append(index);
                // ok = true;
                // break;
                //
                // case 42: // '*'
                // sb.append(DATE_FORMAT.format(date));
                // ok = true;
                // break;
                //
                // default:
                // sb.append(fileName.charAt(i));
                // break;
                // }
                //
                // goodFile = sb.toString();
                // if (!ok)
                // goodFile = goodFile + index;
                f = new File(d);
                index++;
            } else {
                return f.getAbsolutePath();
            }
        } while (true);
    }

    public String getDefaultFileName() {
        String d = fileName;
        d = d.replace(DATE_PATTERN, "");
        d = d.replace(SEQUENCE_PATTERN, "");
        return d;
        // StringBuffer sb = new StringBuffer();
        // for (int i = 0; i < fileName.length();)
        // switch (fileName.charAt(i)) {
        // default:
        // sb.append(fileName.charAt(i));
        // // fall through
        //
        // case 42: // '*'
        // case 63: // '?'
        // i++;
        // break;
        // }
        //
        // return sb.toString();
    }

    public void passivate() {
        try {
            writer.flush();
            writer.close();
            writer = null;
        } catch (Exception e) {
        }
    }

    private String getBestPathFor(File f) {
        try {
            return f.getCanonicalPath();
        } catch (IOException e) {
            return f.getAbsolutePath();
        }
    }
}
