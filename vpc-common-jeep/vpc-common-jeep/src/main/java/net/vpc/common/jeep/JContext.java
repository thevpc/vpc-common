/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import net.vpc.common.jeep.impl.DefaultJParsers;

import java.util.Map;

/**
 *
 * @author vpc
 */
public interface JContext {

    JTokens tokens();

    Map<String, Object> userProperties();

    JContext newContext();

    void debug(String message);

    JVars vars();

    JTypes types();

    JFunctions functions();

    JOperators operators();

    JResolvers resolvers();

    JEvaluators evaluators();

    JParsers parsers();

    JNode parse(String expression);

    Object evaluate(JNode expression);

    Object evaluate(String expression);

    Jeep manager();

    JCompilerLog log();

    JContext log(JCompilerLog log);

    JContext parent();

//    /**
//     * import aware type resolution
//     * @param nameUsingImports name
//     * @return Type
//     */
//    JType typeForName(String nameUsingImports);
//    /**
//     * import aware type resolution
//     * @param nameUsingImports name
//     * @return Type
//     */
//    JType typeForNameOrNull(String nameUsingImports);
//
//    /**
//     * import aware type resolution
//     * @param nameUsingImports name
//     * @return Type
//     */
//    JType typeForNameOrNull(JTypeName nameUsingImports);
//    /**
//     * import aware type resolution
//     * @param nameUsingImports name
//     * @return Type
//     */
//    JType typeForName(JTypeName nameUsingImports);

}
