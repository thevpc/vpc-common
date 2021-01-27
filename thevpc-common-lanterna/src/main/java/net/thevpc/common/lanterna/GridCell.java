/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package net.thevpc.common.lanterna;

import com.googlecode.lanterna.gui2.LayoutData;

import java.awt.*;

/**
 * The <code>GridBagConstraints</code> class specifies constraints
 * for components that are laid out using the
 * <code>GridBagLayout</code> class.
 *
 * @author Doug Stein
 * @author Bill Spitzak (orignial NeWS &amp; OLIT implementation)
 * @see GridBagLayout
 * @since JDK1.0
 */
public class GridCell implements Cloneable, java.io.Serializable,LayoutData {

    /**
     * Specifies that this component is the next-to-last component in its
     * column or row (<code>gridwidth</code>, <code>gridheight</code>),
     * or that this component be placed next to the previously added
     * component (<code>gridx</code>, <code>gridy</code>).
     * @see      GridCell#gridwidth
     * @see      GridCell#gridheight
     * @see      GridCell#gridx
     * @see      GridCell#gridy
     */
    public static final int RELATIVE = -1;

    /**
     * Specifies that this component is the
     * last component in its column or row.
     */
    public static final int REMAINDER = 0;

    /**
     * Do not resize the component.
     */
    public static final int NONE = 0;

    /**
     * Resize the component both horizontally and vertically.
     */
    public static final int BOTH = 1;

    /**
     * Resize the component horizontally but not vertically.
     */
    public static final int HORIZONTAL = 2;

    /**
     * Resize the component vertically but not horizontally.
     */
    public static final int VERTICAL = 3;

    /**
     * Put the component in the center of its display area.
     */
    public static final int CENTER = 10;

    /**
     * Put the component at the top of its display area,
     * centered horizontally.
     */
    public static final int NORTH = 11;

    /**
     * Put the component at the top-right corner of its display area.
     */
    public static final int NORTHEAST = 12;

    /**
     * Put the component on the right side of its display area,
     * centered vertically.
     */
    public static final int EAST = 13;

    /**
     * Put the component at the bottom-right corner of its display area.
     */
    public static final int SOUTHEAST = 14;

    /**
     * Put the component at the bottom of its display area, centered
     * horizontally.
     */
    public static final int SOUTH = 15;

    /**
     * Put the component at the bottom-left corner of its display area.
     */
    public static final int SOUTHWEST = 16;

    /**
     * Put the component on the left side of its display area,
     * centered vertically.
     */
    public static final int WEST = 17;

    /**
     * Put the component at the top-left corner of its display area.
     */
    public static final int NORTHWEST = 18;

    /**
     * Place the component centered along the edge of its display area
     * associated with the start of a page for the current
     * {@code ComponentOrientation}.  Equal to NORTH for horizontal
     * orientations.
     */
    public static final int PAGE_START = 19;

    /**
     * Place the component centered along the edge of its display area
     * associated with the end of a page for the current
     * {@code ComponentOrientation}.  Equal to SOUTH for horizontal
     * orientations.
     */
    public static final int PAGE_END = 20;

    /**
     * Place the component centered along the edge of its display area where
     * lines of text would normally begin for the current
     * {@code ComponentOrientation}.  Equal to WEST for horizontal,
     * left-to-right orientations and EAST for horizontal, right-to-left
     * orientations.
     */
    public static final int LINE_START = 21;

    /**
     * Place the component centered along the edge of its display area where
     * lines of text would normally end for the current
     * {@code ComponentOrientation}.  Equal to EAST for horizontal,
     * left-to-right orientations and WEST for horizontal, right-to-left
     * orientations.
     */
    public static final int LINE_END = 22;

    /**
     * Place the component in the corner of its display area where
     * the first line of text on a page would normally begin for the current
     * {@code ComponentOrientation}.  Equal to NORTHWEST for horizontal,
     * left-to-right orientations and NORTHEAST for horizontal, right-to-left
     * orientations.
     */
    public static final int FIRST_LINE_START = 23;

    /**
     * Place the component in the corner of its display area where
     * the first line of text on a page would normally end for the current
     * {@code ComponentOrientation}.  Equal to NORTHEAST for horizontal,
     * left-to-right orientations and NORTHWEST for horizontal, right-to-left
     * orientations.
     */
    public static final int FIRST_LINE_END = 24;

