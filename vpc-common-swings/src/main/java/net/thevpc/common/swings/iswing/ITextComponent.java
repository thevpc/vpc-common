package net.thevpc.common.swings.iswing;

import java.awt.event.TextListener;
import java.util.EventListener;

public interface ITextComponent {
    public void setText(String t);

    /**
     * Returns the text that is presented by this text component.
     * By default, this is an empty string.
     *
     * @return the value of this <code>TextComponent</code>
     * @see java.awt.TextComponent#setText
     */
    public String getText();


    public String getSelectedText();

    /**
     * Indicates whether or not this text component is editable.
     *
     * @return <code>true</code> if this text component is
     *         editable; <code>false</code> otherwise.
     * @see java.awt.TextComponent#setEditable
     * @since JDK1.0
     */
    public boolean isEditable();

    /**
     * Sets the flag that determines whether or not this
     * text component is editable.
     * <p/>
     * If the flag is set to <code>true</code>, this text component
     * becomes user editable. If the flag is set to <code>false</code>,
     * the user cannot change the text of this text component.
     * By default, non-editable text components have a background color
     * of SystemColor.control.  This default can be overridden by
     * calling setBackground.
     *
     * @param b a flag indicating whether this text component
     *          is user editable.
     * @see java.awt.TextComponent#isEditable
     * @since JDK1.0
     */
    public void setEditable(boolean b);
    /**
     * Gets the background color of this text component.
     *
     * By default, non-editable text components have a background color
     * of SystemColor.control.  This default can be overridden by
     * calling setBackground.
     *
     * @return This text component's background color.
     *         If this text component does not have a background color,
     *         the background color of its parent is returned.
     * @see #setBackground(java.awt.Color)
     * @since JDK1.0
     */

    /**
     * Gets the start position of the selected text in
     * this text component.
     *
     * @return the start position of the selected text
     * @see java.awt.TextComponent#setSelectionStart
     * @see java.awt.TextComponent#getSelectionEnd
     */
    public int getSelectionStart();

    /**
     * Sets the selection start for this text component to
     * the specified position. The new start point is constrained
     * to be at or before the current selection end. It also
     * cannot be set to less than zero, the beginning of the
     * component's text.
     * If the caller supplies a value for <code>selectionStart</code>
     * that is out of bounds, the method enforces these constraints
     * silently, and without failure.
     *
     * @param selectionStart the start position of the
     *                       selected text
     * @see java.awt.TextComponent#getSelectionStart
     * @see java.awt.TextComponent#setSelectionEnd
     * @since JDK1.1
     */
    public void setSelectionStart(int selectionStart);

    /**
     * Gets the end position of the selected text in
     * this text component.
     *
     * @return the end position of the selected text
     * @see java.awt.TextComponent#setSelectionEnd
     * @see java.awt.TextComponent#getSelectionStart
     */
    public int getSelectionEnd();

    /**
     * Sets the selection end for this text component to
     * the specified position. The new end point is constrained
     * to be at or after the current selection start. It also
     * cannot be set beyond the end of the component's text.
     * If the caller supplies a value for <code>selectionEnd</code>
     * that is out of bounds, the method enforces these constraints
     * silently, and without failure.
     *
     * @param selectionEnd the end position of the
     *                     selected text
     * @see java.awt.TextComponent#getSelectionEnd
     * @see java.awt.TextComponent#setSelectionStart
     * @since JDK1.1
     */
    public void setSelectionEnd(int selectionEnd);

