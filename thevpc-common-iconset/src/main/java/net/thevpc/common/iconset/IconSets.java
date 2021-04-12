/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.iconset;

import java.io.File;
import javax.swing.ImageIcon;
import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.WritableLiMap;
import net.thevpc.common.props.WritableValue;

/**
 *
 * @author vpc
 */
public interface IconSets extends WritableLiMap<String,IconSet>{
    WritableValue<IconSetConfig> config();
    
    WritableValue<String> id();

    ObservableValue<ImageIcon> icon(String id);

    WritableValue<AppIconResolver> resolver();

    String iconIdForFile(File f, boolean selected, boolean expanded);

    String iconIdForFileName(String f, boolean selected, boolean expanded);

    ObservableValue<ImageIcon> iconForFile(File f, boolean selected, boolean expanded);

    ObservableValue<ImageIcon> iconForFileName(String f, boolean selected, boolean expanded);
    
}
