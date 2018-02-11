/**
 * ====================================================================
 *                        vpc-swingext library
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
package net.vpc.common.swings;

import java.awt.*;
import java.util.Hashtable;
import java.util.NoSuchElementException;

/**
 * @author vpc
 *         Date: 12 janv. 2005
 *         Time: 19:41:17
 */
public class GridBagLayoutSupport {
    private String pattern;
    private Hashtable<String, GridBagConstraints> constraintsMap = new Hashtable<String, GridBagConstraints>();

    public GridBagLayoutSupport(String pattern) {
        setPattern(pattern);
    }

    public GridBagConstraints getConstraints(String name) {
        GridBagConstraints constraints = constraintsMap.get(name);
        if (constraints == null) {
            throw new NoSuchElementException(name);
        }
        return constraints;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        constraintsMap.clear();
        int x = 0;
        int y = 0;
        int i = 0;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == '[') {
                int j = pattern.indexOf(']', i + 1);
                if (j < 0) {
                    throw new IllegalArgumentException("Expected [");
                }
                String tagAndName = pattern.substring(i + 1, j);
                StringBuilder name = new StringBuilder();
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = x;
                constraints.gridy = y;
                x++;
                i = j + 1;
                boolean lastConfig = true;
                boolean config = false;
                for (int k = 0; k < tagAndName.length(); k++) {
                    char c = tagAndName.charAt(k);
                    switch (c) {
                        case '-': {
                            config = true;
                            lastConfig = true;
                            constraints.fill = GridBagConstraints.HORIZONTAL;
                            break;
                        }
                        case '|': {
                            config = true;
                            lastConfig = true;
                            constraints.fill = GridBagConstraints.VERTICAL;
                            break;
                        }
                        case '+': {
                            config = true;
                            lastConfig = true;
                            constraints.fill = GridBagConstraints.BOTH;
                            break;
                        }
                        case ':': {
                            config = true;
                            lastConfig = true;
                            constraints.gridwidth ++;
                            x++;
                            break;
                        }
                        case '.': {
                            config = true;
                            lastConfig = true;
                            constraints.gridwidth = GridBagConstraints.REMAINDER;
                            break;
                        }
                        case '<': {
                            config = true;
                            lastConfig = true;
                            if (constraints.anchor == GridBagConstraints.PAGE_START) {
                                constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                            } else if (constraints.anchor == GridBagConstraints.PAGE_END) {
                                constraints.anchor = GridBagConstraints.LAST_LINE_START;
                            } else {
                                constraints.anchor = GridBagConstraints.LINE_START;
                            }
                            break;
                        }
                        case '>': {
                            config = true;
                            lastConfig = true;
                            if (constraints.anchor == GridBagConstraints.PAGE_START) {
                                constraints.anchor = GridBagConstraints.FIRST_LINE_END;
                            } else if (constraints.anchor == GridBagConstraints.PAGE_END) {
                                constraints.anchor = GridBagConstraints.LAST_LINE_END;
                            } else {
                                constraints.anchor = GridBagConstraints.LINE_END;
                            }
                            break;
                        }
                        case '_': {
                            config = true;
                            lastConfig = true;
                            if (constraints.anchor == GridBagConstraints.LINE_START) {
                                constraints.anchor = GridBagConstraints.LAST_LINE_START;
                            } else if (constraints.anchor == GridBagConstraints.LINE_END) {
                                constraints.anchor = GridBagConstraints.LAST_LINE_END;
                            } else {
                                constraints.anchor = GridBagConstraints.PAGE_END;
                            }
                            break;
                        }
                        case '^': {
                            config = true;
                            lastConfig = true;
                            if (constraints.anchor == GridBagConstraints.LINE_START) {
                                constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                            } else if (constraints.anchor == GridBagConstraints.LINE_END) {
                                constraints.anchor = GridBagConstraints.FIRST_LINE_END;
                            } else {
                                constraints.anchor = GridBagConstraints.PAGE_START;
                            }
                            break;
                        }
                        case '=': {
                            config = true;
                            lastConfig = true;
                            constraints.weightx++;
                            break;
                        }
                        case '$': {
                            config = true;
                            lastConfig = true;
                            constraints.weighty++;
                            break;
                        }
                        case ' ': {
                            //do nothing just allowed for formatting
                            lastConfig = true;
                            break;
                        }
                        default : {
                            if (Character.isLetter(c) || Character.isDigit(c)) {
                                if (name.length() > 0 && config && lastConfig) {
                                    throw new IllegalArgumentException("name is already specified : got '" + c + "' , name is '" + name.toString() + "'");
                                }
                                name.append(c);
                            } else {
                                throw new IllegalArgumentException("Unexpected token " + c);
                            }
                            lastConfig = false;
                            break;
                        }
                    }
                }
                String key = name.toString();
                GridBagConstraints oldConstraints = constraintsMap.get(key);
                if (oldConstraints != null) {
                    if (config) {
                        throw new IllegalArgumentException("you cannot override constraints for " + name);
                    } else {
                        oldConstraints.gridwidth = constraints.gridx - oldConstraints.gridx + 1;
                        oldConstraints.gridheight = constraints.gridy - oldConstraints.gridy + 1;
                    }
                } else {
                    constraintsMap.put(key, constraints);
                }
            } else if (pattern.charAt(i) == '\n') {
                y++;
                x = 0;
                i++;
            } else if (pattern.charAt(i) == ':' || pattern.charAt(i) == ' ') {
                i++;
            } else {
                throw new IllegalArgumentException("Expected [");
            }
        }
    }

    public void setInsets(String commaSeparatedNames, Insets insets) {
        String[] names = commaSeparatedNames.split(";");
        for (String s : names) {
            getConstraints(s).insets = insets;
        }
    }

    public void setIpadx(String commaSeparatedNames, int ipadx) {
        String[] names = commaSeparatedNames.split(";");
        for (String s : names) {
            getConstraints(s).ipadx = ipadx;
        }
    }

    public void setIpady(String commaSeparatedNames, int ipady) {
        String[] names = commaSeparatedNames.split(";");
        for (String s : names) {
            getConstraints(s).ipady = ipady;
        }
    }
}
