package net.thevpc.jshell.parser2.ctx;

import net.thevpc.jshell.parser2.AbstractContext;
import net.thevpc.jshell.parser2.JShellParser2;
import net.thevpc.jshell.parser2.StrReader;
import net.thevpc.jshell.parser2.Token;

public class SimpleQuotedContext extends AbstractContext {
    boolean processed=false;
    public SimpleQuotedContext(JShellParser2 jshp) {
        super(jshp);
    }

    @Override
    public Token nextToken() {
        if(processed){
            return null;
        }
        StrReader reader = this.reader.strReader();
        StringBuilder sb=new StringBuilder();
        while(true){
            int r = reader.read();
            if (r < 0) {
                if(sb.length()==0){
                    return null;
                }
                break;
            }
            char rc=(char)r;
            if (rc == '\'') {
                break;
            }else{
                sb.append(rc);
            }
        }
        processed=true;
        return new Token("'",sb.toString());
    }
}
