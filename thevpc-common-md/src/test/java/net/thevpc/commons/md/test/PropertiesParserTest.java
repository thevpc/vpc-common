/**
 * ====================================================================
 *            thevpc-common-md : Simple Markdown Manipulation Library
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
package net.thevpc.commons.md.test;

import java.util.Map;
import net.thevpc.commons.md.PropertiesParser;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vpc
 */
public class PropertiesParserTest {

    @Test
    public void test1() {
        String str = "  defaultValue=\"centos\"\n"
                + "  values={[\n"
                + "    { label: 'CentOS/RedHat', value: 'centos', },\n"
                + "    { label: 'OpenSuse/SLES', value: 'suse', },\n"
                + "    { label: 'Debian/Ububtu', value: 'ubuntu', },\n"
                + "    { label: 'MacOS', value: 'macos', },\n"
                + "    { label: 'Windows', value: 'windows', },\n"
                + "  ]\n"
                + "}";
        PropertiesParser sp = new PropertiesParser(str);
        while (true) {
            int t = sp.nextToken();
            if (t == PropertiesParser.EOF) {
                System.out.println("<EOF>");
                break;
            }
            System.out.println(sp.st_ttypeName + " " + sp.st_image);
        }
    }

    @Test
    public void test2() {
        String str = "  defaultValue=\"centos\"\n"
                + "  values={[\n"
                + "    { label: 'CentOS/RedHat', value: 'centos', },\n"
                + "    { label: 'OpenSuse/SLES', value: 'suse', },\n"
                + "    { label: 'Debian/Ububtu', value: 'ubuntu', },\n"
                + "    { label: 'MacOS', value: 'macos', },\n"
                + "    { label: 'Windows', value: 'windows', },\n"
                + "  ]\n"
                + "}";
        Map<String, String> map = new PropertiesParser(str).parseMap();
        System.out.println(map);
    }
}
