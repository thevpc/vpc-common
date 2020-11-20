package net.thevpc.jshell.parser2;

public interface Lexer {
    boolean skipWhites();

    Token peekToken();

    Token nextToken();
}
