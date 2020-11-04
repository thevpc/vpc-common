package net.thevpc.common.vfs;

import java.util.function.Predicate;

/**
 * Created by vpc on 1/1/17.
 */
public interface VFileNameGenerator {

    public String generateFileName(String baseName, int index);

    default String generateFileName(String baseName, Predicate<String> existsTest) {
        String validName = baseName;
        int index = 1;
        int maxIndex = 1024;
        while (index < maxIndex) {
            validName = generateFileName(baseName, index);
            if (!existsTest.test(validName)) {
                break;
            }
            index++;
        }
        return validName;
    }
}
