package net.vpc.common.strings;

public final class StringBuilder2 implements java.io.Serializable, CharSequence, Appendable {

    /**
     * use serialVersionUID for interoperability
     */
    static final long serialVersionUID = 1L;

    private StringBuilder sb;

    /**
     * This no-arg constructor is necessary for serialization of subclasses.
     */
    public StringBuilder2() {
        this(16);
    }

    /**
     * Creates an AbstractStringBuilder of the specified capacity.
     */
    public StringBuilder2(int capacity) {
        sb = new StringBuilder(capacity);
    }


    /**
     * Constructs a string builder initialized to the contents of the
     * specified string. The initial capacity of the string builder is
     * {@code 16} plus the length of the string argument.
     *
     * @param str the initial contents of the buffer.
     */
    public StringBuilder2(String str) {
        this(str.length() + 16);
        append(str);
    }

    /**
     * Constructs a string builder that contains the same characters
     * as the specified {@code CharSequence}. The initial capacity of
     * the string builder is {@code 16} plus the length of the
     * {@code CharSequence} argument.
     *
     * @param seq the sequence to copy.
     */
    public StringBuilder2(CharSequence seq) {
        this(seq.length() + 16);
        append(seq);
    }


    public String toString() {
        return sb.toString();
    }

    /**
     * Save the state of the {@code StringBuilder2} instance to a stream
     * (that is, serialize it).
     *
     * @serialData the number of characters currently stored in the string
     * builder ({@code int}), followed by the characters in the
     * string builder ({@code char[]}).   The length of the
     * {@code char} array may be greater than the number of
     * characters currently stored in the string builder, in which
     * case extra characters are ignored.
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        s.defaultWriteObject();
        s.writeObject(sb);
    }

    public char[] toCharArray() {
        char[] dst = new char[length()];
        getChars(0, length(), dst, 0);
        return dst;
    }


    /**
     * readObject is called to restore the state of the StringBuffer from
     * a stream.
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        s.defaultReadObject();
        sb = (StringBuilder) s.readObject();
    }


    /**
     * Returns the length (character count).
     *
     * @return the length of the sequence of characters currently
     * represented by this object
     */

    public int length() {
        return sb.length();
    }

    /**
     * Returns the current capacity. The capacity is the amount of storage
     * available for newly inserted characters, beyond which an allocation
     * will occur.
     *
     * @return the current capacity
     */
    public int capacity() {
        return sb.capacity();
    }

    /**
     * Ensures that the capacity is at least equal to the specified minimum.
     * If the current capacity is less than the argument, then a new internal
     * array is allocated with greater capacity. The new capacity is the
     * larger of:
     * <ul>
     * <li>The {@code minimumCapacity} argument.
     * <li>Twice the old capacity, plus {@code 2}.
     * </ul>
     * If the {@code minimumCapacity} argument is nonpositive, this
     * method takes no action and simply returns.
     * Note that subsequent operations on this object can reduce the
     * actual capacity below that requested here.
     *
     * @param minimumCapacity the minimum desired capacity.
     */
    public void ensureCapacity(int minimumCapacity) {
        sb.ensureCapacity(minimumCapacity);
    }


    /**
     * Attempts to reduce storage used for the character sequence.
     * If the buffer is larger than necessary to hold its current sequence of
     * characters, then it may be resized to become more space efficient.
     * Calling this method may, but is not required to, affect the value
     * returned by a subsequent call to the {@link #capacity()} method.
     */
    public void trimToSize() {
        sb.trimToSize();
    }

    /**
     * Sets the length of the character sequence.
     * The sequence is changed to a new character sequence
     * whose length is specified by the argument. For every nonnegative
     * index <i>k</i> less than {@code newLength}, the character at
     * index <i>k</i> in the new character sequence is the same as the
     * character at index <i>k</i> in the old sequence if <i>k</i> is less
     * than the length of the old character sequence; otherwise, it is the
     * null character {@code '\u005Cu0000'}.
     * <p>
     * In other words, if the {@code newLength} argument is less than
     * the current length, the length is changed to the specified length.
     * <p>
     * If the {@code newLength} argument is greater than or equal
     * to the current length, sufficient null characters
     * ({@code '\u005Cu0000'}) are appended so that
     * length becomes the {@code newLength} argument.
     * <p>
     * The {@code newLength} argument must be greater than or equal
     * to {@code 0}.
     *
     * @param newLength the new length
     * @throws IndexOutOfBoundsException if the
     *                                   {@code newLength} argument is negative.
     */
    public void setLength(int newLength) {
        sb.setLength(newLength);
    }

