/**
 * ====================================================================
 *                        vpc-commons library
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
package net.thevpc.common.swings.util.jcmd;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * %creationtime  13 juil. 2006 22:14:21
 */
public class OptionParam extends CmdParam {
    private CmdOption relativeOption;
    private int relativeOptionIndex;

    OptionParam(String arg, int index, CmdOption cmdOption, int optionIndex) {
        super(arg, index);
        this.relativeOption = cmdOption;
        this.relativeOptionIndex = optionIndex;
    }

    public CmdOption getRelativeOption() {
        return relativeOption;
    }

    public int getRelativeOptionIndex() {
        return relativeOptionIndex;
    }
}
