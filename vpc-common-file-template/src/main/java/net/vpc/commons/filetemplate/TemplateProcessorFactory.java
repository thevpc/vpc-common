/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.filetemplate;

/**
 *
 * @author vpc
 */
public interface TemplateProcessorFactory {
    public TemplateProcessor getProcessor(String mimeType);
}
