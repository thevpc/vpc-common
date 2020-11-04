/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.md;

import java.io.StringReader;
import net.thevpc.commons.md.docusaurus.DocusaurusMdParser;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vpc
 */
public class DocusaurusMdParserTest {

//    @Test
//    public void test1() {
//        DocusaurusMdParser p = new DocusaurusMdParser(new StringReader(""
//                + "## Official Code Conventions\n"
//                + "The following list refers to the official code conventions of the retained programming languages.\n"
//                + "- ![''](../static/img/java24.png) [Java Official Code Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-introduction.html)\n"
//                + "- ![''](../static/img/scala24.png) [Scala Official Code Conventions](https://docs.scala-lang.org/style/)\n"
//                + "- ![''](../static/img/csharp24.png) [C# Code Conventions](https://docs.microsoft.com/en-us/dotnet/csharp/programming-guide/inside-a-program/coding-conventions)\n"
//                + ""
//                + ""));
//        MdElement r = p.parse();
//        Assertions.assertTrue(r instanceof MdSequence);
//        MdImage m = r.asSeq().get(2).asUnNumItem().getValue().asSeq().get(0).asImage();
//        System.out.println(m);
//        System.out.println(r);
//        Assertions.assertTrue(true);
//    }

//    @Test
//    public void test2() {
//        DocusaurusMdParser p = new DocusaurusMdParser(new StringReader(""
//                + "| Language       | Java        | Scala         | C#            | Go         | Python   | JavaScript |\n"
//                + "| --------       | :----:      | :----:        | :----:        | :----:     | :----:   | :----:     |\n"
//                + "| Supported Term | N/A         | N/A           | N/A           | function ```fun```  | function ```def```|```function```    | \n"
//                + "| Alternatives    |```static``` method| ```object``` method | ```static``` method |            |          |            |\n"
//                + ""));
//        MdElement r = p.parse();
//        System.out.println(r);
//        Assertions.assertTrue(true);
//    }
    
    @Test
    public void test3() {
        DocusaurusMdParser p = new DocusaurusMdParser(new StringReader(""
                + "Documentation about ```docusaurus``` markdown syntax is available here: [docusaurus-markdown](https://v2.docusaurus.io/docs/markdown-features) ."));
        MdElement r = p.parse();
        System.out.println(r);
    }
}
