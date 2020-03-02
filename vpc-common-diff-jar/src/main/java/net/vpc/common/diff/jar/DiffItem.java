/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.diff.jar;

import java.util.List;

/**
 * @author vpc
 */
public interface DiffItem {

    String getName();

    String getKind();

    DiffStatus getStatus();

    String getDescription();

    List<DiffItem> children();
}