    /**
     * Returns the {@code char} value in this sequence at the specified index.
     * The first {@code char} value is at index {@code 0}, the next at index
     * {@code 1}, and so on, as in array indexing.
     * <p>
     * The index argument must be greater than or equal to
     * {@code 0}, and less than the length of this sequence.
     * <p>
     * <p>If the {@code char} value specified by the index is a
     * <a href="Character.html#unicode">surrogate</a>, the surrogate
     * value is returned.
     *
     * @param index the index of the desired {@code char} value.
     * @return the {@code char} value at the specified index.
     * @throws IndexOutOfBoundsException if {@code index} is
     *                                   negative or greater than or equal to {@code length()}.
     */

    public char charAt(int index) {
        return sb.charAt(index);
    }

    /**
     * Returns the character (Unicode code point) at the specified
     * index. The index refers to {@code char} values
     * (Unicode code units) and ranges from {@code 0} to
     * {@link #length()}{@code  - 1}.
     * <p>
     * <p> If the {@code char} value specified at the given index
     * is in the high-surrogate range, the following index is less
     * than the length of this sequence, and the
     * {@code char} value at the following index is in the
     * low-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the {@code char} value at the given index is returned.
     *
     * @param index the index to the {@code char} values
     * @return the code point value of the character at the
     * {@code index}
     * @throws IndexOutOfBoundsException if the {@code index}
     *                                   argument is negative or not less than the length of this
     *                                   sequence.
     */
    public int codePointAt(int index) {
        return sb.codePointAt(index);
    }

    /**
     * Returns the character (Unicode code point) before the specified
     * index. The index refers to {@code char} values
     * (Unicode code units) and ranges from {@code 1} to {@link
     * #length()}.
     * <p>
     * <p> If the {@code char} value at {@code (index - 1)}
     * is in the low-surrogate range, {@code (index - 2)} is not
     * negative, and the {@code char} value at {@code (index -
     * 2)} is in the high-surrogate range, then the
     * supplementary code point value of the surrogate pair is
     * returned. If the {@code char} value at {@code index -
     * 1} is an unpaired low-surrogate or a high-surrogate, the
     * surrogate value is returned.
     *
     * @param index the index following the code point that should be returned
     * @return the Unicode code point value before the given index.
     * @throws IndexOutOfBoundsException if the {@code index}
     *                                   argument is less than 1 or greater than the length
     *                                   of this sequence.
     */
    public int codePointBefore(int index) {
        return sb.codePointBefore(index);
    }

    /**
     * Returns the number of Unicode code points in the specified text
     * range of this sequence. The text range begins at the specified
     * {@code beginIndex} and extends to the {@code char} at
     * index {@code endIndex - 1}. Thus the length (in
     * {@code char}s) of the text range is
     * {@code endIndex-beginIndex}. Unpaired surrogates within
     * this sequence count as one code point each.
     *
     * @param beginIndex the index to the first {@code char} of
     *                   the text range.
     * @param endIndex   the index after the last {@code char} of
     *                   the text range.
     * @return the number of Unicode code points in the specified text
     * range
     * @throws IndexOutOfBoundsException if the
     *                                   {@code beginIndex} is negative, or {@code endIndex}
     *                                   is larger than the length of this sequence, or
     *                                   {@code beginIndex} is larger than {@code endIndex}.
     */
    public int codePointCount(int beginIndex, int endIndex) {
        return sb.codePointCount(beginIndex, endIndex);
    }

    /**
     * Returns the index within this sequence that is offset from the
     * given {@code index} by {@code codePointOffset} code
     * points. Unpaired surrogates within the text range given by
     * {@code index} and {@code codePointOffset} count as
     * one code point each.
     *
     * @param index           the index to be offset
     * @param codePointOffset the offset in code points
     * @return the index within this sequence
     * @throws IndexOutOfBoundsException if {@code index}
     *                                   is negative or larger then the length of this sequence,
     *                                   or if {@code codePointOffset} is positive and the subsequence
     *                                   starting with {@code index} has fewer than
     *                                   {@code codePointOffset} code points,
     *                                   or if {@code codePointOffset} is negative and the subsequence
     *                                   before {@code index} has fewer than the absolute value of
     *                                   {@code codePointOffset} code points.
     */
    public int offsetByCodePoints(int index, int codePointOffset) {
        return sb.offsetByCodePoints(index, codePointOffset);
    }

