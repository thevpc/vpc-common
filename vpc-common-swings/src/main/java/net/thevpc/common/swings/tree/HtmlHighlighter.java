/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.tree;

import java.util.regex.Pattern;

/**
 *
 * @author vpc
 */
public class HtmlHighlighter {
    private static final String HighLightTemplate = "<span style='background:rgb(230,230,0);'>$1</span>";

    public static String highlightText(String text, String textToHighlight) {
        if (textToHighlight.length() == 0) {
            return text;
        }

        try {
            text = text.replaceAll("(?i)(" + Pattern.quote(textToHighlight) + ")", HighLightTemplate);
        } catch (Exception e) {
            return text;
        }
        return text;
    }
}
