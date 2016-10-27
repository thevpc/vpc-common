package net.vpc.common.config;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:22:33
 * To change this template use File | Settings | File Templates.
 */
public class ConfigOptions {
     DateFormat dateFormat;
     NumberFormat numberFormat;
     boolean helpAsEntry;
     transient File file;
     boolean autoSave;
     boolean storeComments=true;
     boolean readOnly;

    ConfigOptions() {
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean enable) {
        autoSave = enable;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public DateFormat getDateFormat() {
        return dateFormat != null ? dateFormat : DateFormat.getInstance();
    }

    public NumberFormat getNumberFormat() {
        return numberFormat != null ? numberFormat : NumberFormat.getInstance();
    }

    public boolean isHelpAsEntry() {
        return helpAsEntry;
    }

    public void setHelpAsEntry(boolean enable) {
        helpAsEntry = enable;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isStoreComments() {
        return storeComments;
    }

    public void setStoreComments(boolean storeComments) {
        this.storeComments = storeComments;
    }
    
}
