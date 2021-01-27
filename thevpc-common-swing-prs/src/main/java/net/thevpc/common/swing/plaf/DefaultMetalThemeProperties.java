/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Pluggable Resources Set is a small library for simplifying
 * plugin based applications
 * 
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/

package net.thevpc.common.swing.plaf;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalIconFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 *
 * @author thevpc
 */
public class DefaultMetalThemeProperties extends DefaultMetalTheme {
    private String name;
    private Map uidefaults;
    private Properties properties;
    private ColorUIResource ui_primary1;
    private ColorUIResource ui_primary2;
    private ColorUIResource ui_primary3;
    private ColorUIResource ui_secondary1;
    private ColorUIResource ui_secondary2;
    private ColorUIResource ui_secondary3;
    private ColorUIResource ui_white;
    private ColorUIResource ui_black;
    private ColorUIResource ui_focusColor;
    private ColorUIResource ui_desktopColor;
    private ColorUIResource ui_control;
    private ColorUIResource ui_controlShadow;
    private ColorUIResource ui_controlDarkShadow;
    private ColorUIResource ui_controlInfo;
    private ColorUIResource ui_controlHighlight;
    private ColorUIResource ui_controlDisabled;
    private ColorUIResource ui_primaryControl;
    private ColorUIResource ui_primaryControlShadow;
    private ColorUIResource ui_primaryControlDarkShadow;
    private ColorUIResource ui_primaryControlInfo;
    private ColorUIResource ui_primaryControlHighlight;
    private ColorUIResource ui_systemTextColor;
    private ColorUIResource ui_controlTextColor;
    private ColorUIResource ui_inactiveControlTextColor;
    private ColorUIResource ui_inactiveSystemTextColor;
    private ColorUIResource ui_userTextColor;
    private ColorUIResource ui_textHighlightColor;
    private ColorUIResource ui_highlightedTextColor;
    private ColorUIResource ui_windowBackground;
    private ColorUIResource ui_windowTitleBackground;
    private ColorUIResource ui_windowTitleForeground;
    private ColorUIResource ui_windowTitleInactiveBackground;
    private ColorUIResource ui_windowTitleInactiveForeground;
    private ColorUIResource ui_menuBackground;
    private ColorUIResource ui_menuForeground;
    private ColorUIResource ui_menuSelectedBackground;
    private ColorUIResource ui_menuSelectedForeground;
    private ColorUIResource ui_menuDisabledForeground;
    private ColorUIResource ui_separatorBackground;
    private ColorUIResource ui_separatorForeground;
    private ColorUIResource ui_acceleratorForeground;
    private ColorUIResource ui_acceleratorSelectedForeground;
    private FontUIResource ui_controlTextFont;
    private FontUIResource ui_systemTextFont;
    private FontUIResource ui_userTextFont;
    private FontUIResource ui_menuTextFont;
    private FontUIResource ui_windowTitleFont;
    private FontUIResource ui_subTextFont;

    public Map getUIDefaults() {
        return uidefaults;
    }

    public DefaultMetalThemeProperties() {
        properties = new Properties();
        uidefaults = new HashMap();
        name = "Custom Theme";
    }

