package net.vpc.common.app;

import net.vpc.common.props.PValue;
import net.vpc.common.props.WritablePValue;

import javax.swing.*;
import java.io.File;

public interface AppIconSet {
    WritablePValue<String> id();

    PValue<ImageIcon> icon(String id);

    WritablePValue<AppIconResolver> resolver();

    String iconIdForFile(File f, boolean selected, boolean expanded);

    String iconIdForFileName(String f, boolean selected, boolean expanded);

    PValue<ImageIcon> iconForFile(File f, boolean selected, boolean expanded);

    PValue<ImageIcon> iconForFileName(String f, boolean selected, boolean expanded);
}
