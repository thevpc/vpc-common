package net.thevpc.jshell.parser2;

class DoubleQuotedContext extends AbstractContext {
    public DoubleQuotedContext(StringReader2 stringReader2) {
        super(stringReader2);
    }

    @Override
    public Token nextToken() {
        int r = reader.peekChar();
        if (r < 0) {
            return null;
        }
        char rc = (char) r;

        if (reader.readString("$((")) {
            return processContext("$((", new DollarPar2Context(reader));
        }
        if (reader.readString("$(")) {
            return processContext("$(", new DollarParContext(reader));
        }
        if (reader.readString("${")) {
            return processContext("${", new DollarCurlBracketContext(reader));
        }
        if (rc == '$') {
            return continueReadDollarWord();
        }
        if (rc == '\"') {
            reader.read();
            return null;
        }
        if (rc == '`') {
            reader.read();
            return processContext(String.valueOf(rc), new AntiQuotedContext(reader));
        }


        StringBuilder sb = new StringBuilder();
        while (true) {
            r = reader.peekChar();
            if (r > 0) {
                rc = (char) r;
                if (isDblQChar(rc)) {
                    sb.append((char) reader.read());
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return new Token("STR", sb.toString());
    }

    boolean isDblQChar(char c) {
        switch (c) {
            case '\'':
            case '\"':
            case '$': {
                return false;
            }
        }
        return true;
    }
}
