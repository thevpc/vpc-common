/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.diff.jar;

/**
 *
 * @author vpc
 */
public interface DiffHashFactory {
    DiffHash get(String entryName);
}