    /**
     * Place the component in the corner of its display area where
     * the last line of text on a page would normally start for the current
     * {@code ComponentOrientation}.  Equal to SOUTHWEST for horizontal,
     * left-to-right orientations and SOUTHEAST for horizontal, right-to-left
     * orientations.
     */
    public static final int LAST_LINE_START = 25;

    /**
     * Place the component in the corner of its display area where
     * the last line of text on a page would normally end for the current
     * {@code ComponentOrientation}.  Equal to SOUTHEAST for horizontal,
     * left-to-right orientations and SOUTHWEST for horizontal, right-to-left
     * orientations.
     */
    public static final int LAST_LINE_END = 26;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally centered and
     * vertically aligned along the baseline of the prevailing row.
     * If the component does not have a baseline it will be vertically
     * centered.
     *
     * @since 1.6
     */
    public static final int BASELINE = 0x100;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally placed along the
     * leading edge.  For components with a left-to-right orientation,
     * the leading edge is the left edge.  Vertically the component is
     * aligned along the baseline of the prevailing row.  If the
     * component does not have a baseline it will be vertically
     * centered.
     *
     * @since 1.6
     */
    public static final int BASELINE_LEADING = 0x200;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally placed along the
     * trailing edge.  For components with a left-to-right
     * orientation, the trailing edge is the right edge.  Vertically
     * the component is aligned along the baseline of the prevailing
     * row.  If the component does not have a baseline it will be
     * vertically centered.
     *
     * @since 1.6
     */
    public static final int BASELINE_TRAILING = 0x300;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally centered.  Vertically
     * the component is positioned so that its bottom edge touches
     * the baseline of the starting row.  If the starting row does not
     * have a baseline it will be vertically centered.
     *
     * @since 1.6
     */
    public static final int ABOVE_BASELINE = 0x400;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally placed along the
     * leading edge.  For components with a left-to-right orientation,
     * the leading edge is the left edge.  Vertically the component is
     * positioned so that its bottom edge touches the baseline of the
     * starting row.  If the starting row does not have a baseline it
     * will be vertically centered.
     *
     * @since 1.6
     */
    public static final int ABOVE_BASELINE_LEADING = 0x500;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally placed along the
     * trailing edge.  For components with a left-to-right
     * orientation, the trailing edge is the right edge.  Vertically
     * the component is positioned so that its bottom edge touches
     * the baseline of the starting row.  If the starting row does not
     * have a baseline it will be vertically centered.
     *
     * @since 1.6
     */
    public static final int ABOVE_BASELINE_TRAILING = 0x600;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally centered.  Vertically
     * the component is positioned so that its top edge touches the
     * baseline of the starting row.  If the starting row does not
     * have a baseline it will be vertically centered.
     *
     * @since 1.6
     */
    public static final int BELOW_BASELINE = 0x700;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally placed along the
     * leading edge.  For components with a left-to-right orientation,
     * the leading edge is the left edge.  Vertically the component is
     * positioned so that its top edge touches the baseline of the
     * starting row.  If the starting row does not have a baseline it
     * will be vertically centered.
     *
     * @since 1.6
     */
    public static final int BELOW_BASELINE_LEADING = 0x800;

    /**
     * Possible value for the <code>anchor</code> field.  Specifies
     * that the component should be horizontally placed along the
     * trailing edge.  For components with a left-to-right
     * orientation, the trailing edge is the right edge.  Vertically
     * the component is positioned so that its top edge touches the
     * baseline of the starting row.  If the starting row does not
     * have a baseline it will be vertically centered.
     *
     * @since 1.6
     */
    public static final int BELOW_BASELINE_TRAILING = 0x900;

    /**
     * Specifies the cell containing the leading edge of the component's
     * display area, where the first cell in a row has <code>gridx=0</code>.
     * The leading edge of a component's display area is its left edge for
     * a horizontal, left-to-right container and its right edge for a
     * horizontal, right-to-left container.
     * The value
     * <code>RELATIVE</code> specifies that the component be placed
     * immediately following the component that was added to the container
     * just before this component was added.
     * <p>
     * The default value is <code>RELATIVE</code>.
     * <code>gridx</code> should be a non-negative value.
     * @serial
     * @see #clone()
     * @see GridCell#gridy
     * @see ComponentOrientation
     */
    public int gridx;

