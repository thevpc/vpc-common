/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.filetemplate;

/**
 *
 * @author vpc
 */
public interface ExprEvaluator {

    Object eval(String content, FileTemplater context);
    
}
