package net.vpc.common.config;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 18 juin 2006
 * Time: 17:38:47
 * To change this template use File | Settings | File Templates.
 */
public interface MetaDataHandler {
    public boolean handleMetaData(Properties2 props, String command, String metadata);
}