    /**
     * Specifies the cell at the top of the component's display area,
     * where the topmost cell has <code>gridy=0</code>. The value
     * <code>RELATIVE</code> specifies that the component be placed just
     * below the component that was added to the container just before
     * this component was added.
     * <p>
     * The default value is <code>RELATIVE</code>.
     * <code>gridy</code> should be a non-negative value.
     * @serial
     * @see #clone()
     * @see GridCell#gridx
     */
    public int gridy;

    /**
     * Specifies the number of cells in a row for the component's
     * display area.
     * <p>
     * Use <code>REMAINDER</code> to specify that the component's
     * display area will be from <code>gridx</code> to the last
     * cell in the row.
     * Use <code>RELATIVE</code> to specify that the component's
     * display area will be from <code>gridx</code> to the next
     * to the last one in its row.
     * <p>
     * <code>gridwidth</code> should be non-negative and the default
     * value is 1.
     * @serial
     * @see #clone()
     * @see GridCell#gridheight
     */
    public int gridwidth;

    /**
     * Specifies the number of cells in a column for the component's
     * display area.
     * <p>
     * Use <code>REMAINDER</code> to specify that the component's
     * display area will be from <code>gridy</code> to the last
     * cell in the column.
     * Use <code>RELATIVE</code> to specify that the component's
     * display area will be from <code>gridy</code> to the next
     * to the last one in its column.
     * <p>
     * <code>gridheight</code> should be a non-negative value and the
     * default value is 1.
     * @serial
     * @see #clone()
     * @see GridCell#gridwidth
     */
    public int gridheight;

    /**
     * Specifies how to distribute extra horizontal space.
     * <p>
     * The grid bag layout manager calculates the weight of a column to
     * be the maximum <code>weightx</code> of all the components in a
     * column. If the resulting layout is smaller horizontally than the area
     * it needs to fill, the extra space is distributed to each column in
     * proportion to its weight. A column that has a weight of zero receives
     * no extra space.
     * <p>
     * If all the weights are zero, all the extra space appears between
     * the grids of the cell and the left and right edges.
     * <p>
     * The default value of this field is <code>0</code>.
     * <code>weightx</code> should be a non-negative value.
     * @serial
     * @see #clone()
     * @see GridCell#weighty
     */
    public double weightx;

    /**
     * Specifies how to distribute extra vertical space.
     * <p>
     * The grid bag layout manager calculates the weight of a row to be
     * the maximum <code>weighty</code> of all the components in a row.
     * If the resulting layout is smaller vertically than the area it
     * needs to fill, the extra space is distributed to each row in
     * proportion to its weight. A row that has a weight of zero receives no
     * extra space.
     * <p>
     * If all the weights are zero, all the extra space appears between
     * the grids of the cell and the top and bottom edges.
     * <p>
     * The default value of this field is <code>0</code>.
     * <code>weighty</code> should be a non-negative value.
     * @serial
     * @see #clone()
     * @see GridCell#weightx
     */
    public double weighty;

    /**
     * This field is used when the component is smaller than its
     * display area. It determines where, within the display area, to
     * place the component.
     * <p> There are three kinds of possible values: orientation
     * relative, baseline relative and absolute.  Orientation relative
     * values are interpreted relative to the container's component
     * orientation property, baseline relative values are interpreted
     * relative to the baseline and absolute values are not.  The
     * absolute values are:
     * <code>CENTER</code>, <code>NORTH</code>, <code>NORTHEAST</code>,
     * <code>EAST</code>, <code>SOUTHEAST</code>, <code>SOUTH</code>,
     * <code>SOUTHWEST</code>, <code>WEST</code>, and <code>NORTHWEST</code>.
     * The orientation relative values are: <code>PAGE_START</code>,
     * <code>PAGE_END</code>,
     * <code>LINE_START</code>, <code>LINE_END</code>,
     * <code>FIRST_LINE_START</code>, <code>FIRST_LINE_END</code>,
     * <code>LAST_LINE_START</code> and <code>LAST_LINE_END</code>.  The
     * baseline relative values are:
     * <code>BASELINE</code>, <code>BASELINE_LEADING</code>,
     * <code>BASELINE_TRAILING</code>,
     * <code>ABOVE_BASELINE</code>, <code>ABOVE_BASELINE_LEADING</code>,
     * <code>ABOVE_BASELINE_TRAILING</code>,
     * <code>BELOW_BASELINE</code>, <code>BELOW_BASELINE_LEADING</code>,
     * and <code>BELOW_BASELINE_TRAILING</code>.
     * The default value is <code>CENTER</code>.
     * @serial
     * @see #clone()
     * @see ComponentOrientation
     */
    public int anchor;

