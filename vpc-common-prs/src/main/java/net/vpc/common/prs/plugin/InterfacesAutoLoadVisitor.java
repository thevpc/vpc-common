package net.vpc.common.prs.plugin;

import net.vpc.common.prs.softreflect.SoftClassVisitor;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.vpc.common.prs.log.LoggerProvider;
import net.vpc.common.prs.softreflect.SoftClass;
import net.vpc.common.prs.softreflect.SoftClassFilter;

/**
* Created by IntelliJ IDEA.
* User: vpc
* Date: 6 janv. 2010
* Time: 14:19:20
* To change this template use File | Settings | File Templates.
*/
class InterfacesAutoLoadVisitor implements SoftClassVisitor {
    private Set<String> interfaces;
    private SoftClassFilter interfaceFilter;
    private Logger logger;

    InterfacesAutoLoadVisitor(Set<String> interfaces,SoftClassFilter interfaceFilter,LoggerProvider loggerProvider) {
        this.interfaces = interfaces;
        this.logger = loggerProvider.getLogger(getClass().getName());
        this.interfaceFilter = interfaceFilter;
    }

    public void visit(SoftClass clazz) {
        if (interfaceFilter.accept(clazz)) {
            interfaces.add(clazz.getName());
            logger.log(Level.FINE, "Detected Extension : {0}", clazz.getName());
        }
    }
}