    /**
     * Characters are copied from this sequence into the
     * destination character array {@code dst}. The first character to
     * be copied is at index {@code srcBegin}; the last character to
     * be copied is at index {@code srcEnd-1}. The total number of
     * characters to be copied is {@code srcEnd-srcBegin}. The
     * characters are copied into the subarray of {@code dst} starting
     * at index {@code dstBegin} and ending at index:
     * <pre>{@code
     * dstbegin + (srcEnd-srcBegin) - 1
     * }</pre>
     *
     * @param srcBegin start copying at this offset.
     * @param srcEnd   stop copying at this offset.
     * @param dst      the array to copy the data into.
     * @param dstBegin offset into {@code dst}.
     * @throws IndexOutOfBoundsException if any of the following is true:
     *                                   <ul>
     *                                   <li>{@code srcBegin} is negative
     *                                   <li>{@code dstBegin} is negative
     *                                   <li>the {@code srcBegin} argument is greater than
     *                                   the {@code srcEnd} argument.
     *                                   <li>{@code srcEnd} is greater than
     *                                   {@code this.length()}.
     *                                   <li>{@code dstBegin+srcEnd-srcBegin} is greater than
     *                                   {@code dst.length}
     *                                   </ul>
     */
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
        sb.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    /**
     * The character at the specified index is set to {@code ch}. This
     * sequence is altered to represent a new character sequence that is
     * identical to the old character sequence, except that it contains the
     * character {@code ch} at position {@code index}.
     * <p>
     * The index argument must be greater than or equal to
     * {@code 0}, and less than the length of this sequence.
     *
     * @param index the index of the character to modify.
     * @param ch    the new character.
     * @throws IndexOutOfBoundsException if {@code index} is
     *                                   negative or greater than or equal to {@code length()}.
     */
    public void setCharAt(int index, char ch) {
        sb.setCharAt(index, ch);
    }

    /**
     * Appends the string representation of the {@code Object} argument.
     * <p>
     * The overall effect is exactly as if the argument were converted
     * to a string by the method {@link String#valueOf(Object)},
     * and the characters of that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param obj an {@code Object}.
     * @return a reference to this object.
     */
    public StringBuilder2 append(Object obj) {
        sb.append(obj);
        return this;
    }

    /**
     * Appends the specified string to this character sequence.
     * <p>
     * The characters of the {@code String} argument are appended, in
     * order, increasing the length of this sequence by the length of the
     * argument. If {@code str} is {@code null}, then the four
     * characters {@code "null"} are appended.
     * <p>
     * Let <i>n</i> be the length of this character sequence just prior to
     * execution of the {@code append} method. Then the character at
     * index <i>k</i> in the new character sequence is equal to the character
     * at index <i>k</i> in the old character sequence, if <i>k</i> is less
     * than <i>n</i>; otherwise, it is equal to the character at index
     * <i>k-n</i> in the argument {@code str}.
     *
     * @param str a string.
     * @return a reference to this object.
     */
    public StringBuilder2 append(String str) {
        sb.append(str);
        return this;
    }

    /**
     * appends separator and than str.
     * If this stringbuilder ends with the separator, it wont be appended (the separator)
     * If this str starts with the separator, it wont be appended either (the separator)
     * @param separator separator
     * @param str str
     * @return {@code this} instance
     */
    public StringBuilder2 appendWithSeparator(String separator,String str) {
        if(!endsWith(separator) && !str.startsWith(separator)){
            append(separator);
        }
        append(str);
        return this;
    }

    // Documentation in subclasses because of synchro difference
    public StringBuilder2 append(StringBuffer sb) {
        sb.append(sb);
        return this;
    }

    public StringBuilder2 append(CharSequence s) {
        sb.append(s);
        return this;
    }

    public StringBuilder2 append(StringBuilder2 s) {
        sb.append(s.sb);
        return this;
    }

    /**
     * Appends a subsequence of the specified {@code CharSequence} to this
     * sequence.
     * <p>
     * Characters of the argument {@code s}, starting at
     * index {@code start}, are appended, in order, to the contents of
     * this sequence up to the (exclusive) index {@code end}. The length
     * of this sequence is increased by the value of {@code end - start}.
     * <p>
     * Let <i>n</i> be the length of this character sequence just prior to
     * execution of the {@code append} method. Then the character at
     * index <i>k</i> in this character sequence becomes equal to the
     * character at index <i>k</i> in this sequence, if <i>k</i> is less than
     * <i>n</i>; otherwise, it is equal to the character at index
     * <i>k+start-n</i> in the argument {@code s}.
     * <p>
     * If {@code s} is {@code null}, then this method appends
     * characters as if the s parameter was a sequence containing the four
     * characters {@code "null"}.
     *
     * @param s     the sequence to append.
     * @param start the starting index of the subsequence to be appended.
     * @param end   the end index of the subsequence to be appended.
     * @return a reference to this object.
     * @throws IndexOutOfBoundsException if
     *                                   {@code start} is negative, or
     *                                   {@code start} is greater than {@code end} or
     *                                   {@code end} is greater than {@code s.length()}
     */

    public StringBuilder2 append(CharSequence s, int start, int end) {
        sb.append(s, start, end);
        return this;
    }

