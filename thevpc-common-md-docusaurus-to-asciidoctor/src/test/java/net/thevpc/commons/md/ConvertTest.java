/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.md;

import net.thevpc.commons.ljson.LJSON;
import net.thevpc.commons.md.convert.Docusaurus2Adoc;
import net.thevpc.commons.md.docusaurus.DocusaurusFile;
import net.thevpc.commons.md.docusaurus.DocusaurusFolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * @author thevpc
 */
public class ConvertTest {

    @Test
    private static Docusaurus2Adoc prepareContent(String content) {
        Docusaurus2Adoc a = new Docusaurus2Adoc("example", "example", new String[0],
                DocusaurusFolder.ofRoot(
                        DocusaurusFile.ofContent("example", "example", "example",
                                content, 0
                        ))
        );
        return a;
    }

    @Test
    private static Docusaurus2Adoc preparePath(Path path) {
        Docusaurus2Adoc a = new Docusaurus2Adoc("example", "example", new String[0],
                DocusaurusFolder.ofRoot(
                        DocusaurusFolder.of("One Part",
                                "One Part",
                                0,
                                LJSON.NULL,
                                DocusaurusFile.ofPath("example", "example", "example",
                                        path, 0
                                )
                        )
                ));
        return a;
    }

    //    @Test
    private static Docusaurus2Adoc prepareTree(MdElement path) {
        Docusaurus2Adoc a = new Docusaurus2Adoc("example", "example", new String[0],
                DocusaurusFolder.ofRoot(
                        DocusaurusFolder.of("One Part",
                                "One Part",
                                0,
                                LJSON.NULL,
                                DocusaurusFile.ofTree("example", "example", "example",
                                        path, 0
                                )
                        )
                ));
        return a;
    }

    //    @Test
//    public void test1() {
//        Docusaurus2Adoc a=prepare(
//            "Documentation about ```docusaurus``` markdown syntax is available here: [docusaurus-markdown](https://v2.docusaurus.io/docs/markdown-features) ."
//        );
//        
//        System.out.println(a.runToString());
//    }
//    @Test
//    public void test2() {
//        try {
//            DocusaurusMdParser p = new DocusaurusMdParser(new FileReader("common-comments.md"));
//            MdElement tree = p.parse();
////            System.out.println(tree);
//            System.out.println(prepareTree(tree).runToString());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
////        Docusaurus2Adoc a = preparePath(
////                Paths.get("common-formats.md")
////        );
////
////        System.out.println(a.runToString());
//    }
//    
    @Test
    public void test3() {
        try {
            String tree = "### 1.1.1 My Title\n"
                    + "- Always use exclusively **standard and academic US-English** for names\n"
                    + "- Always use exclusively **standard and academic US-English** for comments\n"
                    + "";
            String str = prepareContent(tree).runToString();
            Assertions.assertTrue(str.contains("exclusively **standard and academic US-English** for"));
            System.out.println(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
