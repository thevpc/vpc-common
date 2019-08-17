/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.javashell;

/**
 *
 * @author vpc
 */
public class DefaultJShellErrorHandler implements JShellErrorHandler {

    @Override
    public boolean isRequireExit(Throwable th) {
        return th instanceof JShellQuitException;
    }

    @Override
    public int errorToCode(Throwable th) {
        return 1;
    }

    @Override
    public String errorToMessage(Throwable th) {
        return th.toString();
    }

    @Override
    public void onErrorImpl(String message, Throwable th, JShellContext context) {
        th.printStackTrace();
    }
}