    /**
     * This field is used when the component's display area is larger
     * than the component's requested size. It determines whether to
     * resize the component, and if so, how.
     * <p>
     * The following values are valid for <code>fill</code>:
     *
     * <ul>
     * <li>
     * <code>NONE</code>: Do not resize the component.
     * <li>
     * <code>HORIZONTAL</code>: Make the component wide enough to fill
     *         its display area horizontally, but do not change its height.
     * <li>
     * <code>VERTICAL</code>: Make the component tall enough to fill its
     *         display area vertically, but do not change its width.
     * <li>
     * <code>BOTH</code>: Make the component fill its display area
     *         entirely.
     * </ul>
     * <p>
     * The default value is <code>NONE</code>.
     * @serial
     * @see #clone()
     */
    public int fill;

    /**
     * This field specifies the external padding of the component, the
     * minimum amount of space between the component and the edges of its
     * display area.
     * <p>
     * The default value is <code>new Insets(0, 0, 0, 0)</code>.
     * @serial
     * @see #clone()
     */
    public Insets insets;

    /**
     * This field specifies the internal padding of the component, how much
     * space to add to the minimum width of the component. The width of
     * the component is at least its minimum width plus
     * <code>ipadx</code> pixels.
     * <p>
     * The default value is <code>0</code>.
     * @serial
     * @see #clone()
     * @see GridCell#ipady
     */
    public int ipadx;

    /**
     * This field specifies the internal padding, that is, how much
     * space to add to the minimum height of the component. The height of
     * the component is at least its minimum height plus
     * <code>ipady</code> pixels.
     * <p>
     * The default value is 0.
     * @serial
     * @see #clone()
     * @see GridCell#ipadx
     */
    public int ipady;

    /**
     * Temporary place holder for the x coordinate.
     * @serial
     */
    int tempX;
    /**
     * Temporary place holder for the y coordinate.
     * @serial
     */
    int tempY;
    /**
     * Temporary place holder for the Width of the component.
     * @serial
     */
    int tempWidth;
    /**
     * Temporary place holder for the Height of the component.
     * @serial
     */
    int tempHeight;
    /**
     * The minimum width of the component.  It is used to calculate
     * <code>ipady</code>, where the default will be 0.
     * @serial
     * @see #ipady
     */
    int minWidth;
    /**
     * The minimum height of the component. It is used to calculate
     * <code>ipadx</code>, where the default will be 0.
     * @serial
     * @see #ipadx
     */
    int minHeight;

    // The following fields are only used if the anchor is
    // one of BASELINE, BASELINE_LEADING or BASELINE_TRAILING.
    // ascent and descent include the insets and ipady values.
    transient int ascent;
    transient int descent;
    transient ComponentBaselineResizeBehavior baselineResizeBehavior;
    // The folllowing two fields are used if the baseline type is
    // CENTER_OFFSET.
    // centerPadding is either 0 or 1 and indicates if
    // the height needs to be padded by one when calculating where the
    // baseline lands
    transient int centerPadding;
    // Where the baseline lands relative to the center of the component.
    transient int centerOffset;

    /*
     * JDK 1.1 serialVersionUID
     */
    private static final long serialVersionUID = -1000070633030801713L;