    /**
     * Appends the string representation of the {@code char} array
     * argument to this sequence.
     * <p>
     * The characters of the array argument are appended, in order, to
     * the contents of this sequence. The length of this sequence
     * increases by the length of the argument.
     * <p>
     * The overall effect is exactly as if the argument were converted
     * to a string by the method {@link String#valueOf(char[])},
     * and the characters of that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param str the characters to be appended.
     * @return a reference to this object.
     */
    public StringBuilder2 append(char[] str) {
        sb.append(str);
        return this;
    }

    /**
     * Appends the string representation of a subarray of the
     * {@code char} array argument to this sequence.
     * <p>
     * Characters of the {@code char} array {@code str}, starting at
     * index {@code offset}, are appended, in order, to the contents
     * of this sequence. The length of this sequence increases
     * by the value of {@code len}.
     * <p>
     * The overall effect is exactly as if the arguments were converted
     * to a string by the method {@link String#valueOf(char[], int, int)},
     * and the characters of that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param str    the characters to be appended.
     * @param offset the index of the first {@code char} to append.
     * @param len    the number of {@code char}s to append.
     * @return a reference to this object.
     * @throws IndexOutOfBoundsException if {@code offset < 0} or {@code len < 0}
     *                                   or {@code offset+len > str.length}
     */
    public StringBuilder2 append(char str[], int offset, int len) {
        sb.append(str, offset, len);
        return this;
    }

    /**
     * Appends the string representation of the {@code boolean}
     * argument to the sequence.
     * <p>
     * The overall effect is exactly as if the argument were converted
     * to a string by the method {@link String#valueOf(boolean)},
     * and the characters of that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param b a {@code boolean}.
     * @return a reference to this object.
     */
    public StringBuilder2 append(boolean b) {
        sb.append(b);
        return this;
    }

    /**
     * Appends the string representation of the {@code char}
     * argument to this sequence.
     * <p>
     * The argument is appended to the contents of this sequence.
     * The length of this sequence increases by {@code 1}.
     * <p>
     * The overall effect is exactly as if the argument were converted
     * to a string by the method {@link String#valueOf(char)},
     * and the character in that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param c a {@code char}.
     * @return a reference to this object.
     */

    public StringBuilder2 append(char c) {
        sb.append(c);
        return this;
    }

    /**
     * Appends the string representation of the {@code int}
     * argument to this sequence.
     * <p>
     * The overall effect is exactly as if the argument were converted
     * to a string by the method {@link String#valueOf(int)},
     * and the characters of that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param i an {@code int}.
     * @return a reference to this object.
     */
    public StringBuilder2 append(int i) {
        sb.append(i);
        return this;
    }

    /**
     * Appends the string representation of the {@code long}
     * argument to this sequence.
     * <p>
     * The overall effect is exactly as if the argument were converted
     * to a string by the method {@link String#valueOf(long)},
     * and the characters of that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param l a {@code long}.
     * @return a reference to this object.
     */
    public StringBuilder2 append(long l) {
        sb.append(l);
        return this;
    }

    /**
     * Appends the string representation of the {@code float}
     * argument to this sequence.
     * <p>
     * The overall effect is exactly as if the argument were converted
     * to a string by the method {@link String#valueOf(float)},
     * and the characters of that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param f a {@code float}.
     * @return a reference to this object.
     */
    public StringBuilder2 append(float f) {
        sb.append(f);
        return this;
    }

    /**
     * Appends the string representation of the {@code double}
     * argument to this sequence.
     * <p>
     * The overall effect is exactly as if the argument were converted
     * to a string by the method {@link String#valueOf(double)},
     * and the characters of that string were then
     * {@link #append(String) appended} to this character sequence.
     *
     * @param d a {@code double}.
     * @return a reference to this object.
     */
    public StringBuilder2 append(double d) {
        sb.append(d);
        return this;
    }

    /**
     * Removes the characters in a substring of this sequence.
     * The substring begins at the specified {@code start} and extends to
     * the character at index {@code end - 1} or to the end of the
     * sequence if no such character exists. If
     * {@code start} is equal to {@code end}, no changes are made.
     *
     * @param start The beginning index, inclusive.
     * @param end   The ending index, exclusive.
     * @return This object.
     * @throws StringIndexOutOfBoundsException if {@code start}
     *                                         is negative, greater than {@code length()}, or
     *                                         greater than {@code end}.
     */
    public StringBuilder2 delete(int start, int end) {
        sb.delete(start, end);
        return this;
    }

    public StringBuilder2 delete() {
        sb.delete(0, length());
        return this;
    }

