package net.thevpc.common.swing.file;

/**
 * This is the event that is passed to the
 * {@link FileDropListener#filesDropped filesDropped(...)} method in
 * your {@link FileDropListener} when files are dropped onto
 * a registered drop target.
 * <p>
 * <p>I'm releasing this code into the Public Domain. Enjoy.</p>
 *
 * @author Robert Harder
 * @author rob@iharder.net
 * @version 1.2
 */
public class FileDropEvent extends java.util.EventObject {

    private java.io.File[] files;

    /**
     * Constructs an {@link FileDropEvent} with the array
     * of files that were dropped and the
     * {@link FileDrop} that initiated the event.
     *
     * @param files The array of files that were dropped
     * %source The event source
     * @since 1.1
     */
    public FileDropEvent(java.io.File[] files, Object source) {
        super(source);
        this.files = files;
    }   // end constructor

    /**
     * Returns an array of files that were dropped on a
     * registered drop target.
     *
     * @return array of files that were dropped
     * @since 1.1
     */
    public java.io.File[] getFiles() {
        return files;
    }   // end getFiles

}   // end inner class FileDropEvent