    public GridCell(String pattern){
        this();
        gridx = RELATIVE;
        gridy = RELATIVE;
        gridwidth = 1;
        gridheight = 1;

        weightx = 0;
        weighty = 0;
        anchor = CENTER;
        fill = NONE;

        insets = new Insets(0, 0, 0, 0);
        ipadx = 0;
        ipady = 0;
        if(pattern!=null){
            for (char c : pattern.toCharArray()) {
                switch (c){
                    case '<':{
                        if(anchor== GridCell.NORTH){
                            anchor = GridCell.NORTHWEST;
                        }else if(anchor== GridCell.SOUTH){
                            anchor = GridCell.SOUTHWEST;
                        }else if(anchor== GridCell.EAST) {
                            anchor = GridCell.CENTER;
                        }else if(anchor== GridCell.WEST) {
                            anchor = GridCell.WEST;
                        }else{
                            anchor = GridCell.WEST;
                        }
                        break;
                    }
                    case '>':{
                        if(anchor== GridCell.NORTH){
                            anchor = GridCell.NORTHEAST;
                        }else if(anchor== GridCell.SOUTH){
                            anchor = GridCell.SOUTHEAST;
                        }else if(anchor== GridCell.EAST) {
                            anchor = GridCell.EAST;
                        }else if(anchor== GridCell.WEST) {
                            anchor = GridCell.CENTER;
                        }else{
                            anchor = GridCell.EAST;
                        }
                        break;
                    }
                    case '^':{
                        if(anchor== GridCell.NORTH){
                            anchor = GridCell.NORTH;
                        }else if(anchor== GridCell.CENTER){
                            anchor = GridCell.SOUTHEAST;
                        }else if(anchor== GridCell.EAST) {
                            anchor = GridCell.NORTHEAST;
                        }else if(anchor== GridCell.WEST) {
                            anchor = GridCell.NORTHWEST;
                        }else{
                            anchor = GridCell.NORTH;
                        }
                        break;
                    }
                    case '_':{
                        if(anchor== GridCell.NORTH){
                            anchor = GridCell.CENTER;
                        }else if(anchor== GridCell.CENTER){
                            anchor = GridCell.SOUTH;
                        }else if(anchor== GridCell.EAST) {
                            anchor = GridCell.SOUTHEAST;
                        }else if(anchor== GridCell.WEST) {
                            anchor = GridCell.SOUTHWEST;
                        }else{
                            anchor = GridCell.SOUTH;
                        }
                        break;
                    }
                    case '!':{
                        gridheight++;
                        break;
                    }
                    case ' ':{
                        gridwidth++;
                        break;
                    }
//                    case '+':{
//                        gridheight++;
//                        gridwidth++;
//                        break;
//                    }
                    case '=':{
                        weightx++;
                        break;
                    }
                    case '$':{
                        weighty++;
                        break;
                    }
                    case '*':{
                        weightx++;
                        weighty++;
                        break;
                    }
                    case '-':{
                        fill=HORIZONTAL;
                        break;
                    }
                    case '|':{
                        fill=VERTICAL;
                        break;
                    }
                    case '+':{
                        fill=BOTH;
                        break;
                    }
                    case '.':{
                        gridwidth=REMAINDER;
                        break;
                    }
                    case ';':{
                        gridheight=REMAINDER;
                        break;
                    }
                }
            }
        }

//
//        case :
//        r.x += diffx / 2;
//        alignOnBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.BASELINE_LEADING:
//        if (rightToLeft) {
//            r.x += diffx;
//        }
//        alignOnBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.BASELINE_TRAILING:
//        if (!rightToLeft) {
//            r.x += diffx;
//        }
//        alignOnBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.ABOVE_BASELINE:
//        r.x += diffx / 2;
//        alignAboveBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.ABOVE_BASELINE_LEADING:
//        if (rightToLeft) {
//            r.x += diffx;
//        }
//        alignAboveBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.ABOVE_BASELINE_TRAILING:
//        if (!rightToLeft) {
//            r.x += diffx;
//        }
//        alignAboveBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.BELOW_BASELINE:
//        r.x += diffx / 2;
//        alignBelowBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.BELOW_BASELINE_LEADING:
//        if (rightToLeft) {
//            r.x += diffx;
//        }
//        alignBelowBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.BELOW_BASELINE_TRAILING:
//        if (!rightToLeft) {
//            r.x += diffx;
//        }
//        alignBelowBaseline(constraints, r, cellY, cellHeight);
//        break;
//        case GridCell.CENTER:
//        r.x += diffx / 2;
//        r.y += diffy / 2;
//        break;
//        case GridCell.PAGE_START:
//        case GridCell.NORTH:
//        r.x += diffx / 2;
//        break;
//        case GridCell.NORTHEAST:
//        r.x += diffx;
//        break;
//        case GridCell.EAST:
//        r.x += diffx;
//        r.y += diffy / 2;
//        break;
//        case GridCell.SOUTHEAST:
//        r.x += diffx;
//        r.y += diffy;
//        break;
//        case GridCell.PAGE_END:
//        case GridCell.SOUTH:
//        r.x += diffx / 2;
//        r.y += diffy;
//        break;
//        case GridCell.SOUTHWEST:
//        r.y += diffy;
//        break;
//        case GridCell.WEST:
//        r.y += diffy / 2;
//        break;
//        case GridCell.NORTHWEST:
//        break;
//        case GridCell.LINE_START:
//        if (rightToLeft) {
//            r.x += diffx;
//        }
//        r.y += diffy / 2;
//        break;
//        case GridCell.LINE_END:
//        if (!rightToLeft) {
//            r.x += diffx;
//        }
//        r.y += diffy / 2;
//        break;
//        case GridCell.FIRST_LINE_START:
//        if (rightToLeft) {
//            r.x += diffx;
//        }
//        break;
//        case GridCell.FIRST_LINE_END:
//        if (!rightToLeft) {
//            r.x += diffx;
//        }
//        break;
//        case GridCell.LAST_LINE_START:
//        if (rightToLeft) {
//            r.x += diffx;
//        }
//        r.y += diffy;
//        break;
//        case GridCell.LAST_LINE_END:
//        if (!rightToLeft) {
//            r.x += diffx;
//        }
//        r.y += diffy;
//        break;
//        default:
//        throw new IllegalArgumentException("illegal anchor value");
    }

