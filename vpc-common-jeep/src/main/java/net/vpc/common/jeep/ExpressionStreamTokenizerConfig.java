/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

/**
 *
 * @author vpc
 */
public final class ExpressionStreamTokenizerConfig implements Cloneable {

    private boolean acceptIntNumber = true;
    private boolean acceptFloatNumber = true;
    private boolean acceptComplexNumber = true;
    private char quoteDbl = '\"';
    private char quoteSmp = '\'';
    private char quoteAnti = '`';
    private char quoteDblEscape = '\\';
    private char quoteSmpEscape = '\\';
    private char quoteAntiEscape = '\\';
    private String lineComment = null;
    private String blockCommentStart = null;
    private String blockCommentEnd = null;
    public String operators = "+-*/^=<>!&|$@:";
    private IdentifierFilter identifierFilter = DefaultIdentifierFilter.INSTANCE;

    public ExpressionStreamTokenizerConfig copy() {
        try {
            return (ExpressionStreamTokenizerConfig) this.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("ShouldNeverHappen");
        }
    }

    public String getOperators() {
        return operators;
    }

    public ExpressionStreamTokenizerConfig setOperators(String operators) {
        this.operators = operators == null ? "" : operators;
        return this;
    }

    public IdentifierFilter getIdentifierFilter() {
        return identifierFilter;
    }

    public ExpressionStreamTokenizerConfig setIdentifierFilter(IdentifierFilter identifierFilter) {
        this.identifierFilter = identifierFilter;
        return this;
    }

    public ExpressionStreamTokenizerConfig setAcceptComplexNumber(boolean acceptComplex) {
        this.acceptComplexNumber = acceptComplex;
        return this;
    }

    public boolean isAcceptIntNumber() {
        return acceptIntNumber;
    }

    public ExpressionStreamTokenizerConfig setAcceptIntNumber(boolean acceptIntNumber) {
        this.acceptIntNumber = acceptIntNumber;
        return this;
    }

    public boolean isAcceptFloatNumber() {
        return acceptFloatNumber;
    }

    public ExpressionStreamTokenizerConfig setAcceptFloatNumber(boolean acceptFloatNumber) {
        this.acceptFloatNumber = acceptFloatNumber;
        return this;
    }
    
    public ExpressionStreamTokenizerConfig unsetQuoteDbl() {
        setQuoteDbl('\0');
        return this;
    }

    public ExpressionStreamTokenizerConfig unsetQuoteSmp() {
        setQuoteSmp('\0');
        return this;
    }

    public ExpressionStreamTokenizerConfig unsetQuoteAnti() {
        setQuoteAnti('\0');
        return this;
    }

    public ExpressionStreamTokenizerConfig setQuoteSmp(char quoteSmp) {
        this.quoteSmp = quoteSmp;
        return this;
    }

    public boolean isOperatorChar(int cc) {
        return operators.indexOf(cc) >= 0;
    }

    public boolean isAcceptComplexNumber() {
        return acceptComplexNumber;
    }

    public char getQuoteDbl() {
        return quoteDbl;
    }

    public ExpressionStreamTokenizerConfig setQuoteDbl(char quoteDbl) {
        this.quoteDbl = quoteDbl;
        return this;
    }

    public char getQuoteSmp() {
        return quoteSmp;
    }

    public char getQuoteAnti() {
        return quoteAnti;
    }

    public ExpressionStreamTokenizerConfig setQuoteAnti(char quoteAnti) {
        this.quoteAnti = quoteAnti;
        return this;
    }

    public char getQuoteDblEscape() {
        return quoteDblEscape;
    }

    public ExpressionStreamTokenizerConfig setQuoteDblEscape(char quoteDblEscape) {
        this.quoteDblEscape = quoteDblEscape;
        return this;
    }

    public char getQuoteSmpEscape() {
        return quoteSmpEscape;
    }

    public ExpressionStreamTokenizerConfig setQuoteSmpEscape(char quoteSmpEscape) {
        this.quoteSmpEscape = quoteSmpEscape;
        return this;
    }

    public char getQuoteAntiEscape() {
        return quoteAntiEscape;
    }

    public ExpressionStreamTokenizerConfig setQuoteAntiEscape(char quoteAntiEscape) {
        this.quoteAntiEscape = quoteAntiEscape;
        return this;
    }

    public String getLineComment() {
        return lineComment;
    }

    public ExpressionStreamTokenizerConfig setLineComment(String lineComment) {
        if (lineComment == null || lineComment.length() == 0) {
            lineComment = null;
        }
        this.lineComment = lineComment;
        return this;
    }

    public String getBlockCommentStart() {
        return blockCommentStart;
    }

    public String getBlockCommentEnd() {
        return blockCommentEnd;
    }

    public ExpressionStreamTokenizerConfig setCStyleBlockComments() {
        return setBlockComments("/*", "*/");
    }

    public ExpressionStreamTokenizerConfig setCStyleComments() {
        setBlockComments("/*", "*/");
        return setLineComment("//");
    }

    public ExpressionStreamTokenizerConfig setBlockComments(String blockCommentStart, String blockCommentEnd) {
        if (blockCommentStart == null || blockCommentStart.length() == 0) {
            blockCommentStart = null;
        }
        if (blockCommentEnd == null || blockCommentEnd.length() == 0) {
            blockCommentEnd = null;
        }
        if ((blockCommentStart == null) != (blockCommentEnd == null)) {
            throw new IllegalArgumentException("Invalid Block comment separators " + blockCommentStart + " " + blockCommentEnd);
        }
        this.blockCommentStart = blockCommentStart;
        this.blockCommentEnd = blockCommentEnd;
        return this;
    }

}
