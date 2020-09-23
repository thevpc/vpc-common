/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md;

/**
 *
 * @author vpc
 */
public class MdImage extends MdAbstractElement {

    private String type;
    private String imageTitle;
    private String imageUrl;

    public MdImage(String type, String imageTitle, String imageUrl) {
        this.type = type;
        this.imageTitle = imageTitle;
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public MdElementType getElementType() {
        return MdElementType.IMAGE;
    }

    @Override
    public String toString() {
        return "![" + imageTitle + "](" + imageUrl + ")";
    }

}
