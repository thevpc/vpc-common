/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.common.prs.softreflect;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author thevpc
 */
public interface SoftClassBuilder {
    SoftClass buildClass(InputStream stream) throws IOException;
}
