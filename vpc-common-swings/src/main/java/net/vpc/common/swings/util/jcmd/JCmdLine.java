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
package net.vpc.common.swings.util.jcmd;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public class JCmdLine {
    public static final int ERR_UNKNOWN_CMD_ARG = 99001;
    public static final String OPTION_CMD_START = "#";
    public static final String OPTION_START = "-";
    public static final String OPTION_LONG_START = "--";
    public static final String OPTION_NEGATE = "!";
    public static final String OPTION_SEPARATOR = ":=";
    public static final String CRLF = "\r\n";
    public String HELP1_OPTION = "help";
    public String HELP2_OPTION = "?";
    public String HELP3_OPTION = "help?";
    private boolean exitIfHelp;
    private boolean exitIfError;
    private String helpContent;
    private Object caller;
    private ArrayList<CmdArg> arguments;
    public String optionStart = OPTION_START;
    public String optionLongStart = OPTION_LONG_START;
    public String optionCmdStart = OPTION_CMD_START;
    public String optionNegate = OPTION_NEGATE;
    public String optionSeparator = OPTION_SEPARATOR;
    private boolean caseSensitive;
    private LinkedHashMap<String, ExpectOption> expectedOptions;

    public JCmdLine() {
        exitIfHelp = true;
        exitIfError = true;
        helpContent = null;
        caller = null;
        arguments = new ArrayList<CmdArg>();
    }

    public JCmdLine(String[] args) {
        this(null, args);
    }

    public JCmdLine(String defaultAguentsFile, String[] args) {
        this();
        if (defaultAguentsFile != null) {
            File file = new File(defaultAguentsFile);
            if (file.exists() && file.isFile()) {
                try {
                    loadArgumentsFromFile(defaultAguentsFile);
                } catch (IOException e) {
                    System.err.println(e.toString());
                }
            }
        }
        if (args != null) {
            addArguments(args);
        }
    }

    public void addExpectedOption(ExpectOption expectOption) {
        if (expectedOptions == null) {
            expectedOptions = new LinkedHashMap<String, ExpectOption>();
        }
        if (expectOption.getShortName() != null && expectedOptions.containsKey(isCaseSensitive() ? expectOption.getShortName() : expectOption.getShortName().toLowerCase()))
        {
            throw new IllegalArgumentException("CmdOption " + expectOption.getShortName() + " already defined");
        } else {
            expectedOptions.put(isCaseSensitive() ? expectOption.getShortName() : expectOption.getShortName().toLowerCase(), expectOption);
        }
        if (expectOption.getLongName() != null && expectedOptions.containsKey(isCaseSensitive() ? expectOption.getLongName() : expectOption.getLongName().toLowerCase()))
        {
            throw new IllegalArgumentException("CmdOption " + expectOption.getLongName() + " already defined");
        } else {
            expectedOptions.put(isCaseSensitive() ? expectOption.getLongName() : expectOption.getLongName().toLowerCase(), expectOption);
        }
    }

    public ExpectOption getExpectedArgument(String shortOrLongName) {
        if (expectedOptions != null) {
            return expectedOptions.get(shortOrLongName);
        }
        return null;
    }

    public void validate() {
        if (expectedOptions != null) {
            for (CmdArg arg : arguments) {
                if (arg instanceof CmdOption) {
                    CmdOption cmdOption = (CmdOption) arg;
                    cmdOption.validate();
                }
            }
        }
    }

    public CmdOption getOption(int i) {
        return (CmdOption) arguments.get(i);
    }

    public CmdParam getParameter(int i) {
        return (CmdParam) arguments.get(i);
    }

    public CmdArg getArgument(int i) {
        return arguments.get(i);
    }


    public boolean isAnySelectedOption(String shortName, String longName) {
        return isAnySelectedOption(shortName, longName, false, isCaseSensitive(), false);
    }

    public boolean isAnySelectedOption(String shortName, String longName, boolean required) {
        return isAnySelectedOption(shortName, longName, required, isCaseSensitive(), false);
    }

    public boolean isAnySelectedOption(String shortName, String longName, boolean required, boolean caseSensitive) {
        return isAnySelectedOption(shortName, longName, required, caseSensitive, false);
    }

    public boolean isAnySelectedOption(String shortName, String longName, boolean required, boolean caseSensitive, boolean defaultValue) {
        CmdOption cmdOption = getAnyOption(shortName, longName, required, caseSensitive);
        if (cmdOption != null) {
            return cmdOption.isSelected();
        }
        return defaultValue;
    }

    public String getAnyOptionValue(String shortName, String longName) {
        return getAnyOptionValue(shortName, longName, false, isCaseSensitive(), null);
    }

    public String getAnyOptionValue(String shortName, String longName, boolean required) {
        return getAnyOptionValue(shortName, longName, required, isCaseSensitive(), null);
    }

    public String getAnyOptionValue(String shortName, String longName, boolean required, boolean caseSensitive) {
        return getAnyOptionValue(shortName, longName, required, caseSensitive, null);
    }

    public String getAnyOptionValue(String shortName, String longName, boolean required, boolean caseSensitive, String defaultValue) {
        CmdOption cmdOption = getAnyOption(shortName, longName, required, caseSensitive);
        if (cmdOption != null) {
            return cmdOption.getValue();
        }
        return defaultValue;
    }

    public CmdOption getAnyOption(String shortName, String longName) {
        return getAnyOption(shortName, longName, false, isCaseSensitive());
    }

    public CmdOption getAnyOption(String shortName, String longName, boolean required) {
        return getAnyOption(shortName, longName, required, isCaseSensitive());
    }

    public CmdOption getAnyOption(String shortName, String longName, boolean required, boolean caseSensitive) {
        CmdOption cmdOption;
        if (shortName != null) {
            cmdOption = getOption(shortName, false, caseSensitive);
            if (cmdOption != null && cmdOption.isShortOption()) {
                return cmdOption;
            }
        }
        if (longName != null) {
            cmdOption = getOption(longName, required, caseSensitive);
            if (cmdOption != null && !cmdOption.isShortOption()) {
                return cmdOption;
            }
        }
        if (required) {
            throw new IllegalArgumentException("cmdOption " +
                    (shortName != null ? ("'" + optionStart + shortName + "'") : "") +
                    ((shortName != null && longName != null) ? (" or ") : "") +
                    (longName != null ? ("'" + optionLongStart + longName + "'") : "") +
                    " required");
        }
        return null;
    }

    public String getOptionValue(String name) {
        return getOptionValue(name, false, isCaseSensitive(), null);
    }

    public String getOptionValue(String name, boolean required) {
        return getOptionValue(name, required, isCaseSensitive(), null);
    }

    public String getOptionValue(String name, boolean required, boolean caseSensitive) {
        return getOptionValue(name, required, caseSensitive, null);
    }

    public String getOptionValue(String name, boolean required, boolean caseSensitive, String defaultValue) {
        CmdOption cmdOption = getOption(name, required, caseSensitive);
        if (cmdOption != null) {
            return cmdOption.getValue();
        }
        return defaultValue;
    }

    public boolean isSelectedOption(String name) {
        return isSelectedOption(name, false, isCaseSensitive(), false);
    }

    public boolean isSelectedOption(String name, boolean required) {
        return isSelectedOption(name, required, isCaseSensitive(), false);
    }

    public boolean isSelectedOption(String name, boolean required, boolean caseSensitive) {
        return isSelectedOption(name, required, caseSensitive, false);
    }

    public boolean isSelectedOption(String name, boolean required, boolean caseSensitive, boolean defaultValue) {
        CmdOption cmdOption = getOption(name, required, caseSensitive);
        if (cmdOption != null) {
            return cmdOption.isSelected();
        }
        return defaultValue;
    }

    public CmdOption getOption(String name) {
        return getOption(name, false, isCaseSensitive());
    }

    public CmdOption getOption(String name, boolean required) {
        return getOption(name, required, isCaseSensitive());
    }

    public CmdOption getOption(String name, boolean required, boolean caseSensitive) {
        for (int i = arguments.size() - 1; i >= 0; i--) {
            CmdArg a = arguments.get(i);
            if (a instanceof CmdOption) {
                CmdOption o = (CmdOption) a;
                o.setUsed(true);
                if (name.equals(o.getName()) || (!caseSensitive && name.equalsIgnoreCase(o.getName()))) {
                    return o;
                }
            }
        }
        if (required) {
            throw new IllegalArgumentException("option '" + name + "' required");
        }
        return null;
    }

    public Map<String,String> getOptionValuesMap() {
        Map<String, String> h = new HashMap<String, String>();
        int max = arguments.size();
        for (int i = 0; i < max; i++) {
            CmdArg a = arguments.get(i);
            if (a instanceof CmdOption) {
                CmdOption o = (CmdOption) a;
                if (o.isBoolean()) {
                    h.put(o.getName(), o.isSelected() ? "true" : "false");
                } else {
                    h.put(o.getName(), o.getValue());
                }
            }
        }
        return h;
    }

    public int size() {
        return arguments.size();
    }


    public String getHelp() {
        if (helpContent != null) {
            try {
                return getString(helpContent);
            } catch (IOException ee) {
                return "Help file was not found(" + helpContent + ")";
            }
        }
        if (caller == null) {
            return "No Help Provided";
        }
        Class cl = caller.getClass();
        StringBuilder detailsString = new StringBuilder("SYNTAXE: " + cl.getName() + " [options] params...\n\nWHERE options ARE IN :\n");
        Field[] fields = cl.getFields();
        for (Field field : fields) {
            try {
                if (Modifier.isPublic(field.getModifiers())) {
                    Class fc = field.getType();
                    String fn = field.getName();
                    Object fv = field.get(caller);
                    detailsString.append("\t").append(fn).append("\t:").append(fc).append(" (default ").append(fv).append(" )\n");
                }
            } catch (IllegalAccessException ee) {
                System.out.println(ee);
            }
        }

        return detailsString.toString();
    }

    public Object getObjectFromString(Class c, String value) {
        if (c.equals(String.class) || c.equals(Object.class)) {
            return value;
        }
        if (c.equals(Boolean.class) || c.equals(Boolean.TYPE)) {
            return new Boolean(value);
        }
        if (c.equals(Long.class) || c.equals(Long.TYPE)) {
            return new Long(value);
        }
        if (c.equals(Integer.class) || c.equals(Integer.TYPE)) {
            return new Integer(value);
        }
        if (c.equals(Short.class) || c.equals(Short.TYPE)) {
            return new Short(value);
        }
        if (c.equals(Byte.class) || c.equals(Byte.TYPE)) {
            return new Byte(value);
        }
        if (c.equals(Float.class) || c.equals(Float.TYPE)) {
            return new Float(value);
        }
        if (c.equals(Double.class) || c.equals(Double.TYPE)) {
            return new Double(value);
        }
        if (c.equals(BigInteger.class)) {
            return new BigInteger(value);
        }
        if (c.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }
        try {
            Constructor cc = c.getConstructor(new Class[]{
                    String.class
            });
            return cc.newInstance(new Object[]{
                    value
            });
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }


    public Object getReference() {
        return caller;
    }

    public String getString(String string)
            throws IOException {
        String STRING_PREFIX = "string:";
        String FILE_PREFIX = "file:";
        if (string == null) {
            return null;
        }
        if (string.toLowerCase().startsWith(FILE_PREFIX)) {
            InputStream ins = null;
            String s;
            try {
                String ress = substring(string, FILE_PREFIX.length(), string.length());
                ins = (caller == null ? getClass() : caller.getClass()).getResourceAsStream(ress);
                if (ins == null) {
                    System.out.println("Not a class ressource....");
                    ins = new FileInputStream(ress);
                }
                long l = ins.available();
                byte[] b = new byte[(int) l];
                int x = ins.read(b);
                s = x < 0 ? "" : new String(b, 0, x);
            } finally {
                if (ins != null) {
                    ins.close();
                }
            }
            return s;
        }
        if (string.toLowerCase().startsWith(STRING_PREFIX)) {
            return substring(string, STRING_PREFIX.length(), string.length());
        }
        else {
            return string;
        }
    }

    public boolean isDeclared(String label) {
        return isDeclared(label, isCaseSensitive());
    }

    public boolean isDeclared(String label, boolean caseSensitive) {
        return getOption(label, false, caseSensitive) != null;
    }

//    public static void main(String args[]) {
//        new JCmdLine(args, false, "file:help.txt");
//    }

    public void setArguments(String[] args) {
        arguments.clear();
        try {
            loadArgumentsFromFile("options.txt");
            addArguments(args);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    public void loadArgumentsFromFile(String fileName) throws IOException {
        BufferedReader r = null;
        try {
            File f = new File(fileName);
            if (!f.exists() && f.isFile()) {
                return;
            }
            System.out.println("Loading properties from " + fileName + " ...");
            r = new BufferedReader(new FileReader(f));
            String l;
            ArrayList<String> lines = new ArrayList<String>();
            while ((l = r.readLine()) != null) {
                lines.add(l);
            }
            addArguments(lines.toArray(new String[lines.size()]));
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

    public void addArguments(String[] args) {
        if (args == null) {
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].length() != 0) {
                if (args[i].equals((optionStart + optionCmdStart) + "OPTION_START")) {
                    optionStart = args[i + 1];
                    i++;
                } else if (args[i].equals((optionStart + optionCmdStart) + "OPTION_LONG_START")) {
                    optionLongStart = args[i + 1];
                    i++;
                } else if (args[i].equals((optionStart + optionCmdStart) + "OPTION_NEGATE")) {
                    optionNegate = args[i + 1];
                    i++;
                } else if (args[i].equals((optionStart + optionCmdStart) + "OPTION_SEPARATOR")) {
                    optionSeparator = args[i + 1];
                    i++;
                } else if (args[i].equals((optionStart + optionCmdStart) + "?")) {
                    System.out.println((optionStart + optionCmdStart) + "OPTION_START followed by start sequence : change the dash \"-\" by another sequence");
                    System.out.println((optionStart + optionCmdStart) + "OPTION_LONG_START followed by start sequence : change the dash \"--\" by another sequence");
                    System.out.println((optionStart + optionCmdStart) + "OPTION_NEGATE followed by start sequence : change the exclamation mark \"!\" by another sequence");
                    System.out.println((optionStart + optionCmdStart) + "OPTION_SEPARATOR followed by start sequence : change the \":=\" by another sequence");
                    System.out.println((optionStart + optionCmdStart) + "? : shows this help");
                    System.out.println((optionStart + optionCmdStart) + "OPTIONS_FROM followed by options file name : loads all options in the specified file");
                    if (exitIfHelp) {
                        System.exit(0);
                    }
                } else if (args[i].equals((optionStart + optionCmdStart) + "OPTIONS_FROM")) {
                    String file = args[i + 1];
                    i++;
                    try {
                        loadArgumentsFromFile(file);
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                } else
                if (args[i].equals(optionStart + HELP1_OPTION) || args[i].equals(optionStart + HELP2_OPTION) || args[i].equals(optionStart + HELP3_OPTION))
                {
                    showHelp();
                } else
                if (args[i].equalsIgnoreCase(optionStart + "w" + HELP1_OPTION) || args[i].equals(optionStart + "w" + HELP2_OPTION) || args[i].equals(optionStart + "w" + HELP3_OPTION))
                {
                    showWinHelp(null, null);
                } else if (args[i].startsWith(optionLongStart)) {
                    arguments.add(new CmdOption(this, args[i].substring(optionLongStart.length()), arguments.size(), false));
                } else if (args[i].startsWith(optionStart)) {
                    arguments.add(new CmdOption(this, args[i].substring(optionStart.length()), arguments.size(), true));
                } else {
                    if (expectedOptions != null) {
                        CmdOption o = null;
                        int oi = -1;
                        for (int j = arguments.size() - 1; j >= 0; j--) {
                            CmdArg a = arguments.get(j);
                            if (a instanceof CmdOption) {
                                o = (CmdOption) a;
                                oi = j;
                                break;
                            }
                        }
                        if (o != null) {
                            int c = i - oi;
                            ExpectOption eo = o.getExpectOption();
                            if (eo.getMaxFollowingParamsCount() >= 0) {
                                if (!o.isBoolean() && eo.isValueIsFirstParam()) {
                                    if ((c + 1) <= eo.getMaxFollowingParamsCount()) {
                                        arguments.add(new OptionParam(args[i], arguments.size(), o, c + 1));
                                        continue;
                                    }
                                } else {
                                    if ((c + 0) <= eo.getMaxFollowingParamsCount()) {
                                        arguments.add(new OptionParam(args[i], arguments.size(), o, c));
                                        continue;
                                    }
                                }
                            } else {
                                arguments.add(new OptionParam(args[i], arguments.size(), o, oi));
                                continue;
                            }
                        }

                    }
                    arguments.add(new CmdParam(args[i], arguments.size()));
                }
            }
        }
    }

    public String readFromConsole(String questionMessage, String endPattern)
            throws IOException {
        System.out.println(questionMessage);
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        StringBuffer sb = new StringBuffer();
        do {
            String srvMsg = stdIn.readLine();
            if (srvMsg == null) {
                srvMsg = "";
            }
            if (!srvMsg.equals(endPattern)) {
                sb.append(srvMsg).append(CRLF);
            } else {
                stdIn.close();
                return sb.toString();
            }
        } while (true);
    }

    public void reflectValues() {
        Class cl = caller.getClass();
        Field[] fields = cl.getFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers())) {
                Class fc = field.getType();
                String fn = field.getName();
//                    Object fv = fields[i].getString(caller);
                String o = getOptionValue(fn, true, null);
                if (o != null) {
                    try {
                        Object x = getObjectFromString(fc, o);
                        if (x == null) {
                            throw new RuntimeException("Illegal class " + fc + "for " + fn + " : no constructor takes as single String as argument;");
                        }
                        field.set(caller, x);
                    } catch (Exception e) {
                        throw new RuntimeException(e + " (" + fn + ")");
                    }
                }
            }
//            } catch (IllegalAccessException ee) {
//                System.out.println(ee);
//            }
        }
//            } catch (IllegalAccessException ee) {
//                System.out.println(ee);
//            }

    }

    public String getOptionValue(String label, boolean caseSensitive, String defaultValue) {
        CmdOption a = getOption(label, false, caseSensitive);
        String val = null;
        if (a != null) {
            val = a.getValue();
        }
        return val != null ? val : defaultValue;
    }

    public void setReference(Object caller) {
        this.caller = caller;
    }

//    public void setStatic() {
//        applicationArguments = this;
//    }

    public void showHelp() {
        try {
            System.out.println(getHelp());
            System.out.println();
            System.out.println("For meta options type option : " + (optionStart + optionCmdStart) + "?");
        } catch (Exception e) {
            System.out.println("Help cannot be displayed....(" + e + ")");
        }
        if (exitIfHelp) {
            System.exit(0);
        }
    }

    public void showWinHelp(Component parent, String title) {
        String help = getHelp();
        JTextComponent textComponent;
        if (help.toLowerCase().startsWith("<html>")) {
            textComponent = new JEditorPane("text/html", help);
        } else {
            textComponent = new JTextArea(getHelp());
        }
        textComponent.setEditable(false);
        JScrollPane pane = new JScrollPane(textComponent);
        pane.setBorder(null);
        pane.setPreferredSize(new Dimension(400, 400));
        if (title == null) {
            title = "Command Syntax";
        }
        JOptionPane.showMessageDialog(parent, pane, title, JOptionPane.PLAIN_MESSAGE);
    }

    private String substring(String string, int start, int end) {
        if (string == null || string.length() == 0) {
            return "";
        }
        if (start < 0)
            start = 0;
        if (end > string.length()) {
            end = string.length();
        }
        if (end <= start) {
            return "";
        }
        else {
            return string.substring(start, end);
        }
    }

    public boolean wantsHelp() {
        return isDeclared(HELP1_OPTION) || isDeclared(HELP2_OPTION) || isDeclared(HELP3_OPTION);
    }

    public boolean isExitIfHelp() {
        return exitIfHelp;
    }

    public void setExitIfHelp(boolean exitIfHelp) {
        this.exitIfHelp = exitIfHelp;
    }

    public String getHelpContent() {
        return helpContent;
    }

    public void setHelpContent(String helpContent) {
        this.helpContent = helpContent;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public CmdParam[] getParameters() {
        ArrayList<CmdParam> a = new ArrayList<CmdParam>();
        for (Object o : arguments) {
            if (o instanceof CmdParam) {
                a.add((CmdParam) o);
            }
        }
        return a.toArray(new CmdParam[a.size()]);
    }

    public CmdOption[] getUnknownOptions() {
        ArrayList<CmdOption> a = new ArrayList<CmdOption>();
        for (Object o : arguments) {
            if (o instanceof CmdOption) {
                CmdOption p = (CmdOption) o;
                if (!p.isUsed()) {
                    a.add(p);
                }
            }
        }
        return a.toArray(new CmdOption[a.size()]);
    }


    public boolean isHelpNeeded() {
        return isSelectedOption(HELP1_OPTION) || isSelectedOption(HELP2_OPTION) || isSelectedOption(HELP3_OPTION);
    }

    public boolean showUnknownOptionsErrors() {
        CmdOption[] o = getUnknownOptions();
        if (o.length > 0) {
            for (CmdOption cmdOption : o) {
                if (exitIfError) {
                    exitWithError(ERR_UNKNOWN_CMD_ARG, "Unknwon cmdOption " + cmdOption.getArgument());
                } else {
                    System.err.println("Unknwon cmdOption " + cmdOption.getArgument());
                }
            }
        }
        return o.length > 0;
    }

    public void dump(PrintStream ps) throws IOException {
        ps.println("ARGS{");
        for (int i = 0; i < size(); i++) {
            ps.println(arguments.get(i));
        }
        ps.println("}");
    }

    public String dump() {
        StringBuffer sb = new StringBuffer();
        sb.append("ARGS{\n");
        for (int i = 0; i < size(); i++) {
            sb.append(arguments.get(i)).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean isExitIfError() {
        return exitIfError;
    }

    public void setExitIfError(boolean exitIfError) {
        this.exitIfError = exitIfError;
    }

    public void exitWithError(int errorLevel, String msg) {
        System.err.println(msg);
        System.exit(errorLevel);
    }

    public boolean isExpectedOptions() {
        return expectedOptions != null;
    }

    public void noExpectedOptions() {
        expectedOptions = null;
    }
}