    /**
     * Appends the string representation of the {@code codePoint}
     * argument to this sequence.
     * <p>
     * <p> The argument is appended to the contents of this sequence.
     * The length of this sequence increases by
     * {@link Character#charCount(int) Character.charCount(codePoint)}.
     * <p>
     * <p> The overall effect is exactly as if the argument were
     * converted to a {@code char} array by the method
     * {@link Character#toChars(int)} and the character in that array
     * were then {@link #append(char[]) appended} to this character
     * sequence.
     *
     * @param codePoint a Unicode code point
     * @return a reference to this object.
     * @throws IllegalArgumentException if the specified
     *                                  {@code codePoint} isn't a valid Unicode code point
     */
    public StringBuilder2 appendCodePoint(int codePoint) {
        sb.appendCodePoint(codePoint);
        return this;
    }

    /**
     * Removes the {@code char} at the specified position in this
     * sequence. This sequence is shortened by one {@code char}.
     * <p>
     * <p>Note: If the character at the given index is a supplementary
     * character, this method does not remove the entire character. If
     * correct handling of supplementary characters is required,
     * determine the number of {@code char}s to remove by calling
     * {@code Character.charCount(thisSequence.codePointAt(index))},
     * where {@code thisSequence} is this sequence.
     *
     * @param index Index of {@code char} to remove
     * @return This object.
     * @throws StringIndexOutOfBoundsException if the {@code index}
     *                                         is negative or greater than or equal to
     *                                         {@code length()}.
     */
    public StringBuilder2 deleteCharAt(int index) {
        sb.deleteCharAt(index);
        return this;
    }

    /**
     * Replaces the characters in a substring of this sequence
     * with characters in the specified {@code String}. The substring
     * begins at the specified {@code start} and extends to the character
     * at index {@code end - 1} or to the end of the
     * sequence if no such character exists. First the
     * characters in the substring are removed and then the specified
     * {@code String} is inserted at {@code start}. (This
     * sequence will be lengthened to accommodate the
     * specified String if necessary.)
     *
     * @param start The beginning index, inclusive.
     * @param end   The ending index, exclusive.
     * @param str   String that will replace previous contents.
     * @return This object.
     * @throws StringIndexOutOfBoundsException if {@code start}
     *                                         is negative, greater than {@code length()}, or
     *                                         greater than {@code end}.
     */
    public StringBuilder2 replace(int start, int end, String str) {
        sb.replace(start, end, str);
        return this;
    }

    /**
     * Returns a new {@code String} that contains a subsequence of
     * characters currently contained in this character sequence. The
     * substring begins at the specified index and extends to the end of
     * this sequence.
     *
     * @param start The beginning index, inclusive.
     * @return The new string.
     * @throws StringIndexOutOfBoundsException if {@code start} is
     *                                         less than zero, or greater than the length of this object.
     */
    public String substring(int start) {
        return sb.substring(start);
    }

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     * <p>
     * <p> An invocation of this method of the form
     * <p>
     * <pre>{@code
     * sb.subSequence(begin,&nbsp;end)}</pre>
     * <p>
     * behaves in exactly the same way as the invocation
     * <p>
     * <pre>{@code
     * sb.substring(begin,&nbsp;end)}</pre>
     * <p>
     * This method is provided so that this class can
     * implement the {@link CharSequence} interface.
     *
     * @param start the start index, inclusive.
     * @param end   the end index, exclusive.
     * @return the specified subsequence.
     * @throws IndexOutOfBoundsException if {@code start} or {@code end} are negative,
     *                                   if {@code end} is greater than {@code length()},
     *                                   or if {@code start} is greater than {@code end}
     * @spec JSR-51
     */

    public CharSequence subSequence(int start, int end) {
        return sb.subSequence(start, end);
    }

    /**
     * Returns a new {@code String} that contains a subsequence of
     * characters currently contained in this sequence. The
     * substring begins at the specified {@code start} and
     * extends to the character at index {@code end - 1}.
     *
     * @param start The beginning index, inclusive.
     * @param end   The ending index, exclusive.
     * @return The new string.
     * @throws StringIndexOutOfBoundsException if {@code start}
     *                                         or {@code end} are negative or greater than
     *                                         {@code length()}, or {@code start} is
     *                                         greater than {@code end}.
     */
    public String substring(int start, int end) {
        return sb.substring(start, end);
    }

    /**
     * Inserts the string representation of a subarray of the {@code str}
     * array argument into this sequence. The subarray begins at the
     * specified {@code offset} and extends {@code len} {@code char}s.
     * The characters of the subarray are inserted into this sequence at
     * the position indicated by {@code index}. The length of this
     * sequence increases by {@code len} {@code char}s.
     *
     * @param index  position at which to insert subarray.
     * @param str    A {@code char} array.
     * @param offset the index of the first {@code char} in subarray to
     *               be inserted.
     * @param len    the number of {@code char}s in the subarray to
     *               be inserted.
     * @return This object
     * @throws StringIndexOutOfBoundsException if {@code index}
     *                                         is negative or greater than {@code length()}, or
     *                                         {@code offset} or {@code len} are negative, or
     *                                         {@code (offset+len)} is greater than
     *                                         {@code str.length}.
     */
    public StringBuilder2 insert(int index, char[] str, int offset,
                                 int len) {
        sb.insert(index, str, offset, len);
        return this;
    }

