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

import java.util.ArrayList;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime  13 juil. 2006 22:14:21
 */
public class CmdOption extends CmdArg {
    private boolean shortOption;
    private String name;
    private String val;
    private boolean selected;
    private ExpectOption expectOption;
    private JCmdLine jCmdLine;
    private boolean used;

    CmdOption(JCmdLine jCmdLine, String arg, int index, boolean shortOption) {
        super(arg, index);
        this.jCmdLine = jCmdLine;
        this.shortOption = shortOption;
        boolean isFullSeparator = false;
        int x = arg.indexOf(jCmdLine.optionSeparator);
        if (x < 0) {
            for (int i = 0; i < jCmdLine.optionSeparator.length(); i++) {
                if (arg.indexOf(jCmdLine.optionSeparator.charAt(i)) >= 0) {
                    x = arg.indexOf(jCmdLine.optionSeparator.charAt(i));
                    break;
                }
            }
        } else {
            isFullSeparator = true;
        }

        if (x == 0 || x == arg.length() - 1) {
            throw new RuntimeException("Syntax error near '" + arg + "'");
        }
        if (x < 0) {
            if (arg.startsWith(jCmdLine.optionNegate)) {
                name = arg.substring(1);
                val = null;
                selected = false;
            } else {
                name = arg;
                val = null;
                selected = true;
            }
        } else {
            if (arg.startsWith(jCmdLine.optionNegate)) {
                name = arg.substring((isFullSeparator ? jCmdLine.optionSeparator.length() : 1), x);
                val = arg.substring(x + (isFullSeparator ? jCmdLine.optionSeparator.length() : 1), arg.length());
                selected = false;
            } else {
                name = arg.substring(0, x);
                val = arg.substring(x + (isFullSeparator ? jCmdLine.optionSeparator.length() : 1), arg.length());
                selected = true;
            }
        }
    }

    public void validate() {
        if (jCmdLine.isExpectedOptions()) {
            ExpectOption _expectOption = jCmdLine.getExpectedArgument(name);
            if (_expectOption == null) {
                throw new IllegalArgumentException("Unknown CmdOption " + name);
            } else {

                if (shortOption) {
                    if (_expectOption.getShortName() == null || (jCmdLine.isCaseSensitive() ? _expectOption.getShortName().equals(name) : _expectOption.getShortName().equalsIgnoreCase(name)))
                    {
                        throw new IllegalArgumentException("expected " + jCmdLine.optionStart + name + " and not " + jCmdLine.optionLongStart + name);
                    }
                } else {
                    if (_expectOption.getLongName() == null || (jCmdLine.isCaseSensitive() ? _expectOption.getLongName().equals(name) : _expectOption.getLongName().equalsIgnoreCase(name)))
                    {
                        throw new IllegalArgumentException("expected " + jCmdLine.optionLongStart + name + " and not " + jCmdLine.optionStart + name);
                    }
                }
                String fullName = (isShortOption() ? jCmdLine.optionStart : jCmdLine.optionLongStart) + name;
                if (!_expectOption.isBooleanValue() && this.isBoolean()) {
                    throw new IllegalArgumentException("expected " + fullName + "=???");
                }
//                if(expectOption.validValues !=null){
//                    throw new IllegalArgumentException("expected "+fullName+"=???");
//                }
                int c0 = countFollowingParams();
                int c = c0;
                if (!isBoolean() && _expectOption.isValueIsFirstParam()) {
                    c = c + 1;
                }
                if (_expectOption.getMaxFollowingParamsCount() >= 0) {
                    if (c > _expectOption.getMinFollowingParamsCount()) {
                        if (!isLastOption()) {
                            throw new IllegalArgumentException("Too many params for option " + name);
                        }
                    }
                }
                if (_expectOption.getMinFollowingParamsCount() >= 0) {
                    if (c < _expectOption.getMinFollowingParamsCount()) {
                        throw new IllegalArgumentException("Too few params for option " + name);
                    }
                }
                if (this.getIndex() > 0 && jCmdLine.getArgument(this.getIndex() - 1) instanceof CmdParam) {
                    throw new IllegalArgumentException("Unexpected CmdOption " + name + " after param " + jCmdLine.getArgument(this.getIndex() - 1).getArgument());
                }

            }
            this.expectOption = _expectOption;
        }
    }

    public ExpectOption getExpArg() {
        return expectOption;
    }

    public boolean isShortOption() {
        return shortOption;
    }


    public String getName() {
        return name;
    }

    public String getValue() {
        return val;
    }

    public boolean isSelected() {
        return selected;
    }

    public int countFollowingParams() {
        int i = getIndex();
        while (true) {
            if (i > jCmdLine.size()) {
                return i - getIndex();
            }
            CmdArg a = jCmdLine.getArgument(i);
            if (!(a instanceof OptionParam)) {
                return i - getIndex();
            }
        }
    }

    public boolean isLastOption() {
        int i = getIndex();
        for (int j = i + 1; j < jCmdLine.size(); j++) {
            CmdArg a = jCmdLine.getArgument(i);
            if (a instanceof CmdOption) {
                return false;
            }
        }
        return true;
    }

    public CmdParam getFollowingParam(int i) {
        return (CmdParam) jCmdLine.getArgument(i + getIndex());
    }

    public boolean isBoolean() {
        return (getValue().length() == 0);
    }

    public String toString() {
        return val == null ? (getIndex() + "[CmdOption]:" + getName() + ((isSelected()) ? "" : ":deactivated")) : (getIndex() + "[CmdOption]:" + getName() + "=" + getValue() + (isSelected() ? "" : ":deactivated"));
    }

    public String[] getParamValues() {
        ArrayList<String> p = new ArrayList<String>();
        int i = getIndex();
        while (true) {
            if (i > jCmdLine.size()) {
                break;
            }
            CmdArg a = jCmdLine.getArgument(i++);
            if (!(a instanceof OptionParam)) {
                break;
            }
            p.add(((OptionParam) a).getValue());
        }
        if (!isBoolean() && expectOption != null && expectOption.isValueIsFirstParam()) {
            p.add(0, val);
        }
        return p.toArray(new String[p.size()]);
    }

    public ExpectOption getExpectOption() {
        return expectOption;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isAnyOption(String ... names){
        for (String n : names) {
            if(getName().equals(n)){
                return true;
            }
        }
        return false;
    }

    public boolean isOption(String shortName,String longName){
        return isOption(shortName,longName,true);
    }

    public boolean isOption(String shortName,String longName,boolean caseSensitive){
        CmdOption cmdOption;
        if (shortName != null) {
            if (isShortOption() && (caseSensitive? getName().equals(shortName):getName().equalsIgnoreCase(shortName))) {
                return true;
            }
        }
        if (longName != null) {
            if (isLastOption() && (caseSensitive? getName().equals(longName):getName().equalsIgnoreCase(longName))) {
                return true;
            }
        }
        return false;
    }

//        public boolean isEnabled(boolean defaultValue){
//            if(getValue()!=null){
//                return defaultValue;
//            }
//            return isArmed();
//        }
}
