/**
 * ====================================================================
 *                        vpc-swingext library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.swing;

import java.io.File;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime 9 nov. 2006 11:52:18
 */
public interface RecentFilesModel {
    public File[] getFiles();
    public void setFiles(File[] files);

    void removeFile(File file);

    void addFile(File file);

    int getMaxRecentFiles();

    void setMaxRecentFiles(int maxRecentFiles);
}