    /**
     * Creates a <code>GridBagConstraint</code> object with
     * all of its fields set to their default value.
     */
    public GridCell() {
        gridx = RELATIVE;
        gridy = RELATIVE;
        gridwidth = 1;
        gridheight = 1;

        weightx = 0;
        weighty = 0;
        anchor = CENTER;
        fill = NONE;

        insets = new Insets(0, 0, 0, 0);
        ipadx = 0;
        ipady = 0;
    }

    /**
     * Creates a <code>GridBagConstraints</code> object with
     * all of its fields set to the passed-in arguments.
     *
     * Note: Because the use of this constructor hinders readability
     * of source code, this constructor should only be used by
     * automatic source code generation tools.
     *
     * @param gridx     The initial gridx value.
     * @param gridy     The initial gridy value.
     * @param gridwidth The initial gridwidth value.
     * @param gridheight        The initial gridheight value.
     * @param weightx   The initial weightx value.
     * @param weighty   The initial weighty value.
     * @param anchor    The initial anchor value.
     * @param fill      The initial fill value.
     * @param insets    The initial insets value.
     * @param ipadx     The initial ipadx value.
     * @param ipady     The initial ipady value.
     *
     * @see GridCell#gridx
     * @see GridCell#gridy
     * @see GridCell#gridwidth
     * @see GridCell#gridheight
     * @see GridCell#weightx
     * @see GridCell#weighty
     * @see GridCell#anchor
     * @see GridCell#fill
     * @see GridCell#insets
     * @see GridCell#ipadx
     * @see GridCell#ipady
     *
     * @since 1.2
     */
    public GridCell(int gridx, int gridy,
                    int gridwidth, int gridheight,
                    double weightx, double weighty,
                    int anchor, int fill,
                    Insets insets, int ipadx, int ipady) {
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridwidth = gridwidth;
        this.gridheight = gridheight;
        this.fill = fill;
        this.ipadx = ipadx;
        this.ipady = ipady;
        this.insets = insets;
        this.anchor  = anchor;
        this.weightx = weightx;
        this.weighty = weighty;
    }

    /**
     * Creates a copy of this grid bag constraint.
     * @return     a copy of this grid bag constraint
     */
    public Object clone () {
        try {
            GridCell c = (GridCell)super.clone();
            c.insets = (Insets)insets.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new IllegalArgumentException(e);
        }
    }

    boolean isVerticallyResizable() {
        return (fill == BOTH || fill == VERTICAL);
    }
}