    /**
     * Inserts the string representation of the {@code Object}
     * argument into this character sequence.
     * <p>
     * The overall effect is exactly as if the second argument were
     * converted to a string by the method {@link String#valueOf(Object)},
     * and the characters of that string were then
     * {@link #insert(int, String) inserted} into this character
     * sequence at the indicated offset.
     * <p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param obj    an {@code Object}.
     * @return a reference to this object.
     * @throws StringIndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, Object obj) {
        sb.insert(offset, obj);
        return this;
    }

    /**
     * Inserts the string into this character sequence.
     * <p>
     * The characters of the {@code String} argument are inserted, in
     * order, into this sequence at the indicated offset, moving up any
     * characters originally above that position and increasing the length
     * of this sequence by the length of the argument. If
     * {@code str} is {@code null}, then the four characters
     * {@code "null"} are inserted into this sequence.
     * <p>
     * The character at index <i>k</i> in the new character sequence is
     * equal to:
     * <ul>
     * <li>the character at index <i>k</i> in the old character sequence, if
     * <i>k</i> is less than {@code offset}
     * <li>the character at index <i>k</i>{@code -offset} in the
     * argument {@code str}, if <i>k</i> is not less than
     * {@code offset} but is less than {@code offset+str.length()}
     * <li>the character at index <i>k</i>{@code -str.length()} in the
     * old character sequence, if <i>k</i> is not less than
     * {@code offset+str.length()}
     * </ul><p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param str    a string.
     * @return a reference to this object.
     * @throws StringIndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, String str) {
        sb.insert(offset, str);
        return this;
    }

    /**
     * Inserts the string representation of the {@code char} array
     * argument into this sequence.
     * <p>
     * The characters of the array argument are inserted into the
     * contents of this sequence at the position indicated by
     * {@code offset}. The length of this sequence increases by
     * the length of the argument.
     * <p>
     * The overall effect is exactly as if the second argument were
     * converted to a string by the method {@link String#valueOf(char[])},
     * and the characters of that string were then
     * {@link #insert(int, String) inserted} into this character
     * sequence at the indicated offset.
     * <p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param str    a character array.
     * @return a reference to this object.
     * @throws StringIndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, char[] str) {
        sb.insert(offset, str);
        return this;
    }

    /**
     * Inserts the specified {@code CharSequence} into this sequence.
     * <p>
     * The characters of the {@code CharSequence} argument are inserted,
     * in order, into this sequence at the indicated offset, moving up
     * any characters originally above that position and increasing the length
     * of this sequence by the length of the argument s.
     * <p>
     * The result of this method is exactly the same as if it were an
     * invocation of this object's
     * {@link #insert(int, CharSequence, int, int) insert}(dstOffset, s, 0, s.length())
     * method.
     * <p>
     * <p>If {@code s} is {@code null}, then the four characters
     * {@code "null"} are inserted into this sequence.
     *
     * @param dstOffset the offset.
     * @param s         the sequence to be inserted
     * @return a reference to this object.
     * @throws IndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int dstOffset, CharSequence s) {
        sb.insert(dstOffset, s);
        return this;
    }

    /**
     * Inserts a subsequence of the specified {@code CharSequence} into
     * this sequence.
     * <p>
     * The subsequence of the argument {@code s} specified by
     * {@code start} and {@code end} are inserted,
     * in order, into this sequence at the specified destination offset, moving
     * up any characters originally above that position. The length of this
     * sequence is increased by {@code end - start}.
     * <p>
     * The character at index <i>k</i> in this sequence becomes equal to:
     * <ul>
     * <li>the character at index <i>k</i> in this sequence, if
     * <i>k</i> is less than {@code dstOffset}
     * <li>the character at index <i>k</i>{@code +start-dstOffset} in
     * the argument {@code s}, if <i>k</i> is greater than or equal to
     * {@code dstOffset} but is less than {@code dstOffset+end-start}
     * <li>the character at index <i>k</i>{@code -(end-start)} in this
     * sequence, if <i>k</i> is greater than or equal to
     * {@code dstOffset+end-start}
     * </ul><p>
     * The {@code dstOffset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     * <p>The start argument must be nonnegative, and not greater than
     * {@code end}.
     * <p>The end argument must be greater than or equal to
     * {@code start}, and less than or equal to the length of s.
     * <p>
     * <p>If {@code s} is {@code null}, then this method inserts
     * characters as if the s parameter was a sequence containing the four
     * characters {@code "null"}.
     *
     * @param dstOffset the offset in this sequence.
     * @param s         the sequence to be inserted.
     * @param start     the starting index of the subsequence to be inserted.
     * @param end       the end index of the subsequence to be inserted.
     * @return a reference to this object.
     * @throws IndexOutOfBoundsException if {@code dstOffset}
     *                                   is negative or greater than {@code this.length()}, or
     *                                   {@code start} or {@code end} are negative, or
     *                                   {@code start} is greater than {@code end} or
     *                                   {@code end} is greater than {@code s.length()}
     */
    public StringBuilder2 insert(int dstOffset, CharSequence s,
                                 int start, int end) {
        sb.insert(dstOffset, s, start, end);
        return this;
    }

