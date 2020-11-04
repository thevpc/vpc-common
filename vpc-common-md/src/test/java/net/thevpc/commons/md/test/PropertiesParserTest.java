/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
