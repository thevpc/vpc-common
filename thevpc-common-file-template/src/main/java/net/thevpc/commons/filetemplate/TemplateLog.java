/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.filetemplate;

/**
 *
 * @author thevpc
 */
public interface TemplateLog {

    void info(String title, String message);

    void debug(String title, String message);

    void error(String title, String message);
}