    /**
     * Selects the text between the specified start and end positions.
     * <p/>
     * This method sets the start and end positions of the
     * selected text, enforcing the restriction that the start position
     * must be greater than or equal to zero.  The end position must be
     * greater than or equal to the start position, and less than or
     * equal to the length of the text component's text.  The
     * character positions are indexed starting with zero.
     * The length of the selection is
     * <code>endPosition</code> - <code>startPosition</code>, so the
     * character at <code>endPosition</code> is not selected.
     * If the start and end positions of the selected text are equal,
     * all text is deselected.
     * <p/>
     * If the caller supplies values that are inconsistent or out of
     * bounds, the method enforces these constraints silently, and
     * without failure. Specifically, if the start position or end
     * position is greater than the length of the text, it is reset to
     * equal the text length. If the start position is less than zero,
     * it is reset to zero, and if the end position is less than the
     * start position, it is reset to the start position.
     *
     * @param selectionStart the zero-based index of the first
     *                       character (<code>char</code> value) to be selected
     * @param selectionEnd   the zero-based end position of the
     *                       text to be selected; the character (<code>char</code> value) at
     *                       <code>selectionEnd</code> is not selected
     * @see java.awt.TextComponent#setSelectionStart
     * @see java.awt.TextComponent#setSelectionEnd
     * @see java.awt.TextComponent#selectAll
     */
    public void select(int selectionStart, int selectionEnd);

    /**
     * Selects all the text in this text component.
     *
     * @see java.awt.TextComponent#select
     */
    public void selectAll();

    /**
     * Sets the position of the text insertion caret.
     * The caret position is constrained to be between 0
     * and the last character of the text, inclusive.
     * If the passed-in value is greater than this range,
     * the value is set to the last character (or 0 if
     * the <code>TextComponent</code> contains no text)
     * and no error is returned.  If the passed-in value is
     * less than 0, an <code>IllegalArgumentException</code>
     * is thrown.
     *
     * @param position the position of the text insertion caret
     * @throws IllegalArgumentException if <code>position</code>
     *                                  is less than zero
     * @since JDK1.1
     */
    public void setCaretPosition(int position);

    /**
     * Returns the position of the text insertion caret.
     * The caret position is constrained to be between 0
     * and the last character of the text, inclusive.
     * If the text or caret have not been set, the default
     * caret position is 0.
     *
     * @return the position of the text insertion caret
     * @see #setCaretPosition(int)
     * @since JDK1.1
     */
    public int getCaretPosition();

    /**
     * Adds the specified text event listener to receive text events
     * from this text component.
     * If <code>l</code> is <code>null</code>, no exception is
     * thrown and no action is performed.
     *
     * @param l the text event listener
     * @see #removeTextListener
     * @see #getTextListeners
     * @see java.awt.event.TextListener
     */
    public void addTextListener(TextListener l);

    /**
     * Removes the specified text event listener so that it no longer
     * receives text events from this text component
     * If <code>l</code> is <code>null</code>, no exception is
     * thrown and no action is performed.
     *
     * @param l the text listener
     * @see #addTextListener
     * @see #getTextListeners
     * @see java.awt.event.TextListener
     * @since JDK1.1
     */
    public void removeTextListener(TextListener l);

    /**
     * Returns an array of all the text listeners
     * registered on this text component.
     *
     * @return all of this text component's <code>TextListener</code>s
     *         or an empty array if no text
     *         listeners are currently registered
     * @see #addTextListener
     * @see #removeTextListener
     * @since 1.4
     */
    public TextListener[] getTextListeners();

    /**
     * Returns an array of all the objects currently registered
     * as <code><em>Foo</em>Listener</code>s
     * upon this <code>TextComponent</code>.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     * <p/>
     * <p/>
     * You can specify the <code>listenerType</code> argument
     * with a class literal, such as
     * <code><em>Foo</em>Listener.class</code>.
     * For example, you can query a
     * <code>TextComponent</code> <code>t</code>
     * for its text listeners with the following code:
     * <p/>
     * <pre>TextListener[] tls = (TextListener[])(t.getListeners(TextListener.class));</pre>
     * <p/>
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType the type of listeners requested; this parameter
     *                     should specify an interface that descends from
     *                     <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     *         <code><em>Foo</em>Listener</code>s on this text component,
     *         or an empty array if no such
     *         listeners have been added
     * @throws ClassCastException if <code>listenerType</code>
     *                            doesn't specify a class or interface that implements
     *                            <code>java.util.EventListener</code>
     * @see #getTextListeners
     * @since 1.3
     */
    public <T extends EventListener> T[] getListeners(Class<T> listenerType);


}
