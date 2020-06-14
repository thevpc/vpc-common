package net.vpc.common.jeep;

import net.vpc.common.jeep.core.tokens.JTokenDef;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface JTokenizer extends Iterable<JToken> {

    JTokenizerState getState();
    JTokenizerState getState(int id);

    void pushBack(JToken token);

    void pushBackAll(Collection<JToken> t);

    JToken peek();

    JToken[] peek(int count);

    /**
     * Read next acceptable token and return it. When this Tokenizer is
     * configured to return comments and white spaces, this methods is strictly
     * equivalent to {{@link #next()}} ; In all other cases, this method will
     * return the first 'acceptable' (aka non comments and/or not white space)
     * token.
     *
     * @return next tocken
     */
    JToken next();

    /**
     * read next token and return it whether or not it is a comment or a white
     * space
     *
     * @return next tocken
     */
    JToken read();

    JToken lastToken();

    /**
     * Return the underlining reader
     *
     * @return the reader
     */
    JTokenizerReader reader();

    /**
     * Acceptable Tokens iterator
     *
     * @return
     */
    @Override
    Iterator<JToken> iterator();

    /**
     * Acceptable Tokens stream
     *
     * @return
     */
    Stream<JToken> stream();

    void skipUntil(Predicate<JToken> t);

    void skipWhile(Predicate<JToken> t);

    void skip();

    void skip(int count);

    JTokenizerSnapshot snapshot();

    JTokenPattern[] getPatterns();

    JTokenizerState[] getStates();

    JTokenDef[] getTokenDefinitions();
}
