/**
 * ====================================================================
 *            Nuts : Network Updatable Things Service
 *                  (universal package manager)
 *
 * is a new Open Source Package Manager to help install packages
 * and libraries for runtime execution. Nuts is the ultimate companion for
 * maven (and other build managers) as it helps installing all package
 * dependencies at runtime. Nuts is not tied to java and is a good choice
 * to share shell scripts and other 'things' . Its based on an extensible
 * architecture to help supporting a large range of sub managers / repositories.
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
package net.thevpc.common.fprint;

import net.thevpc.common.fprint.*;

import java.awt.*;

/**
 *
 * @author thevpc
 */
public class TextFormats {

    public static final TextFormat MOVE_UP = new TextCursor("MOVE_UP");
    public static final TextFormat MOVE_LINE_START = new TextCursor("MOVE_LINE_START");
    public static final TextFormat UNDERLINED = new TextCursor("UNDERLINED");
    public static final TextFormat ITALIC = new TextCursor("ITALIC");
    public static final TextFormat STRIKED = new TextCursor("STRIKED");
    public static final TextFormat REVERSED = new TextCursor("REVERSED");

    public static final TextFormat FG_BLUE = new TextForeground("FG_BLUE", Color.BLUE);
    public static final TextFormat FG_GREEN = new TextForeground("FG_GREEN", Color.GREEN);
    public static final TextFormat FG_CYAN = new TextForeground("FG_CYAN", Color.CYAN);
    public static final TextFormat FG_MAGENTA = new TextForeground("FG_PURPLE", new Color(128, 0, 128));
    public static final TextFormat FG_YELLOW = new TextForeground("FG_YELLOW", Color.YELLOW);
    public static final TextFormat FG_RED = new TextForeground("FG_RED", Color.RED);
    public static final TextFormat FG_WHITE = new TextForeground("FG_WHITE", Color.WHITE);
    public static final TextFormat FG_BLACK = new TextForeground("FG_BLACK", Color.BLACK);
    public static final TextFormat FG_GREY = new TextForeground("FG_GREY", Color.GRAY);
    public static final TextFormat BG_GREY = new TextBackground("BG_GREY", Color.GRAY);

//    public static final TextFormat FG_LIGHT_RED = new TextForeground("FG_LIGHT_RED", new Color(250, 128, 114));
//    public static final TextFormat FG_BROWN = new TextForeground("FG_BROWN", new Color(165, 42, 42));
//    public static final TextFormat FG_DARK_GRAY = new TextForeground("FG_DARK_GRAY", Color.DARK_GRAY);
//    public static final TextFormat FG_LIGHT_BLUE = new TextForeground("FG_LIGHT_BLUE", new Color(173, 216, 230));
//    public static final TextFormat FG_LIGHT_GRAY = new TextForeground("FG_LIGHT_GRAY", Color.LIGHT_GRAY);
//    public static final TextFormat FG_LIGHT_PURPLE = new TextForeground("FG_LIGHT_PURPLE", new Color(216, 191, 216));
//    public static final TextFormat FG_LIGHT_CYAN = new TextForeground("FG_LIGHT_CYAN", new Color(224, 255, 255));
//    public static final TextFormat FG_LIGHT_GREEN = new TextForeground("FG_LIGHT_GREEN", new Color(144, 238, 144));

    public static final TextFormat BG_CYAN = new TextBackground("BG_CYAN", Color.CYAN);
    public static final TextFormat BG_MAGENTA = new TextBackground("BG_PURPLE", new Color(128, 0, 128));
    public static final TextFormat BG_BLACK = new TextBackground("BG_BLACK", Color.BLACK);
    public static final TextFormat BG_YELLOW = new TextBackground("BG_YELLOW", Color.YELLOW);
    public static final TextFormat BG_GREEN = new TextBackground("BG_GREEN", Color.GREEN);
    public static final TextFormat BG_RED = new TextBackground("BG_RED", Color.RED);
    public static final TextFormat BG_BLUE = new TextBackground("BG_BLUE", Color.BLUE);
    public static final TextFormat BG_WHITE = new TextBackground("BG_WHITE", Color.WHITE);

//    public static final TextFormat BG_BROWN = new TextBackground("BG_BROWN", new Color(165, 42, 42));
//    public static final TextFormat BG_DARK_GRAY = new TextBackground("BG_DARK_GRAY", Color.DARK_GRAY);
//    public static final TextFormat BG_LIGHT_RED = new TextBackground("BG_LIGHT_RED", new Color(250, 128, 114));
//    public static final TextFormat BG_LIGHT_CYAN = new TextBackground("BG_LIGHT_CYAN", new Color(224, 255, 255));
//    public static final TextFormat BG_LIGHT_GREEN = new TextBackground("BG_LIGHT_GREEN", new Color(144, 238, 144));
//    public static final TextFormat BG_LIGHT_BLUE = new TextBackground("BG_LIGHT_BLUE", new Color(173, 216, 230));
//    public static final TextFormat BG_LIGHT_GRAY = new TextBackground("BG_LIGHT_GRAY", Color.LIGHT_GRAY);
//    public static final TextFormat BG_LIGHT_PURPLE = new TextBackground("BG_LIGHT_PURPLE", new Color(216, 191, 216));

    public static final TextFormat list(TextFormat... all) {
        if(all==null){
            return null;
        }
        if(all.length==1){
            return all[0];
        }
        return new TextFormatList(all);
    }
}