    public DefaultMetalThemeProperties(File file) throws IOException {
        this();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            loadProperties(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    public DefaultMetalThemeProperties(InputStream stream) {
        this();
        loadProperties(stream);
    }

    public String getName() {
        return name;
    }

    private void loadProperties(InputStream stream) {
        try {
            properties.load(stream);
        } catch (IOException e) {
            System.out.println("load theme from stream failed! : " + e);
        }
        for (Enumeration e = properties.propertyNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
            if ("name".equals(key)) {
                name = properties.getProperty(key);
            } else {
                installProperty(key, properties.getProperty(key));
            }
        }

    }

    public void installProperty(String key, String value) {
        if (value == null) {
            return;
        }
        if ("primary1".equals(key)) {
            ui_primary1 = parseColor(value);
            uidefaults.put(key, ui_primary1);
        } else if ("primary2".equals(key)) {
            ui_primary2 = parseColor(value);
            uidefaults.put(key, ui_primary2);
        } else if ("primary3".equals(key)) {
            ui_primary3 = parseColor(value);
            uidefaults.put(key, ui_primary3);
        } else if ("secondary1".equals(key)) {
            ui_secondary1 = parseColor(value);
            uidefaults.put(key, ui_secondary1);
        } else if ("secondary2".equals(key)) {
            ui_secondary2 = parseColor(value);
            uidefaults.put(key, ui_secondary2);
        } else if ("secondary3".equals(key)) {
            ui_secondary3 = parseColor(value);
            uidefaults.put(key, ui_secondary3);
        } else if ("white".equals(key)) {
            ui_white = parseColor(value);
            uidefaults.put(key, ui_white);
        } else if ("black".equals(key)) {
            ui_black = parseColor(value);
            uidefaults.put(key, ui_black);
        } else if ("focusColor".equals(key)) {
            ui_focusColor = parseColor(value);
            uidefaults.put(key, ui_focusColor);
        } else if ("desktopColor".equals(key)) {
            ui_desktopColor = parseColor(value);
            uidefaults.put(key, ui_desktopColor);
        } else if ("control".equals(key)) {
            ui_control = parseColor(value);
            uidefaults.put(key, ui_control);
        } else if ("controlShadow".equals(key)) {
            ui_controlShadow = parseColor(value);
            uidefaults.put(key, ui_controlShadow);
        } else if ("controlDarkShadow".equals(key)) {
            ui_controlDarkShadow = parseColor(value);
            uidefaults.put(key, ui_controlDarkShadow);
        } else if ("controlInfo".equals(key)) {
            ui_controlInfo = parseColor(value);
            uidefaults.put(key, ui_controlInfo);
        } else if ("controlHighlight".equals(key)) {
            ui_controlHighlight = parseColor(value);
            uidefaults.put(key, ui_controlHighlight);
        } else if ("controlDisabled".equals(key)) {
            ui_controlDisabled = parseColor(value);
            uidefaults.put(key, ui_controlDisabled);
        } else if ("primaryControl".equals(key)) {
            ui_primaryControl = parseColor(value);
            uidefaults.put(key, ui_primaryControl);
        } else if ("primaryControlShadow".equals(key)) {
            ui_primaryControlShadow = parseColor(value);
            uidefaults.put(key, ui_primaryControlShadow);
        } else if ("primaryControlDarkShadow".equals(key)) {
            ui_primaryControlDarkShadow = parseColor(value);
            uidefaults.put(key, ui_primaryControlDarkShadow);
        } else if ("primaryControlInfo".equals(key)) {
            ui_primaryControlInfo = parseColor(value);
            uidefaults.put(key, ui_primaryControlInfo);
        } else if ("primaryControlHighlight".equals(key)) {
            ui_primaryControlHighlight = parseColor(value);
            uidefaults.put(key, ui_primaryControlHighlight);
        } else if ("systemTextColor".equals(key)) {
            ui_systemTextColor = parseColor(value);
            uidefaults.put(key, ui_systemTextColor);
        } else if ("controlTextColor".equals(key)) {
            ui_controlTextColor = parseColor(value);
            uidefaults.put(key, ui_controlTextColor);
        } else if ("inactiveControlTextColor".equals(key)) {
            ui_inactiveControlTextColor = parseColor(value);
            uidefaults.put(key, ui_inactiveControlTextColor);
        } else if ("inactiveSystemTextColor".equals(key)) {
            ui_inactiveSystemTextColor = parseColor(value);
            uidefaults.put(key, ui_inactiveSystemTextColor);
        } else if ("userTextColor".equals(key)) {
            ui_userTextColor = parseColor(value);
            uidefaults.put(key, ui_userTextColor);
        } else if ("textHighlightColor".equals(key)) {
            ui_textHighlightColor = parseColor(value);
            uidefaults.put(key, ui_textHighlightColor);
        } else if ("highlightedTextColor".equals(key)) {
            ui_highlightedTextColor = parseColor(value);
            uidefaults.put(key, ui_highlightedTextColor);
        } else if ("windowBackground".equals(key)) {
            ui_windowBackground = parseColor(value);
            uidefaults.put(key, ui_windowBackground);
        } else if ("windowTitleBackground".equals(key)) {
            ui_windowTitleBackground = parseColor(value);
            uidefaults.put(key, ui_windowTitleBackground);
        } else if ("windowTitleForeground".equals(key)) {
            ui_windowTitleForeground = parseColor(value);
            uidefaults.put(key, ui_windowTitleForeground);
        } else if ("windowTitleInactiveBackground".equals(key)) {
            ui_windowTitleInactiveBackground = parseColor(value);
            uidefaults.put(key, ui_windowTitleInactiveBackground);
        } else if ("windowTitleInactiveForeground".equals(key)) {
            ui_windowTitleInactiveForeground = parseColor(value);
            uidefaults.put(key, ui_windowTitleInactiveForeground);
        } else if ("menuBackground".equals(key)) {
            ui_menuBackground = parseColor(value);
            uidefaults.put(key, ui_menuBackground);
        } else if ("menuForeground".equals(key)) {
            ui_menuForeground = parseColor(value);
            uidefaults.put(key, ui_menuForeground);
        } else if ("menuSelectedBackground".equals(key)) {
            ui_menuSelectedBackground = parseColor(value);
            uidefaults.put(key, ui_menuSelectedBackground);
        } else if ("menuSelectedForeground".equals(key)) {
            ui_menuSelectedForeground = parseColor(value);
            uidefaults.put(key, ui_menuSelectedForeground);
        } else if ("menuDisabledForeground".equals(key)) {
            ui_menuDisabledForeground = parseColor(value);
            uidefaults.put(key, ui_menuDisabledForeground);
        } else if ("separatorBackground".equals(key)) {
            ui_separatorBackground = parseColor(value);
            uidefaults.put(key, ui_separatorBackground);
        } else if ("separatorForeground".equals(key)) {
            ui_separatorForeground = parseColor(value);
            uidefaults.put(key, ui_separatorForeground);
        } else if ("acceleratorForeground".equals(key)) {
            ui_acceleratorForeground = parseColor(value);
            uidefaults.put(key, ui_acceleratorForeground);
        } else if ("acceleratorSelectedForeground".equals(key)) {
            ui_acceleratorSelectedForeground = parseColor(value);
            uidefaults.put(key, ui_acceleratorSelectedForeground);
        } else if ("controlTextFont".equals(key)) {
            ui_controlTextFont = parseFont(value);
            uidefaults.put(key, ui_controlTextFont);
        } else if ("systemTextFont".equals(key)) {
            ui_systemTextFont = parseFont(value);
            uidefaults.put(key, ui_systemTextFont);
        } else if ("userTextFont".equals(key)) {
            ui_userTextFont = parseFont(value);
            uidefaults.put(key, ui_userTextFont);
        } else if ("menuTextFont".equals(key)) {
            ui_menuTextFont = parseFont(value);
            uidefaults.put(key, ui_menuTextFont);
        } else if ("windowTitleFont".equals(key)) {
            ui_windowTitleFont = parseFont(value);
            uidefaults.put(key, ui_windowTitleFont);
        } else if ("subTextFont".equals(key)) {
            ui_subTextFont = parseFont(value);
            uidefaults.put(key, ui_subTextFont);
        } else if ("InternalFrame.closeIcon.size".equals(key)) {
            uidefaults.put(key.substring(0, key.length() - 5), MetalIconFactory.getInternalFrameCloseIcon(parseInteger(value).intValue()));
        } else if ("InternalFrame.maximizeIcon.size".equals(key)) {
            uidefaults.put(key.substring(0, key.length() - 5), MetalIconFactory.getInternalFrameCloseIcon(parseInteger(value).intValue()));
        } else if ("InternalFrame.iconifyIcon.size".equals(key)) {
            uidefaults.put(key.substring(0, key.length() - 5), MetalIconFactory.getInternalFrameMaximizeIcon(parseInteger(value).intValue()));
        } else if ("InternalFrame.minimizeIcon.size".equals(key)) {
            uidefaults.put(key.substring(0, key.length() - 5), MetalIconFactory.getInternalFrameMinimizeIcon(parseInteger(value).intValue()));
        } else if ("InternalFrame.closeIcon.size".equals(key)) {
            uidefaults.put(key.substring(0, key.length() - 5), MetalIconFactory.getInternalFrameAltMaximizeIcon(parseInteger(value).intValue()));
        } else {
            uidefaults.put(key, parseObject(value));
        }
    }

    private Object parseObject(String s) {
        if (s != null && s.charAt(0) == '@') {
            return parseObject(properties.getProperty(s.substring(1)));
        }
        if (s.startsWith("color:")) {
            return parseColor(s.substring("color:".length()));
        }
        if (s.startsWith("int:")) {
            return parseInteger(s.substring("int:".length()));
        }
        if (s.startsWith("double:")) {
            return parseDouble(s.substring("double:".length()));
        }
        if (s.startsWith("font:")) {
            return parseFont(s.substring("font:".length()));
        }
        if (s.startsWith("dimension:")) {
            return parseDimension(s.substring("dimension:".length()));
        }
        if (s.startsWith("icon:")) {
            return parseIcon(s.substring("icon:".length()));
        }
        if (s.startsWith("insets:")) {
            return parseInsets(s.substring("insets:".length()));
        } else {
            return parseString(s);
        }
    }

    private ColorUIResource parseColor(String s) {
        if (s == null) {
            return null;
        }
        if (s != null && s.charAt(0) == '@') {
            return parseColor(properties.getProperty(s.substring(1)));
        }
        s = s.trim();
        if (s.startsWith("color:")) {
            return parseColor(s.substring("color:".length()));
        }
        int red = 0;
        int green = 0;
        int blue = 0;
        try {
            StringTokenizer st = new StringTokenizer(s, ", .;/");
            red = Integer.parseInt(st.nextToken().trim());
            green = Integer.parseInt(st.nextToken().trim());
            blue = Integer.parseInt(st.nextToken().trim());
            return new ColorUIResource(red, green, blue);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Couldn't parse color :" + s);
        return null;
    }

    private FontUIResource parseFont(String s) {
        if (s != null && s.charAt(0) == '@') {
            return parseFont(properties.getProperty(s.substring(1)));
        }
        s = s.trim();
        if (s.startsWith("font:")) {
            return parseFont(s.substring("font:".length()));
        }
        String name = "";
        int style = 0;
        int size = 0;
        try {
            StringTokenizer st = new StringTokenizer(s, ",");
            name = st.nextToken();
            String styleString = st.nextToken().trim();
            try {
                style = Integer.parseInt(styleString);
            } catch (Exception e) {
                style = "PLAIN".equalsIgnoreCase(styleString) ? 0 : ((int) ("BOLD".equalsIgnoreCase(styleString) ? 1 : ((int) ("ITALIC".equalsIgnoreCase(styleString) ? 2 : ((int) ("BOLD+ITALIC".equalsIgnoreCase(styleString) ? 3 : ((int) ("ITALIC+BOLD".equalsIgnoreCase(styleString) ? 3 : 0))))))));
            }
            size = Integer.parseInt(st.nextToken().trim());
            return new FontUIResource(name, style, size);
        } catch (Exception e) {
            System.out.println("Couldn't parse Font :" + s);
            System.out.println(e);
            return null;
        }
    }

    private DimensionUIResource parseDimension(String s) {
        if (s != null && s.charAt(0) == '@') {
            return parseDimension(properties.getProperty(s.substring(1)));
        }
        s = s.trim();
        if (s.startsWith("dimension:")) {
            return parseDimension(s.substring("dimension:".length()));
        }
        int width = 0;
        int height = 0;
        try {
            StringTokenizer st = new StringTokenizer(s, ", ");
            width = Integer.parseInt(st.nextToken().trim());
            height = Integer.parseInt(st.nextToken().trim());
            return new DimensionUIResource(width, height);
        } catch (Exception e) {
            System.out.println("Couldn't parse Dimension :" + s);
            System.out.println(e);
            return null;
        }
    }

    private Integer parseInteger(String s) {
        if (s != null && s.charAt(0) == '@') {
            return parseInteger(properties.getProperty(s.substring(1)));
        }
        s = s.trim();
        if (s.startsWith("int:")) {
            return parseInteger(s.substring("int:".length()));
        }
        try {
            return new Integer(Integer.parseInt(s.trim()));
        } catch (Exception e) {
            System.out.println("Couldn't parse Integer :" + s);
            System.out.println(e);
            return null;
        }
    }

    private Double parseDouble(String s) {
        if (s != null && s.charAt(0) == '@') {
            return parseDouble(properties.getProperty(s.substring(1)));
        }
        s = s.trim();
        if (s.startsWith("double:")) {
            return parseDouble(s.substring("double:".length()));
        }
        try {
            return new Double(Double.parseDouble(s.trim()));
        } catch (Exception e) {
            System.out.println("Couldn't parse Double :" + s);
            System.out.println(e);
            return null;
        }
    }

    private IconUIResource parseIcon(String s) {
        if (s != null && s.charAt(0) == '@') {
            return parseIcon(properties.getProperty(s.substring(1)));
        }
        s = s.trim();
        if (s.startsWith("icon:")) {
            return parseIcon(s.substring("icon:".length()));
        }
        try {
            return new IconUIResource(new ImageIcon(getClass().getResource(s.trim())));
        } catch (Exception e) {
            System.out.println("Couldn't parse Icon :" + s);
            System.out.println(e);
            return null;
        }
    }

    private InsetsUIResource parseInsets(String s) {
        if (s != null && s.charAt(0) == '@') {
            return parseInsets(properties.getProperty(s.substring(1)));
        }
        s = s.trim();
        if (s.startsWith("insets:")) {
            return parseInsets(s.substring("insets:".length()));
        }
        try {
            StringTokenizer st = new StringTokenizer(s, ",");
            int a = Integer.parseInt(st.nextToken().trim());
            int b = Integer.parseInt(st.nextToken().trim());
            int c = Integer.parseInt(st.nextToken().trim());
            int d = Integer.parseInt(st.nextToken().trim());
            return new InsetsUIResource(a, b, c, d);
        } catch (Exception e) {
            System.out.println("Couldn't parse Insets :" + s);
            System.out.println(e);
            return null;
        }
    }

    private String parseString(String s) {
        if (s != null && s.charAt(0) == '@') {
            return parseString(properties.getProperty(s.substring(1)));
        } else {
            return s;
        }
    }

    protected ColorUIResource getPrimary1() {
        return ui_primary1 != null ? ui_primary1 : super.getPrimary1();
    }

    protected ColorUIResource getPrimary2() {
        return ui_primary2 != null ? ui_primary2 : super.getPrimary2();
    }

    protected ColorUIResource getPrimary3() {
        return ui_primary3 != null ? ui_primary3 : super.getPrimary3();
    }

    protected ColorUIResource getSecondary1() {
        return ui_secondary1 != null ? ui_secondary1 : super.getSecondary1();
    }

    protected ColorUIResource getSecondary2() {
        return ui_secondary2 != null ? ui_secondary2 : super.getSecondary2();
    }

    protected ColorUIResource getSecondary3() {
        return ui_secondary3 != null ? ui_secondary3 : super.getSecondary3();
    }

    public ColorUIResource getWhite() {
        return ui_white != null ? ui_white : super.getWhite();
    }

    public ColorUIResource getBlack() {
        return ui_black != null ? ui_black : super.getBlack();
    }

    public ColorUIResource getFocusColor() {
        return ui_focusColor != null ? ui_focusColor : super.getFocusColor();
    }

    public ColorUIResource getDesktopColor() {
        return ui_desktopColor != null ? ui_desktopColor : super.getDesktopColor();
    }

    public ColorUIResource getControl() {
        return ui_control != null ? ui_control : super.getControl();
    }

    public ColorUIResource getControlShadow() {
        return ui_controlShadow != null ? ui_controlShadow : super.getControlShadow();
    }

    public ColorUIResource getControlDarkShadow() {
        return ui_controlDarkShadow != null ? ui_controlDarkShadow : super.getControlDarkShadow();
    }

    public ColorUIResource getControlInfo() {
        return ui_controlInfo != null ? ui_controlInfo : super.getControlInfo();
    }

    public ColorUIResource getControlHighlight() {
        return ui_controlHighlight != null ? ui_controlHighlight : super.getControlHighlight();
    }

    public ColorUIResource getControlDisabled() {
        return ui_controlDisabled != null ? ui_controlDisabled : super.getControlDisabled();
    }

    public ColorUIResource getPrimaryControl() {
        return ui_primaryControl != null ? ui_primaryControl : super.getPrimaryControl();
    }

    public ColorUIResource getPrimaryControlShadow() {
        return ui_primaryControlShadow != null ? ui_primaryControlShadow : super.getPrimaryControlShadow();
    }

    public ColorUIResource getPrimaryControlDarkShadow() {
        return ui_primaryControlDarkShadow != null ? ui_primaryControlDarkShadow : super.getPrimaryControlDarkShadow();
    }

    public ColorUIResource getPrimaryControlInfo() {
        return ui_primaryControlInfo != null ? ui_primaryControlInfo : super.getPrimaryControlInfo();
    }

    public ColorUIResource getPrimaryControlHighlight() {
        return ui_primaryControlHighlight != null ? ui_primaryControlHighlight : super.getPrimaryControlHighlight();
    }

    public ColorUIResource getSystemTextColor() {
        return ui_systemTextColor != null ? ui_systemTextColor : super.getSystemTextColor();
    }

    public ColorUIResource getControlTextColor() {
        return ui_controlTextColor != null ? ui_controlTextColor : super.getControlTextColor();
    }

    public ColorUIResource getInactiveControlTextColor() {
        return ui_inactiveControlTextColor != null ? ui_inactiveControlTextColor : super.getInactiveControlTextColor();
    }

    public ColorUIResource getInactiveSystemTextColor() {
        return ui_inactiveSystemTextColor != null ? ui_inactiveSystemTextColor : super.getInactiveSystemTextColor();
    }

    public ColorUIResource getUserTextColor() {
        return ui_userTextColor != null ? ui_userTextColor : super.getUserTextColor();
    }

    public ColorUIResource getTextHighlightColor() {
        return ui_textHighlightColor != null ? ui_textHighlightColor : super.getTextHighlightColor();
    }

    public ColorUIResource getHighlightedTextColor() {
        return ui_highlightedTextColor != null ? ui_highlightedTextColor : super.getHighlightedTextColor();
    }

    public ColorUIResource getWindowBackground() {
        return ui_windowBackground != null ? ui_windowBackground : super.getWindowBackground();
    }

    public ColorUIResource getWindowTitleBackground() {
        return ui_windowTitleBackground != null ? ui_windowTitleBackground : super.getWindowTitleBackground();
    }

    public ColorUIResource getWindowTitleForeground() {
        return ui_windowTitleForeground != null ? ui_windowTitleForeground : super.getWindowTitleForeground();
    }

    public ColorUIResource getWindowTitleInactiveBackground() {
        return ui_windowTitleInactiveBackground != null ? ui_windowTitleInactiveBackground : super.getWindowTitleInactiveBackground();
    }

    public ColorUIResource getWindowTitleInactiveForeground() {
        return ui_windowTitleInactiveForeground != null ? ui_windowTitleInactiveForeground : super.getWindowTitleInactiveForeground();
    }

    public ColorUIResource getMenuBackground() {
        return ui_menuBackground != null ? ui_menuBackground : super.getMenuBackground();
    }

    public ColorUIResource getMenuForeground() {
        return ui_menuForeground != null ? ui_menuForeground : super.getMenuForeground();
    }

    public ColorUIResource getMenuSelectedBackground() {
        return ui_menuSelectedBackground != null ? ui_menuSelectedBackground : super.getMenuSelectedBackground();
    }

    public ColorUIResource getMenuSelectedForeground() {
        return ui_menuSelectedForeground != null ? ui_menuSelectedForeground : super.getMenuSelectedForeground();
    }

    public ColorUIResource getMenuDisabledForeground() {
        return ui_menuDisabledForeground != null ? ui_menuDisabledForeground : super.getMenuDisabledForeground();
    }

    public ColorUIResource getSeparatorBackground() {
        return ui_separatorBackground != null ? ui_separatorBackground : super.getSeparatorBackground();
    }

    public ColorUIResource getSeparatorForeground() {
        return ui_separatorForeground != null ? ui_separatorForeground : super.getSeparatorForeground();
    }

    public ColorUIResource getAcceleratorForeground() {
        return ui_acceleratorForeground != null ? ui_acceleratorForeground : super.getAcceleratorForeground();
    }

    public ColorUIResource getAcceleratorSelectedForeground() {
        return ui_acceleratorSelectedForeground != null ? ui_acceleratorSelectedForeground : super.getAcceleratorSelectedForeground();
    }

    public FontUIResource getControlTextFont() {
        return ui_controlTextFont != null ? ui_controlTextFont : super.getControlTextFont();
    }

    public FontUIResource getSystemTextFont() {
        return ui_systemTextFont != null ? ui_systemTextFont : super.getSystemTextFont();
    }

    public FontUIResource getUserTextFont() {
        return ui_userTextFont != null ? ui_userTextFont : super.getUserTextFont();
    }

    public FontUIResource getMenuTextFont() {
        return ui_menuTextFont != null ? ui_menuTextFont : super.getMenuTextFont();
    }

    @Override
    public FontUIResource getWindowTitleFont() {
        return ui_windowTitleFont != null ? ui_windowTitleFont : super.getWindowTitleFont();
    }

    public FontUIResource getSubTextFont() {
        return ui_subTextFont != null ? ui_subTextFont : super.getSubTextFont();
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        table.putAll(uidefaults);
    }

    public void describe() {
        System.out.println("primary1 = " + ui_primary1);
        System.out.println("primary2 = " + ui_primary2);
        System.out.println("primary3 = " + ui_primary3);
        System.out.println("secondary1 = " + ui_secondary1);
        System.out.println("secondary2 = " + ui_secondary2);
        System.out.println("secondary3 = " + ui_secondary3);
        System.out.println("white = " + ui_white);
        System.out.println("black = " + ui_black);
        System.out.println("focusColor = " + ui_focusColor);
        System.out.println("desktopColor = " + ui_desktopColor);
        System.out.println("control = " + ui_control);
        System.out.println("controlShadow = " + ui_controlShadow);
        System.out.println("controlDarkShadow = " + ui_controlDarkShadow);
        System.out.println("controlInfo = " + ui_controlInfo);
        System.out.println("controlHighlight = " + ui_controlHighlight);
        System.out.println("controlDisabled = " + ui_controlDisabled);
        System.out.println("primaryControl = " + ui_primaryControl);
        System.out.println("primaryControlShadow = " + ui_primaryControlShadow);
        System.out.println("primaryControlDarkShadow = " + ui_primaryControlDarkShadow);
        System.out.println("primaryControlInfo = " + ui_primaryControlInfo);
        System.out.println("primaryControlHighlight = " + ui_primaryControlHighlight);
        System.out.println("systemTextColor = " + ui_systemTextColor);
        System.out.println("controlTextColor = " + ui_controlTextColor);
        System.out.println("inactiveControlTextColor = " + ui_inactiveControlTextColor);
        System.out.println("inactiveSystemTextColor = " + ui_inactiveSystemTextColor);
        System.out.println("userTextColor = " + ui_userTextColor);
        System.out.println("textHighlightColor = " + ui_textHighlightColor);
        System.out.println("highlightedTextColor = " + ui_highlightedTextColor);
        System.out.println("windowBackground = " + ui_windowBackground);
        System.out.println("windowTitleBackground = " + ui_windowTitleBackground);
        System.out.println("windowTitleForeground = " + ui_windowTitleForeground);
        System.out.println("windowTitleInactiveBackground = " + ui_windowTitleInactiveBackground);
        System.out.println("windowTitleInactiveForeground = " + ui_windowTitleInactiveForeground);
        System.out.println("menuBackground = " + ui_menuBackground);
        System.out.println("menuForeground = " + ui_menuForeground);
        System.out.println("menuSelectedBackground = " + ui_menuSelectedBackground);
        System.out.println("menuSelectedForeground = " + ui_menuSelectedForeground);
        System.out.println("menuDisabledForeground = " + ui_menuDisabledForeground);
        System.out.println("separatorBackground = " + ui_separatorBackground);
        System.out.println("separatorForeground = " + ui_separatorForeground);
        System.out.println("acceleratorForeground = " + ui_acceleratorForeground);
        System.out.println("acceleratorSelectedForeground = " + ui_acceleratorSelectedForeground);
        System.out.println("controlTextFont = " + ui_controlTextFont);
        System.out.println("systemTextFont = " + ui_systemTextFont);
        System.out.println("userTextFont = " + ui_userTextFont);
        System.out.println("menuTextFont = " + ui_menuTextFont);
        System.out.println("windowTitleFont = " + ui_windowTitleFont);
        System.out.println("subTextFont = " + ui_subTextFont);
    }
}
