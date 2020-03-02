package net.vpc.common.diff.jar.test;

import net.vpc.common.diff.jar.Diff;
import net.vpc.common.diff.jar.DiffResult;
import org.junit.Test;

import java.io.File;

public class TestJars {
    @Test
    public void test() {
        File f1 = new File(System.getProperty("user.home"), "test" + File.separator + "test-jar1.jar");
        File f2 = new File(System.getProperty("user.home"), "test" + File.separator + "test-jar2.jar");
        DiffResult result = Diff.of(f1, f2).verbose().eval();
        System.out.println(result);

    }
}
