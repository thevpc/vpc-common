package net.vpc.common.swings;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

/**
 * A container for images.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 4584 $
 * https://github.com/fracpete/jclipboardhelper/blob/master/src/main/java/com/github/fracpete/jclipboardhelper/TransferableImage.java
 */
public class TransferableImage
        implements Serializable, Transferable {

    /** for serialization. */
    private static final long serialVersionUID = 7613537409206432362L;

    /** the image to transfer. */
    protected BufferedImage m_Data;

    /**
     * Initializes the container.
     *
     * @param data	the string to transfer
     */
    public TransferableImage(BufferedImage data) {
        super();

        m_Data = data;
    }

    /**
     * Returns an array of DataFlavor objects indicating the flavors the data
     * can be provided in.  The array should be ordered according to preference
     * for providing the data (from most richly descriptive to least descriptive).
     *
     * @return 		an array of data flavors in which this data can be transferred
     */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    /**
     * Returns whether or not the specified data flavor is supported for
     * this object.
     *
     * @param flavor 	the requested flavor for the data
     * @return 		boolean indicating whether or not the data flavor is supported
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return (flavor.equals(DataFlavor.imageFlavor));
    }

    /**
     * Returns an object which represents the data to be transferred.  The class
     * of the object returned is defined by the representation class of the flavor.
     *
     * @param flavor 		the requested flavor for the data
     * @return			the transferred string
     * @throws IOException	if the data is no longer available
     *              		in the requested flavor.
     * @throws UnsupportedFlavorException 	if the requested data flavor is
     *              				not supported.
     * @see DataFlavor#getRepresentationClass
     */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (flavor.equals(DataFlavor.imageFlavor))
            return m_Data;
        else
            throw new UnsupportedFlavorException(flavor);
    }

    /**
     * Returns the underlying image.
     *
     * @return		the image
     */
    public BufferedImage getData() {
        return m_Data;
    }

    /**
     * Returns a string representation of the underlying image.
     *
     * @return		the string representation
     */
    public String toString() {
        return m_Data.toString();
    }
}