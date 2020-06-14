package net.vpc.common.jeep.impl.tokens;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.core.tokens.AbstractJTokenizer;
import net.vpc.common.jeep.core.tokens.JTokenDef;


public class NoCommentsNoSpacesJTokenizer extends AbstractJTokenizer {
    private JTokenizer tokenizer;
    private JToken lastToken;

    public NoCommentsNoSpacesJTokenizer(JTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    @Override
    public void pushBack(JToken token) {
        tokenizer.pushBack(token);
    }

    @Override
    public JToken next() {
        JToken token = null;
        while (true) {
            token = tokenizer.next();
            //force ignore comments && white spaces
            if (token.isComments() || token.isWhiteSpace()) {
                //ignore
            } else {
                break;
            }
        }
        if(token.def.id!=JTokenId.EOF){
            lastToken=token;
        }
        return token;
    }
    @Override
    public JToken lastToken() {
        return lastToken;
    }

    @Override
    public JToken read() {
        return tokenizer.read();
    }



    @Override
    public JTokenizerReader reader() {
        return tokenizer.reader();
    }

    @Override
    public JTokenizerSnapshot snapshot() {
        return tokenizer.snapshot();
    }

    @Override
    public JTokenPattern[] getPatterns() {
        return tokenizer.getPatterns();
    }

    @Override
    public JTokenDef[] getTokenDefinitions() {
        return tokenizer.getTokenDefinitions();
    }

    @Override
    public JTokenizerState getState(int id) {
        return tokenizer.getState(id);
    }

    @Override
    public JTokenizerState[] getStates() {
        return tokenizer.getStates();
    }

    @Override
    public JTokenizerState getState() {
        return tokenizer.getState();
    }
}
