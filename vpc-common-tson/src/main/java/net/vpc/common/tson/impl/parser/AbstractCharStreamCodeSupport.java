package net.vpc.common.tson.impl.parser;

public abstract class AbstractCharStreamCodeSupport implements CharStreamCodeSupport {

    @Override
    public void next(char cbuf[]){
        next(cbuf,0,cbuf.length);
    }

    @Override
    public void next(char cbuf[], int off, int len){
        int max=off+len;
        for (int i = off; i < max; i++) {
            next(cbuf[i]);
        }
    }
}
