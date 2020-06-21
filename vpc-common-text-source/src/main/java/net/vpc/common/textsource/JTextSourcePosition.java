package net.vpc.common.textsource;

public interface JTextSourcePosition {
    int getCurrentRowNumber();

    int getCurrentColumnNumber();

    int getCurrentCharNumber();

    int getCurrentTokenNumber();

    JTextSourcePosition readOnly();
}
