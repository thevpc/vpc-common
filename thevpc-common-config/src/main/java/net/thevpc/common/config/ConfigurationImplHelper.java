/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.config;

import java.awt.Color;
import java.awt.Font;
import java.util.StringTokenizer;

/**
 *
 * @author thevpc
 */
public class ConfigurationImplHelper {
    public static String getStringFromFont(Font font) {
        return font.getName() + "," + font.getStyle() + "," + font.getSize();
    }

    public static Font getFontFromString(String font) {
        StringTokenizer st = new StringTokenizer(font, ",");
        st.hasMoreTokens();
        String sn = st.nextToken();
        st.hasMoreTokens();
        String sst = st.nextToken();
        st.hasMoreTokens();
        String ssz = st.nextToken();
        return new Font(sn, Integer.parseInt(sst), Integer.parseInt(ssz));
    }

    public static String getStringFromColor(Color color) {
        return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
    }

    public static Color getColorFromString(String color) {
        StringTokenizer st = new StringTokenizer(color, ", ");
        st.hasMoreTokens();
        String r = st.nextToken();
        st.hasMoreTokens();
        String g = st.nextToken();
        st.hasMoreTokens();
        String b = st.nextToken();
        return new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
    }

}