    /**
     * Inserts the string representation of the {@code boolean}
     * argument into this sequence.
     * <p>
     * The overall effect is exactly as if the second argument were
     * converted to a string by the method {@link String#valueOf(boolean)},
     * and the characters of that string were then
     * {@link #insert(int, String) inserted} into this character
     * sequence at the indicated offset.
     * <p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param b      a {@code boolean}.
     * @return a reference to this object.
     * @throws StringIndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, boolean b) {
        sb.insert(offset, b);
        return this;
    }

    /**
     * Inserts the string representation of the {@code char}
     * argument into this sequence.
     * <p>
     * The overall effect is exactly as if the second argument were
     * converted to a string by the method {@link String#valueOf(char)},
     * and the character in that string were then
     * {@link #insert(int, String) inserted} into this character
     * sequence at the indicated offset.
     * <p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param c      a {@code char}.
     * @return a reference to this object.
     * @throws IndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, char c) {
        sb.insert(offset, c);
        return this;
    }

    /**
     * Inserts the string representation of the second {@code int}
     * argument into this sequence.
     * <p>
     * The overall effect is exactly as if the second argument were
     * converted to a string by the method {@link String#valueOf(int)},
     * and the characters of that string were then
     * {@link #insert(int, String) inserted} into this character
     * sequence at the indicated offset.
     * <p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param i      an {@code int}.
     * @return a reference to this object.
     * @throws StringIndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, int i) {
        sb.insert(offset, i);
        return this;
    }

    /**
     * Inserts the string representation of the {@code long}
     * argument into this sequence.
     * <p>
     * The overall effect is exactly as if the second argument were
     * converted to a string by the method {@link String#valueOf(long)},
     * and the characters of that string were then
     * {@link #insert(int, String) inserted} into this character
     * sequence at the indicated offset.
     * <p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param l      a {@code long}.
     * @return a reference to this object.
     * @throws StringIndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, long l) {
        sb.insert(offset, l);
        return this;
    }

    /**
     * Inserts the string representation of the {@code float}
     * argument into this sequence.
     * <p>
     * The overall effect is exactly as if the second argument were
     * converted to a string by the method {@link String#valueOf(float)},
     * and the characters of that string were then
     * {@link #insert(int, String) inserted} into this character
     * sequence at the indicated offset.
     * <p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param f      a {@code float}.
     * @return a reference to this object.
     * @throws StringIndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, float f) {
        sb.insert(offset, f);
        return this;
    }

    /**
     * Inserts the string representation of the {@code double}
     * argument into this sequence.
     * <p>
     * The overall effect is exactly as if the second argument were
     * converted to a string by the method {@link String#valueOf(double)},
     * and the characters of that string were then
     * {@link #insert(int, String) inserted} into this character
     * sequence at the indicated offset.
     * <p>
     * The {@code offset} argument must be greater than or equal to
     * {@code 0}, and less than or equal to the {@linkplain #length() length}
     * of this sequence.
     *
     * @param offset the offset.
     * @param d      a {@code double}.
     * @return a reference to this object.
     * @throws StringIndexOutOfBoundsException if the offset is invalid.
     */
    public StringBuilder2 insert(int offset, double d) {
        sb.insert(offset, d);
        return this;
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring. The integer returned is the smallest value
     * <i>k</i> such that:
     * <pre>{@code
     * this.toString().startsWith(str, <i>k</i>)
     * }</pre>
     * is {@code true}.
     *
     * @param str any string.
     * @return if the string argument occurs as a substring within this
     * object, then the index of the first character of the first
     * such substring is returned; if it does not occur as a
     * substring, {@code -1} is returned.
     */
    public int indexOf(String str) {
        return sb.indexOf(str);
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, starting at the specified index.  The integer
     * returned is the smallest value {@code k} for which:
     * <pre>{@code
     *     k >= Math.min(fromIndex, this.length()) &&
     *                   this.toString().startsWith(str, k)
     * }</pre>
     * If no such value of <i>k</i> exists, then -1 is returned.
     *
     * @param str       the substring for which to search.
     * @param fromIndex the index from which to start the search.
     * @return the index within this string of the first occurrence of the
     * specified substring, starting at the specified index.
     */
    public int indexOf(String str, int fromIndex) {
        return sb.indexOf(str, fromIndex);
    }

    /**
     * Returns the index within this string of the rightmost occurrence
     * of the specified substring.  The rightmost empty string "" is
     * considered to occur at the index value {@code this.length()}.
     * The returned index is the largest value <i>k</i> such that
     * <pre>{@code
     * this.toString().startsWith(str, k)
     * }</pre>
     * is true.
     *
     * @param str the substring to search for.
     * @return if the string argument occurs one or more times as a substring
     * within this object, then the index of the first character of
     * the last such substring is returned. If it does not occur as
     * a substring, {@code -1} is returned.
     */
    public int lastIndexOf(String str) {
        return sb.lastIndexOf(str);
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring. The integer returned is the largest value <i>k</i>
     * such that:
     * <pre>{@code
     *     k <= Math.min(fromIndex, this.length()) &&
     *                   this.toString().startsWith(str, k)
     * }</pre>
     * If no such value of <i>k</i> exists, then -1 is returned.
     *
     * @param str       the substring to search for.
     * @param fromIndex the index to start the search from.
     * @return the index within this sequence of the last occurrence of the
     * specified substring.
     */
    public int lastIndexOf(String str, int fromIndex) {
        return sb.lastIndexOf(str, fromIndex);
    }

    /**
     * Causes this character sequence to be replaced by the reverse of
     * the sequence. If there are any surrogate pairs included in the
     * sequence, these are treated as single characters for the
     * reverse operation. Thus, the order of the high-low surrogates
     * is never reversed.
     * <p>
     * Let <i>n</i> be the character length of this character sequence
     * (not the length in {@code char} values) just prior to
     * execution of the {@code reverse} method. Then the
     * character at index <i>k</i> in the new character sequence is
     * equal to the character at index <i>n-k-1</i> in the old
     * character sequence.
     * <p>
     * <p>Note that the reverse operation may result in producing
     * surrogate pairs that were unpaired low-surrogates and
     * high-surrogates before the operation. For example, reversing
     * "\u005CuDC00\u005CuD800" produces "\u005CuD800\u005CuDC00" which is
     * a valid surrogate pair.
     *
     * @return a reference to this object.
     */
    public StringBuilder2 reverse() {
        sb.reverse();
        return this;
    }

    /**
     * Tests if the substring of this string beginning at the
     * specified index starts with the specified prefix.
     *
     * @param prefix  the prefix.
     * @param toffset where to begin looking in this string.
     * @return {@code true} if the character sequence represented by the
     * argument is a prefix of the substring of this object starting
     * at index {@code toffset}; {@code false} otherwise.
     * The result is {@code false} if {@code toffset} is
     * negative or greater than the length of this
     * {@code String} object; otherwise the result is the same
     * as the result of the expression
     * <pre>
     *          this.substring(toffset).startsWith(prefix)
     *          </pre>
     */
    public boolean startsWith(String prefix, int toffset) {
        int to = toffset;
        int po = 0;
        int pc = prefix.length();
        // Note: toffset might be near -1>>>1.
        if ((toffset < 0) || (toffset > length() - pc)) {
            return false;
        }
        while (--pc >= 0) {
            if (charAt(to++) != prefix.charAt(po++)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests if this string starts with the specified prefix.
     *
     * @param prefix the prefix.
     * @return {@code true} if the character sequence represented by the
     * argument is a prefix of the character sequence represented by
     * this string; {@code false} otherwise.
     * Note also that {@code true} will be returned if the
     * argument is an empty string or is equal to this
     * {@code String} object as determined by the
     * {@link #equals(Object)} method.
     * @since 1. 0
     */
    public boolean startsWith(String prefix) {
        return startsWith(prefix, 0);
    }

    /**
     * Tests if this string ends with the specified suffix.
     *
     * @param suffix the suffix.
     * @return {@code true} if the character sequence represented by the
     * argument is a suffix of the character sequence represented by
     * this object; {@code false} otherwise. Note that the
     * result will be {@code true} if the argument is the
     * empty string or is equal to this {@code String} object
     * as determined by the {@link #equals(Object)} method.
     */
    public boolean endsWith(String suffix) {
        return startsWith(suffix, length() - suffix.length());
    }

    public boolean regionMatches(boolean ignoreCase, int toffset, String other, int ooffset, int len) {
        return toString().regionMatches(ignoreCase, toffset, other, ooffset, len);
    }

    public boolean regionMatches(int toffset, String other, int ooffset, int len) {
        return toString().regionMatches(toffset, other, ooffset, len);
    }
}