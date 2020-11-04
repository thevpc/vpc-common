package net.thevpc.common.prs.softreflect;

/**
 * Created by IntelliJ IDEA.
* User: vpc
* Date: 27 août 2009
* Time: 20:42:54
* To change this template use File | Settings | File Templates.
*/
public interface SoftClassVisitor {
    /**
     *
     * @param clazz visited class
     * @return false to cancel iteration
     */
    public void visit(SoftClass clazz) throws CancelVisitException;
}
