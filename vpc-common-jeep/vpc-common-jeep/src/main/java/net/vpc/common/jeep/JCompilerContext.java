package net.vpc.common.jeep;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface JCompilerContext {
    JCompilerContext parent();

    JCompilationUnit compilationUnit();

    JCompilerContext parent(int index);

    JCompilerLog log();
    String packageName();

    JContext context();

    boolean isStage(int stageId);

    int iteration();

    int stage();

    JNodePath path();

    JNode node();

    JImportInfo[] imports();

//    JType typeForName(String name);

//    JType typeForName(JTypeName typename);

//    JType typeForNameOrNull(String nameUsingImports);


//    JNode lookupVarDeclaration(String name, JToken location);

//    JNode lookupVarDeclarationOrNull(String name, JToken location);


    default JCompilerContext addImport(JImportInfo value){
        return appendImport(value);
    }

    JCompilerContext appendImport(JImportInfo value);

//    JCompilerContext node(JNode node);

    JCompilerContext dropNode();


    JCompilerContext packageName(String packageName);

    JCompilerContext log(JCompilerLog log);

    JCompilerContext stage(int stageId);

    JCompilerContext iteration(int iterationNumber);

    JCompilerContext nextNode(JNode node);

    JCompilerContext compilationUnit(JCompilationUnit compilationUnit);
}
