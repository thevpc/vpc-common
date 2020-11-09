package net.thevpc.jshell.parser2.ctx;

import net.thevpc.jshell.parser2.AbstractContext;
import net.thevpc.jshell.parser2.JShellParser2;
import net.thevpc.jshell.parser2.StrReader;
import net.thevpc.jshell.parser2.Token;

public class SimpleQuotedContext extends AbstractContext {
    public SimpleQuotedContext(JShellParser2 jshp) {
        super(jshp);
    }

    @Override
    public Token nextToken() {
        StrReader reader = this.reader.strReader();
        StringBuilder sb=new StringBuilder();
        while(true){
            int r = reader.read();
            if (r < 0) {
                break;
            }
            char rc=(char)r;
            if (rc == '\'') {
                break;
            }else{
                sb.append(rc);
            }
        }
        return new Token("'",sb.toString());
    }
}
