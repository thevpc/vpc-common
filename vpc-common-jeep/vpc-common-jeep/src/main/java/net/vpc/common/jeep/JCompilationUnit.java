package net.vpc.common.jeep;

public interface JCompilationUnit {
    JSource getSource();

    JNode getAst();

    void setAst(JNode newNode);
}
