package net.vpc.common.jeep;

import java.util.List;

public interface JParser<T extends JNode> {
    JCompilationUnit compilationUnit();

    T parse();

}
