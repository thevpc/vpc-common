package net.vpc.common.fprint.test;

import net.vpc.common.fprint.FPrint;
import net.vpc.common.fprint.FormattedPrintStream;

public class FPrintExample {
    static String HELP="Nuts format types are :\n"+
            "\n"+
            "Text             | Text\n"+
            "\\_\\_Text\\_\\_         | __Text__\n"+
            "\\@Text\\@           | @Text@\n"+
            "\\@\\@Text\\@\\@         | @@Text@@\n"+
            "\\@\\@\\@Text\\@\\@\\@       | @@@Text@@@\n"+
            "\\[Text\\]           | [Text]\n"+
            "\\[\\[Text\\]\\]         | [[Text]]\n"+
            "\\[\\[\\[Text\\]\\]\\]       | [[[Text]]]\n"+
            "\\[\\[\\[\\[Text\\]\\]\\]\\]     | [[[[Text]]]]\n"+
            "\\[\\[\\[\\[\\[Text\\]\\]\\]\\]\\]   | [[[[[Text]]]]]\n"+
            "\\*Text\\*           | *Text*\n"+
            "\\*\\*Text\\*\\*         | **Text**\n"+
            "\\*\\*\\*Text\\*\\*\\*       | ***Text***\n"+
            "\\=Text\\=           | =Text=\n"+
            "\\=\\=Text\\=\\=         | ==Text==\n"+
            "\\=\\=\\=Text\\=\\=\\=       | ===Text===\n"+
            "\\^Text\\^           | ^Text^\n"+
            "\\^\\^Text\\^\\^         | ^^Text^^\n"+
            "\\^\\^\\^Text\\^\\^\\^       | ^^^Text^^^\n"+
            "\\(Text\\)           | (Text)\n"+
            "\\(\\(Text\\)\\)         | ((Text))\n"+
            "\\(\\(\\(Text\\)\\)\\)       | (((Text)))\n"+
            "\\(\\(\\(\\(Text\\)\\)\\)\\)     | ((((Text))))\n"+
            "\\(\\(\\(\\(\\(Text\\)\\)\\)\\)\\)   | (((((Text)))))\n"+
            "\\#Text\\#           | #Text#\n"+
            "\\#\\#Text\\#\\#         | ##Text##\n"+
            "\\#\\#\\#Text\\#\\#\\#       | ###Text###\n"+
            "\\{Text\\}           | {Text}\n"+
            "\\{\\{Text\\}\\}         | {{Text}}\n"+
            "\\{\\{\\{Text\\}\\}\\}       | {{{Text}}}\n"+
            "\\<Text\\>           | <Text>\n"+
            "\\<\\<Text\\>\\>         | <<Text>>\n"+
            "\\<\\<\\<Text\\>\\>\\>       | <<<Text>>>\n"+
            "\\\"Text\\\"           | \"Text\"\n"+
            "\\\"\\\"Text\\\"\\\"         | \"\"Text\"\"\n"+
            "\\\"\\\"\\\"Text\\\"\\\"\\\"       | \"\"\"Text\"\"\"\n"+
            "\\'Text\\'           | 'Text'\n"+
            "\\'\\'Text\\'\\'         | ''Text''\n"+
            "\\'\\'\\'Text\\'\\'\\'       | '''Text'''\n"+
            "\n"+
            "\n"+
            "You can escape special characters using ##\\\\## character\n";
    public static void main(String[] args) {
        FormattedPrintStream o = new FormattedPrintStream(System.out, FPrint.RENDERER_ANSI,FPrint.PARSER_DEFAULT);
        for (String string : HELP.split("\n")) {
//            System.out.println("\n\n:::: "+string);
            o.println(string);
        }
//        o.println("==a==");
//        o.printf("==a==\n");
//        o.printf("<a>\n");
    }
}
