/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.vfs.impl;

import net.thevpc.common.vfs.VirtualFileACL;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public interface SerializableVirtualFileACL extends VirtualFileACL {

    public byte[] toBytes();

}
