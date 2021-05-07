package net.thevpc.common.swing.layout;

import java.awt.*;

public class GBC {
    private int gridx;
    private int gridy;
    private int anchor;
    private int gridwidth=1;
    private int gridheight=1;
    private int ipadx;
    private int ipady;
    private int fill;
    private double weightx;
    private double weighty;
    private Insets insets=new Insets(0,0,0,0);

    public static GBC of(int gridX, int gridy) {
        return new GBC().grid(gridX,gridy);
    }

    public GBC grid(int x, int y) {
        this.gridx = x;
        this.gridy = y;
        return this;
    }

    public GBC span(int x, int y) {
        this.gridwidth = x;
        this.gridheight = y;
        return this;
    }
    public GBC weight(double x, double y) {
        this.weightx = x;
        this.weighty = y;
        return this;
    }

    public GBC ipad(int x, int y) {
        this.ipadx = x;
        this.ipady = y;
        return this;
    }

    public GBC insets(Insets insets) {
        this.insets = insets;
        return this;
    }

    public GBC insetsx(int left, int right) {
        this.insets = new Insets(insets.top, left, insets.bottom, right);
        return this;
    }

    public GBC insetsy(int top, int bottom) {
        this.insets = new Insets(top, insets.left, bottom, insets.right);
        return this;
    }

    public GBC insets(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }

    public GBC fill(int fill) {
        this.fill = fill;
        return this;
    }

    public GBC fillH() {
        this.fill = GridBagConstraints.HORIZONTAL;
        return this;
    }

    public GBC fillV() {
        this.fill = GridBagConstraints.VERTICAL;
        return this;
    }

    public GBC fillBoth() {
        this.fill = GridBagConstraints.BOTH;
        return this;
    }

    public GBC anchorN() {
        return anchor(GridBagConstraints.PAGE_START);
    }

    public GBC anchorS() {
        return anchor(GridBagConstraints.PAGE_END);
    }

    public GBC anchorW() {
        return anchor(GridBagConstraints.LINE_START);
    }

    public GBC anchorE() {
        return anchor(GridBagConstraints.LINE_END);
    }

    public GBC anchorNW() {
        return anchor(GridBagConstraints.FIRST_LINE_START);
    }
    public GBC anchorSW() {
        return anchor(GridBagConstraints.FIRST_LINE_START);
    }
    public GBC anchorNE() {
        return anchor(GridBagConstraints.LAST_LINE_START);
    }
    public GBC anchorSE() {
        return anchor(GridBagConstraints.LAST_LINE_END);
    }

    public GBC anchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GridBagConstraints build() {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = gridx;
        g.gridy = gridy;
        g.anchor = anchor;
        g.gridwidth = gridwidth;
        g.gridheight = gridheight;
        g.fill = fill;
        g.insets = insets;
        g.weightx = weightx;
        g.weighty = weighty;
        g.ipadx = ipadx;
        g.ipady = ipady;
        return g;
    }
}
