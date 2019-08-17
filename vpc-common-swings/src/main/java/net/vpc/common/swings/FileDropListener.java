package net.vpc.common.swings;

/**
 * Implement this inner interface to listen for when files are dropped. For example
 * your class declaration may begin like this:
 * <code><pre>
 *      public class MyClass implements FileDrop.Listener
 *      ...
 *      public void filesDropped( java.io.File[] files )
 *      {
 *          ...
 *      }   // end filesDropped
 *      ...
 * </pre></code>
 *
 * @since 1.1
 */
public interface FileDropListener {

    /**
     * This method is called when files have been successfully dropped.
     *
     * @param files An array of <tt>File</tt>s that were dropped.
     * @since 1.0
     */
    void filesDropped(java.io.File[] files);


}
