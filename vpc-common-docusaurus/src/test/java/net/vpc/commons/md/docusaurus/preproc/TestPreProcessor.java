/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md.docusaurus.preproc;

import net.vpc.commons.docusaurus.DocusaurusCtrl;
import net.vpc.commons.md.docusaurus.DocusaurusProject;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vpc
 */
public class TestPreProcessor {
    @Test
    public void test1(){
        DocusaurusCtrl proc=new DocusaurusCtrl(
                new DocusaurusProject("/data/public/git/nuts/website/nuts")
        );
        proc.run();
    }
}
