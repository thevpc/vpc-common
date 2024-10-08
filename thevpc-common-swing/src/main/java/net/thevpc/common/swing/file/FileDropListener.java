package net.thevpc.common.swing.file;

/**
 * Implement this inner interface to listen for when files are dropped. For example
 * your class declaration may begin like this:
 * <code>
 *      public class MyClass implements FileDrop.Listener
 *      ...
 *      public void filesDropped( java.io.File[] files )
 *      {
 *          ...
 *      }   // end filesDropped
 *      ...
 * </code>
 *
 * @since 1.1
 */
public interface FileDropListener {

    /**
     * This method is called when files have been successfully dropped.
     *
     * @param files An array of <strong>File</strong>s that were dropped.
     * @since 1.0
     */
    void filesDropped(java.io.File[] files);


}
