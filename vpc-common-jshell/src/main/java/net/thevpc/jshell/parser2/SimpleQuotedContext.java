package net.thevpc.jshell.parser2;

class SimpleQuotedContext extends AbstractContext {
    public SimpleQuotedContext(StringReader2 stringReader2) {
        super(stringReader2);
    }

    @Override
    public Token nextToken() {
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
