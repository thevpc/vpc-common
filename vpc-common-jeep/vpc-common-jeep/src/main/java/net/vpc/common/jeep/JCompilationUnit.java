package net.vpc.common.jeep;

import net.vpc.common.textsource.JTextSource;

public interface JCompilationUnit {
    JTextSource getSource();

    JNode getAst();

    void setAst(JNode newNode);
}
