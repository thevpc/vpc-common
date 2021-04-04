/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.props;

/**
 *
 * @author thevpc
 */
public interface FileObject {

    ObservableValue<String> name();
    
    WritableValue<Boolean> modified();

    WritableValue<String> filePath();

    String defaultFileSuffix();

    String fileTypeTitle();

    void save();

    void load();
}
