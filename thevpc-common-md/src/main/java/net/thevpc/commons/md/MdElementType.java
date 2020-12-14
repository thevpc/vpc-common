/**
 * ====================================================================
 *            thevpc-common-md : Simple Markdown Manipulation Library
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
package net.thevpc.commons.md;

/**
 *
 * @author thevpc
 */
public enum MdElementType {
    TITLE1,
    TITLE2,
    TITLE3,
    TITLE4,
    TITLE5,
    TITLE6,
    TABLE,
    COLUMN,
    ROW,
    XML,
    SEQ,
    TEXT,
    BOLD,
    ITALIC,
    UNNUMBRED_ITEM1,
    UNNUMBRED_ITEM2,
    UNNUMBRED_ITEM3,
    UNNUMBRED_ITEM4,
    UNNUMBRED_ITEM5,
    UNNUMBRED_ITEM6,
    NUMBRED_ITEM1,
    NUMBRED_ITEM2,
    NUMBRED_ITEM3,
    NUMBRED_ITEM4,
    NUMBRED_ITEM5,
    NUMBRED_ITEM6,
    CODE,
    ADMONITION,
    LINE_SEPARATOR,
    LINK,
    CODE_LINK,
    LINE_BREAK,
    HORIZONTAL_RULE,
    IMAGE;

    public static MdElementType title(int depth) {
        return values()[depth - 1 + TITLE1.ordinal()];
    }

    public static MdElementType unnumberedItem(int depth) {
        return values()[depth - 1 + UNNUMBRED_ITEM1.ordinal()];
    }

    public static MdElementType numberedItem(int depth) {
        return values()[depth - 1 + NUMBRED_ITEM1.ordinal()];
    }
}
