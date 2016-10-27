package net.vpc.common.config;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:38:37
 * To change this template use File | Settings | File Templates.
 */
public class LoadMetaDataHandler implements MetaDataHandler {
    public boolean handleMetaData(Properties2 props, String command, String metadata) {
        if ("load".equals(command)) {
            Properties2 p2 = new Properties2();
            try {
                p2.load(new File(props.getLoadingFile() == null ? null : props.getLoadingFile().getParentFile(), metadata.substring(4).trim()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            props.putAll(p2);
            return true;
        }
        return false;
    }
}
