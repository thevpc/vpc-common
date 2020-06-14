/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.iconset;

import javax.swing.ImageIcon;
import net.vpc.common.props.PValue;
import net.vpc.common.props.WritablePValue;

/**
 *
 * @author vpc
 */
public interface PIconSet {

    WritablePValue<String> id();

    PValue<ImageIcon> icon(String id);
}
