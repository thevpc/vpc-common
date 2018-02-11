package net.vpc.common.swings.iswing;

import java.awt.*;
import java.awt.event.ContainerListener;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 28 août 2009
 * Time: 23:04:21
 * To change this template use File | Settings | File Templates.
 */
public interface IContainer extends IComponent{
    public void add(Component comp, Object constraints) ;
    public void add(Component comp, Object constraints,int index) ;
    public void addContainerListener(ContainerListener l) ;
    public void removeContainerListener(ContainerListener l) ;
    public Component getComponentAt(int x, int y);
    public LayoutManager getLayout() ;

    /**
     * Sets the layout manager for this container.
     * @param mgr the specified layout manager
     * @see #getLayout
     */
    public void setLayout(LayoutManager mgr) ;

}
