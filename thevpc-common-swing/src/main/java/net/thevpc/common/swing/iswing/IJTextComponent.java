package net.thevpc.common.swing.iswing;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface IJTextComponent extends IJComponent {
    /**
     * Adds a caret listener for notification of any changes
     * to the caret.
     *
     * @param listener the listener to be added
     * @see javax.swing.event.CaretEvent
     */
    public void addCaretListener(CaretListener listener);

    /**
     * Removes a caret listener.
     *
     * @param listener the listener to be removed
     * @see javax.swing.event.CaretEvent
     */
    public void removeCaretListener(CaretListener listener);

    /**
     * Returns an array of all the caret listeners
     * registered on this text component.
     *
     * @return all of this component's <code>CaretListener</code>s
     *         or an empty
     *         array if no caret listeners are currently registered
     * @see #addCaretListener
     * @see #removeCaretListener
     * @since 1.4
     */
    public CaretListener[] getCaretListeners();

    /**
     * Associates the editor with a text document.
     * The currently registered factory is used to build a view for
     * the document, which gets displayed by the editor after revalidation.
     * A PropertyChange event ("document") is propagated to each listener.
     *
     * @param doc the document to display/edit
     * bound: true
     * expert: true
     * @see #getDocument
     */
    public void setDocument(Document doc);

    /**
     * Fetches the model associated with the editor.  This is
     * primarily for the UI to get at the minimal amount of
     * state required to be a text editor.  Subclasses will
     * return the actual type of the model which will typically
     * be something that extends Document.
     *
     * @return the model
     */
    public Document getDocument();

    // Override of Component.setComponentOrientation
    public void setComponentOrientation(ComponentOrientation o);

    /**
     * Fetches the command list for the editor.  This is
     * the list of commands supported by the plugged-in UI
     * augmented by the collection of commands that the
     * editor itself supports.  These are useful for binding
     * to events, such as in a keymap.
     *
     * @return the command list
     */
    public Action[] getActions();

    /**
     * Sets margin space between the text component's border
     * and its text.  The text component's default <code>Border</code>
     * object will use this value to create the proper margin.
     * However, if a non-default border is set on the text component,
     * it is that <code>Border</code> object's responsibility to create the
     * appropriate margin space (else this property will effectively
     * be ignored).  This causes a redraw of the component.
     * A PropertyChange event ("margin") is sent to all listeners.
     *
     * @param m the space between the border and the text
     * bound: true
     */
    public void setMargin(Insets m);

    /**
     * Returns the margin between the text component's border and
     * its text.
     *
     * @return the margin
     */
    public Insets getMargin();

    /**
     * Sets the <code>NavigationFilter</code>. <code>NavigationFilter</code>
     * is used by <code>DefaultCaret</code> and the default cursor movement
     * actions as a way to restrict the cursor movement.
     *
     * @since 1.4
     */
    public void setNavigationFilter(NavigationFilter filter);

    /**
     * Returns the <code>NavigationFilter</code>. <code>NavigationFilter</code>
     * is used by <code>DefaultCaret</code> and the default cursor movement
     * actions as a way to restrict the cursor movement. A null return value
     * implies the cursor movement and selection should not be restricted.
     *
     * @return the NavigationFilter
     * @since 1.4
     */
    public NavigationFilter getNavigationFilter();

    /**
     * Fetches the caret that allows text-oriented navigation over
     * the view.
     *
     * @return the caret
     */
    public Caret getCaret();

    /**
     * Sets the caret to be used.  By default this will be set
     * by the UI that gets installed.  This can be changed to
     * a custom caret if desired.  Setting the caret results in a
     * PropertyChange event ("caret") being fired.
     *
     * @param c the caret
     * bound: true
     * expert: true
     * @see #getCaret
     */
    public void setCaret(Caret c);

    /**
     * Fetches the object responsible for making highlights.
     *
     * @return the highlighter
     */
    public Highlighter getHighlighter();

    /**
     * Sets the highlighter to be used.  By default this will be set
     * by the UI that gets installed.  This can be changed to
     * a custom highlighter if desired.  The highlighter can be set to
     * <code>null</code> to disable it.
     * A PropertyChange event ("highlighter") is fired
     * when a new highlighter is installed.
     *
     * @param h the highlighter
     * bound: true
     * expert: true
     * @see #getHighlighter
     */
    public void setHighlighter(Highlighter h);

    /**
     * Sets the keymap to use for binding events to
     * actions.  Setting to <code>null</code> effectively disables
     * keyboard input.
     * A PropertyChange event ("keymap") is fired when a new keymap
     * is installed.
     *
     * @param map the keymap
     * bound: true
     * @see #getKeymap
     */
    public void setKeymap(Keymap map);

    /**
     * Sets the <code>dragEnabled</code> property,
     * which must be <code>true</code> to enable
     * automatic drag handling (the first part of drag and drop)
     * on this component.
     * The <code>transferHandler</code> property needs to be set
     * to a non-<code>null</code> value for the drag to do
     * anything.  The default value of the <code>dragEnabled</code>
     * property
     * is <code>false</code>.
     * <p>
     * When automatic drag handling is enabled,
     * most look and feels begin a drag-and-drop operation
     * whenever the user presses the mouse button over a selection
     * and then moves the mouse a few pixels.
     * Setting this property to <code>true</code>
     * can therefore have a subtle effect on
     * how selections behave.
     * <p>
     * Some look and feels might not support automatic drag and drop;
     * they will ignore this property.  You can work around such
     * look and feels by modifying the component
     * to directly call the <code>exportAsDrag</code> method of a
     * <code>TransferHandler</code>.
     *
     * @param b the value to set the <code>dragEnabled</code> property to
     * @throws HeadlessException if
     *                           <code>b</code> is <code>true</code> and
     *                           <code>GraphicsEnvironment.isHeadless()</code>
     *                           returns <code>true</code>
     * bound: false
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #getDragEnabled
     * @see #setTransferHandler
     * @see TransferHandler
     * @since 1.4
     */
    public void setDragEnabled(boolean b);

    /**
     * Gets the <code>dragEnabled</code> property.
     *
     * @return the value of the <code>dragEnabled</code> property
     * @see #setDragEnabled
     * @since 1.4
     */
    public boolean getDragEnabled();


    /**
     * Fetches the keymap currently active in this text
     * component.
     *
     * @return the keymap
     */
    public Keymap getKeymap();


    /**
     * Fetches the current color used to render the
     * caret.
     *
     * @return the color
     */
    public Color getCaretColor();

    /**
     * Sets the current color used to render the caret.
     * Setting to <code>null</code> effectively restores the default color.
     * Setting the color results in a PropertyChange event ("caretColor")
     * being fired.
     *
     * @param c the color
     * bound: true
     * preferred: true
     * @see #getCaretColor
     */
    public void setCaretColor(Color c);

    /**
     * Fetches the current color used to render the
     * selection.
     *
     * @return the color
     */
    public Color getSelectionColor();

    /**
     * Sets the current color used to render the selection.
     * Setting the color to <code>null</code> is the same as setting
     * <code>Color.white</code>.  Setting the color results in a
     * PropertyChange event ("selectionColor").
     *
     * @param c the color
     * bound: true
     * preferred: true
     * @see #getSelectionColor
     */
    public void setSelectionColor(Color c);

    /**
     * Fetches the current color used to render the
     * selected text.
     *
     * @return the color
     */
    public Color getSelectedTextColor();

    /**
     * Sets the current color used to render the selected text.
     * Setting the color to <code>null</code> is the same as
     * <code>Color.black</code>. Setting the color results in a
     * PropertyChange event ("selectedTextColor") being fired.
     *
     * @param c the color
     * bound: true
     * preferred: true
     * @see #getSelectedTextColor
     */
    public void setSelectedTextColor(Color c);

    /**
     * Fetches the current color used to render the
     * selected text.
     *
     * @return the color
     */
    public Color getDisabledTextColor();

    /**
     * Sets the current color used to render the
     * disabled text.  Setting the color fires off a
     * PropertyChange event ("disabledTextColor").
     *
     * @param c the color
     * bound: true
     * preferred: true
     * @see #getDisabledTextColor
     */
    public void setDisabledTextColor(Color c);

    /**
     * Replaces the currently selected content with new content
     * represented by the given string.  If there is no selection
     * this amounts to an insert of the given text.  If there
     * is no replacement text this amounts to a removal of the
     * current selection.
     * <p>
     * This is the method that is used by the default implementation
     * of the action for inserting content that gets bound to the
     * keymap actions.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see
     * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
     * and Swing</A> for more information.
     *
     * @param content the content to replace the selection with
     */
    public void replaceSelection(String content);

    /**
     * Fetches a portion of the text represented by the
     * component.  Returns an empty string if length is 0.
     *
     * @param offs the offset &gt;= 0
     * @param len  the length &gt;= 0
     * @return the text
     * @throws BadLocationException if the offset or length are invalid
     */
    public String getText(int offs, int len) throws BadLocationException;

    /**
     * Converts the given location in the model to a place in
     * the view coordinate system.
     * The component must have a positive size for
     * this translation to be computed (i.e. layout cannot
     * be computed until the component has been sized).  The
     * component does not have to be visible or painted.
     *
     * @param pos the position &gt;= 0
     * @return the coordinates as a rectangle, with (r.x, r.y) as the location
     *         in the coordinate system, or null if the component does
     *         not yet have a positive size.
     * @throws BadLocationException if the given position does not
     *                              represent a valid location in the associated document
     * @see javax.swing.plaf.TextUI#modelToView
     */
    public Rectangle modelToView(int pos) throws BadLocationException;

    /**
     * Converts the given place in the view coordinate system
     * to the nearest representative location in the model.
     * The component must have a positive size for
     * this translation to be computed (i.e. layout cannot
     * be computed until the component has been sized).  The
     * component does not have to be visible or painted.
     *
     * @param pt the location in the view to translate
     * @return the offset &gt;= 0 from the start of the document,
     *         or -1 if the component does not yet have a positive
     *         size.
     * @see javax.swing.plaf.TextUI#viewToModel
     */
    public int viewToModel(Point pt);

    /**
     * Transfers the currently selected range in the associated
     * text model to the system clipboard, removing the contents
     * from the model.  The current selection is reset.  Does nothing
     * for <code>null</code> selections.
     *
     * @see java.awt.Toolkit#getSystemClipboard
     * @see java.awt.datatransfer.Clipboard
     */
    public void cut();

    /**
     * Transfers the currently selected range in the associated
     * text model to the system clipboard, leaving the contents
     * in the text model.  The current selection remains intact.
     * Does nothing for <code>null</code> selections.
     *
     * @see java.awt.Toolkit#getSystemClipboard
     * @see java.awt.datatransfer.Clipboard
     */
    public void copy();

    /**
     * Transfers the contents of the system clipboard into the
     * associated text model.  If there is a selection in the
     * associated view, it is replaced with the contents of the
     * clipboard.  If there is no selection, the clipboard contents
     * are inserted in front of the current insert position in
     * the associated view.  If the clipboard is empty, does nothing.
     *
     * @see #replaceSelection
     * @see java.awt.Toolkit#getSystemClipboard
     * @see java.awt.datatransfer.Clipboard
     */
    public void paste();


    /**
     * Moves the caret to a new position, leaving behind a mark
     * defined by the last time <code>setCaretPosition</code> was
     * called.  This forms a selection.
     * If the document is <code>null</code>, does nothing. The position
     * must be between 0 and the length of the component's text or else
     * an exception is thrown.
     *
     * @param pos the position
     * @throws IllegalArgumentException if the value supplied
     *                                  for <code>position</code> is less than zero or greater
     *                                  than the component's text length
     * @see #setCaretPosition
     */
    public void moveCaretPosition(int pos);


    /**
     * Sets the key accelerator that will cause the receiving text
     * component to get the focus.  The accelerator will be the
     * key combination of the <em>alt</em> key and the character
     * given (converted to upper case).  By default, there is no focus
     * accelerator key.  Any previous key accelerator setting will be
     * superseded.  A '\0' key setting will be registered, and has the
     * effect of turning off the focus accelerator.  When the new key
     * is set, a PropertyChange event (FOCUS_ACCELERATOR_KEY) will be fired.
     *
     * @param aKey the key
     * bound: true
     * @see #getFocusAccelerator
     */
    public void setFocusAccelerator(char aKey);

    /**
     * Returns the key accelerator that will cause the receiving
     * text component to get the focus.  Return '\0' if no focus
     * accelerator has been set.
     *
     * @return the key
     */
    public char getFocusAccelerator();

    /**
     * Initializes from a stream.  This creates a
     * model of the type appropriate for the component
     * and initializes the model from the stream.
     * By default this will load the model as plain
     * text.  Previous contents of the model are discarded.
     *
     * @param in   the stream to read from
     * @param desc an object describing the stream; this
     *             might be a string, a File, a URL, etc.  Some kinds
     *             of documents (such as html for example) might be
     *             able to make use of this information; if non-<code>null</code>,
     *             it is added as a property of the document
     * @throws java.io.IOException as thrown by the stream being
     *                             used to initialize
     * @see EditorKit#createDefaultDocument
     * @see #setDocument
     * @see PlainDocument
     */
    public void read(Reader in, Object desc) throws IOException;

    /**
     * Stores the contents of the model into the given
     * stream.  By default this will store the model as plain
     * text.
     *
     * @param out the output stream
     * @throws IOException on any I/O error
     */
    public void write(Writer out) throws IOException;

    public void removeNotify();

    // --- java.awt.TextComponent methods ------------------------

    /**
     * Sets the position of the text insertion caret for the
     * <code>TextComponent</code>.  Note that the caret tracks change,
     * so this may move if the underlying text of the component is changed.
     * If the document is <code>null</code>, does nothing. The position
     * must be between 0 and the length of the component's text or else
     * an exception is thrown.
     *
     * @param position the position
     * @throws IllegalArgumentException if the value supplied
     *                                  for <code>position</code> is less than zero or greater
     *                                  than the component's text length
     */
    public void setCaretPosition(int position);

    /**
     * Returns the position of the text insertion caret for the
     * text component.
     *
     * @return the position of the text insertion caret for the
     *         text component &gt;= 0
     */
    public int getCaretPosition();

    /**
     * Sets the text of this <code>TextComponent</code>
     * to the specified text.  If the text is <code>null</code>
     * or empty, has the effect of simply deleting the old text.
     * When text has been inserted, the resulting caret location
     * is determined by the implementation of the caret class.
     * <p>
     * This method is thread safe, although most Swing methods
     * are not. Please see
     * <A HREF="http://java.sun.com/products/jfc/swingdoc-archive/threads.html">Threads
     * and Swing</A> for more information.
     * <p>
     * Note that text is not a bound property, so no <code>PropertyChangeEvent
     * </code> is fired when it changes. To listen for changes to the text,
     * use <code>DocumentListener</code>.
     *
     * @param t the new text to be set
     * @see #getText
     * @see DefaultCaret
     */
    public void setText(String t);

    /**
     * Returns the text contained in this <code>TextComponent</code>.
     * If the underlying document is <code>null</code>,
     * will give a <code>NullPointerException</code>.
     * <p>
     * Note that text is not a bound property, so no <code>PropertyChangeEvent
     * </code> is fired when it changes. To listen for changes to the text,
     * use <code>DocumentListener</code>.
     *
     * @return the text
     * @throws NullPointerException if the document is <code>null</code>
     * @see #setText
     */
    public String getText();

    /**
     * Returns the selected text contained in this
     * <code>TextComponent</code>.  If the selection is
     * <code>null</code> or the document empty, returns <code>null</code>.
     *
     * @return the text
     * @throws IllegalArgumentException if the selection doesn't
     *                                  have a valid mapping into the document for some reason
     * @see #setText
     */
    public String getSelectedText();

    /**
     * Returns the boolean indicating whether this
     * <code>TextComponent</code> is editable or not.
     *
     * @return the boolean value
     * @see #setEditable
     */
    public boolean isEditable();

    /**
     * Sets the specified boolean to indicate whether or not this
     * <code>TextComponent</code> should be editable.
     * A PropertyChange event ("editable") is fired when the
     * state is changed.
     *
     * @param b the boolean to be set
     * bound: true
     * @see #isEditable
     */
    public void setEditable(boolean b);

    /**
     * Returns the selected text's start position.  Return 0 for an
     * empty document, or the value of dot if no selection.
     *
     * @return the start position &gt;= 0
     */
    public int getSelectionStart();

    /**
     * Sets the selection start to the specified position.  The new
     * starting point is constrained to be before or at the current
     * selection end.
     * <p>
     * This is available for backward compatibility to code
     * that called this method on <code>java.awt.TextComponent</code>.
     * This is implemented to forward to the <code>Caret</code>
     * implementation which is where the actual selection is maintained.
     *
     * @param selectionStart the start position of the text &gt;= 0
     */
    public void setSelectionStart(int selectionStart);

    /**
     * Returns the selected text's end position.  Return 0 if the document
     * is empty, or the value of dot if there is no selection.
     *
     * @return the end position &gt;= 0
     */
    public int getSelectionEnd();

    /**
     * Sets the selection end to the specified position.  The new
     * end point is constrained to be at or after the current
     * selection start.
     * <p>
     * This is available for backward compatibility to code
     * that called this method on <code>java.awt.TextComponent</code>.
     * This is implemented to forward to the <code>Caret</code>
     * implementation which is where the actual selection is maintained.
     *
     * @param selectionEnd the end position of the text &gt;= 0
     */
    public void setSelectionEnd(int selectionEnd);

    /**
     * Selects the text between the specified start and end positions.
     * <p>
     * This method sets the start and end positions of the
     * selected text, enforcing the restriction that the start position
     * must be greater than or equal to zero.  The end position must be
     * greater than or equal to the start position, and less than or
     * equal to the length of the text component's text.
     * <p>
     * If the caller supplies values that are inconsistent or out of
     * bounds, the method enforces these constraints silently, and
     * without failure. Specifically, if the start position or end
     * position is greater than the length of the text, it is reset to
     * equal the text length. If the start position is less than zero,
     * it is reset to zero, and if the end position is less than the
     * start position, it is reset to the start position.
     * <p>
     * This call is provided for backward compatibility.
     * It is routed to a call to <code>setCaretPosition</code>
     * followed by a call to <code>moveCaretPosition</code>.
     * The preferred way to manage selection is by calling
     * those methods directly.
     *
     * @param selectionStart the start position of the text
     * @param selectionEnd   the end position of the text
     * @see #setCaretPosition
     * @see #moveCaretPosition
     */
    public void select(int selectionStart, int selectionEnd);

    /**
     * Selects all the text in the <code>TextComponent</code>.
     * Does nothing on a <code>null</code> or empty document.
     */
    public void selectAll();
    // --- Tooltip Methods ---------------------------------------------

    /**
     * Returns the string to be used as the tooltip for <code>event</code>.
     * This will return one of:
     * <ol>
     * <li>If <code>setToolTipText</code> has been invoked with a
     * non-<code>null</code>
     * value, it will be returned, otherwise
     * <li>The value from invoking <code>getToolTipText</code> on
     * the UI will be returned.
     * </ol>
     * By default <code>JTextComponent</code> does not register
     * itself with the <code>ToolTipManager</code>.
     * This means that tooltips will NOT be shown from the
     * <code>TextUI</code> unless <code>registerComponent</code> has
     * been invoked on the <code>ToolTipManager</code>.
     *
     * @param event the event in question
     * @return the string to be used as the tooltip for <code>event</code>
     * @see javax.swing.JComponent#setToolTipText
     * @see javax.swing.plaf.TextUI#getToolTipText
     * @see javax.swing.ToolTipManager#registerComponent
     */
    public String getToolTipText(MouseEvent event);

    // --- Scrollable methods ---------------------------------------------

    /**
     * Returns the preferred size of the viewport for a view component.
     * This is implemented to do the default behavior of returning
     * the preferred size of the component.
     *
     * @return the <code>preferredSize</code> of a <code>JViewport</code>
     *         whose view is this <code>Scrollable</code>
     */
    public Dimension getPreferredScrollableViewportSize();

    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one new row
     * or column, depending on the value of orientation.  Ideally,
     * components should handle a partially exposed row or column by
     * returning the distance required to completely expose the item.
     * <p>
     * The default implementation of this is to simply return 10% of
     * the visible area.  Subclasses are likely to be able to provide
     * a much more reasonable value.
     *
     * @param visibleRect the view area visible within the viewport
     * @param orientation either <code>SwingConstants.VERTICAL</code> or
     *                    <code>SwingConstants.HORIZONTAL</code>
     * @param direction   less than zero to scroll up/left, greater than
     *                    zero for down/right
     * @return the "unit" increment for scrolling in the specified direction
     * @throws IllegalArgumentException for an invalid orientation
     * @see JScrollBar#setUnitIncrement
     */
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction);


    /**
     * Components that display logical rows or columns should compute
     * the scroll increment that will completely expose one block
     * of rows or columns, depending on the value of orientation.
     * <p>
     * The default implementation of this is to simply return the visible
     * area.  Subclasses will likely be able to provide a much more
     * reasonable value.
     *
     * @param visibleRect the view area visible within the viewport
     * @param orientation either <code>SwingConstants.VERTICAL</code> or
     *                    <code>SwingConstants.HORIZONTAL</code>
     * @param direction   less than zero to scroll up/left, greater than zero
     *                    for down/right
     * @return the "block" increment for scrolling in the specified direction
     * @throws IllegalArgumentException for an invalid orientation
     * @see JScrollBar#setBlockIncrement
     */
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction);


    /**
     * Returns true if a viewport should always force the width of this
     * <code>Scrollable</code> to match the width of the viewport.
     * For example a normal text view that supported line wrapping
     * would return true here, since it would be undesirable for
     * wrapped lines to disappear beyond the right
     * edge of the viewport.  Note that returning true for a
     * <code>Scrollable</code> whose ancestor is a <code>JScrollPane</code>
     * effectively disables horizontal scrolling.
     * <p>
     * Scrolling containers, like <code>JViewport</code>,
     * will use this method each time they are validated.
     *
     * @return true if a viewport should force the <code>Scrollable</code>s
     *         width to match its own
     */
    public boolean getScrollableTracksViewportWidth();

    /**
     * Returns true if a viewport should always force the height of this
     * <code>Scrollable</code> to match the height of the viewport.
     * For example a columnar text view that flowed text in left to
     * right columns could effectively disable vertical scrolling by
     * returning true here.
     * <p>
     * Scrolling containers, like <code>JViewport</code>,
     * will use this method each time they are validated.
     *
     * @return true if a viewport should force the Scrollables height
     *         to match its own
     */
    public boolean getScrollableTracksViewportHeight();

/////////////////
// Accessibility support
////////////////


    /**
     * Gets the <code>AccessibleContext</code> associated with this
     * <code>JTextComponent</code>. For text components,
     * the <code>AccessibleContext</code> takes the form of an
     * <code>AccessibleJTextComponent</code>.
     * A new <code>AccessibleJTextComponent</code> instance
     * is created if necessary.
     *
     * @return an <code>AccessibleJTextComponent</code> that serves as the
     *         <code>AccessibleContext</code> of this
     *         <code>JTextComponent</code>
     */
    public AccessibleContext getAccessibleContext();

    /**
     * This class implements accessibility support for the
     * <code>JTextComponent</code> class.  It provides an implementation of
     * the Java Accessibility API appropriate to menu user-interface elements.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases. The current serialization support is
     * appropriate for short term storage or RMI between applications running
     * the same version of Swing.  As of 1.4, support for long term storage
     * of all JavaBeans<sup><font size="-2">TM</font></sup>
     * has been added to the <code>java.beans</code> package.
     * Please see {@link java.beans.XMLEncoder}.
     */
}
