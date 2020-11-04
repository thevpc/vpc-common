/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.filetemplate.eval;

import net.thevpc.commons.filetemplate.FileTemplater;

/**
 *
 * @author vpc
 */
public interface ExprEvalFct {

    String getName();

    Object evalFunction(ExprNode[] args, FileTemplater context);
}
