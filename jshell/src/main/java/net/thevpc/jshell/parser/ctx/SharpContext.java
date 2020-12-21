package net.thevpc.jshell.parser.ctx;

import net.thevpc.jshell.parser.AbstractContext;
import net.thevpc.jshell.parser.JShellParser;
import net.thevpc.jshell.parser.StrReader;
import net.thevpc.jshell.parser.Token;

public class SharpContext extends AbstractContext {
    public SharpContext(JShellParser jshp) {
        super(jshp);
    }

    @Override
    public Token nextToken() {
        StrReader reader = this.reader.strReader();
        StringBuilder sb=new StringBuilder();
        int t = reader.peekChar();
        if(t!='#'){
            return null;
        }
        reader.read();
        while(true){
            int r = reader.peekChar();
            if (r < 0) {
                break;
            }
            char rc=(char)r;
            if (rc == '\n') {
                break;
            }else{
                reader.read();
                sb.append(rc);
            }
        }
        return new Token("#",sb.toString());
    }
}
