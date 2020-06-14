/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.props;

import net.vpc.common.props.PValue;
import net.vpc.common.props.WritablePValue;

/**
 *
 * @author vpc
 */
public interface FileObject {

    PValue<String> name();
    
    WritablePValue<Boolean> modified();

    WritablePValue<String> filePath();

    String defaultFileSuffix();

    String fileTypeTitle();

    void save();

    void load();
}
