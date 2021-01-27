/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.vfs.impl;

import net.thevpc.common.vfs.VirtualFileACL;
import java.util.Collections;
import java.util.Set;
import net.thevpc.common.vfs.VFileType;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class DefaultVirtualFileACL implements VirtualFileACL {

    public static final VirtualFileACL READ_WRITE = new DefaultVirtualFileACL(null,ACLPermission.GRANT, ACLPermission.GRANT, ACLPermission.GRANT, ACLPermission.GRANT, ACLPermission.GRANT, ACLPermission.GRANT, ACLPermission.GRANT);
    public static final VirtualFileACL READ_ONLY = new DefaultVirtualFileACL(null,ACLPermission.DENY, ACLPermission.DENY, ACLPermission.DENY, ACLPermission.GRANT, ACLPermission.DENY, ACLPermission.GRANT, ACLPermission.DENY);
    private final ACLPermission allowedCreateChild;
    private final ACLPermission allowedRemoveChild;
    private final ACLPermission allowedUpdateChild;
    private final ACLPermission allowedList;
    private final ACLPermission allowedRemove;
    private final ACLPermission allowedRead;
    private final ACLPermission allowedWrite;
    private final String owner;

    public DefaultVirtualFileACL(String owner,ACLPermission allowedCreateChild, ACLPermission allowedRemoveChild, ACLPermission allowedUpdateChild, ACLPermission allowedList, ACLPermission allowedRemove, ACLPermission allowedRead, ACLPermission allowedWrite) {
        this.allowedCreateChild = allowedCreateChild;
        this.allowedRemoveChild = allowedRemoveChild;
        this.allowedUpdateChild = allowedUpdateChild;
        this.allowedList = allowedList;
        this.allowedRemove = allowedRemove;
        this.allowedRead = allowedRead;
        this.allowedWrite = allowedWrite;
        this.owner= owner;
    }

    @Override
    public ACLPermission getAllowedCreateChildPermission(VFileType type, String user) {
        return allowedCreateChild;
    }

    @Override
    public ACLPermission getAllowedRemoveChildPermission(VFileType type, String user) {
        return allowedRemoveChild;
    }

    @Override
    public ACLPermission getAllowedUpdateChildPermission(VFileType type, String user) {
        return allowedUpdateChild;
    }

    @Override
    public ACLPermission getAllowedListPermission(String user) {
        return allowedList;
    }

    @Override
    public ACLPermission getAllowedRemovePermission(String user) {
        return allowedRemove;
    }

    @Override
    public ACLPermission getAllowedReadPermission(String user) {
        return allowedRead;
    }

    @Override
    public ACLPermission getAllowedWritePermission(String user) {
        return allowedWrite;
    }

    @Override
    public VirtualFileACL getDefaultFileACL() {
        return this;
    }

    @Override
    public VirtualFileACL getDefaultFolderACL() {
        return this;
    }

    @Override
    public String getProperty(String name) {
        return null;
    }

    @Override
    public void setProperty(String name, String value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Set<String> getPropertyNames() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setOwner(String newOwner) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public void setPermissionCreateFile(String profiles) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public void setPermissionCreateDirectory(String profiles) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public void setPermissionReadFile(String profiles) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public void setPermissionWriteFile(String profiles) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public void setPermissionListDirectory(String profiles) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public boolean isPropagateOwner() {
        return false;
    }

    @Override
    public void setPropagateOwner(boolean value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean isPropagateACL() {
        return false;
    }

    @Override
    public void setPropagateACL(boolean value) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setPermissionRemoveFile(String profiles) {

    }

    @Override
    public void setPermissionRemoveDirectory(String profiles) {

    }

    @Override
    public void setPermissionRemove(String profiles) {

    }

    @Override
    public String getPermissionRemove() {
        return null;
    }

    @Override
    public String getPermissionReadFile() {
        return null;
    }

    @Override
    public String getPermissionWriteFile() {
        return null;
    }

    @Override
    public String getPermissionRemoveFile() {
        return null;
    }

    @Override
    public String getPermissionRemoveDirectory() {
        return null;
    }

    @Override
    public String getPermissionListDirectory() {
        return null;
    }

    @Override
    public String getPermissionCreateDirectory() {
        return null;
    }

    @Override
    public String getPermissionCreateFile() {
        return null;
    }

    @Override
    public boolean isAutoSave() {
        return false;
    }

    @Override
    public void setAutoSave(boolean enable) {
    }

    @Override
    public void save() {
    }
    
}
