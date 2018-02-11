package net.vpc.common.prs.factory;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 07/04/11
 * Time: 13:22
 * To change this template use File | Settings | File Templates.
 */
public interface FactoryOwnerFilter {
    public boolean acceptOwner(Object owner);
}
