/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import java.util.*;

/**
 * @author vpc
 */
public final class JTokenConfigDefinition implements Cloneable, JTokenConfig {

    public static final JTokenConfigDefinition DEFAULT = new JTokenConfigDefinition(null);

    private final boolean acceptIntNumber;
    private final boolean acceptFloatNumber;
    private final boolean acceptInfinity;
    private final boolean quoteDbl;
    private final boolean quoteSmp;
    private final boolean quoteAnti;
    private final String lineComment;
    private final String blockCommentStart;
    private final String blockCommentEnd;
    private final Set<String> operators;
    private final Set<String> keywords;
    private final List<JTokenPattern> patterns;
    private final boolean caseSensitive;
    private final JTokenPattern idPattern;
    private char[] numberSuffixes = null;
    private JTokenEvaluator numberEvaluator = null;

    public JTokenConfigDefinition(JTokenConfigBuilder other) {
        if (other != null) {
            this.acceptIntNumber = other.isAcceptIntNumber();
            this.acceptFloatNumber = other.isAcceptFloatNumber();
            this.numberSuffixes = other.getNumberSuffixes() == null ? new char[0] : Arrays.copyOf(other.getNumberSuffixes(), other.getNumberSuffixes().length);
            this.numberEvaluator = other.getNumberEvaluator();
            this.acceptInfinity = other.isAcceptInfinity();
            this.quoteDbl = other.isDoubleQuote();
            this.quoteSmp = other.isSimpleQuote();
            this.quoteAnti = other.isAntiQuote();
            this.lineComment = other.getLineComment();
            this.blockCommentStart = other.getBlockCommentStart();
            this.blockCommentEnd = other.getBlockCommentEnd();
            this.operators = Collections.unmodifiableSet(new HashSet<>(other.getOperators()));
            this.keywords = Collections.unmodifiableSet(new HashSet<>(other.getKeywords()));
            this.caseSensitive = other.isCaseSensitive();
            this.idPattern = other.getIdPattern();
            this.patterns = Collections.unmodifiableList(new ArrayList<>(other.getPatterns()));
        } else {
            this.acceptIntNumber = true;
            this.acceptFloatNumber = true;
            this.numberSuffixes = new char[0];
            this.numberEvaluator = null;
            this.acceptInfinity = true;
            this.quoteDbl = true;
            this.quoteSmp = true;
            this.quoteAnti = true;
            this.lineComment = null;
            this.blockCommentStart = null;
            this.blockCommentEnd = null;
            this.operators = Collections.unmodifiableSet(
                    JTokenConfigBuilder._defaultOperators()
            );
            this.keywords = Collections.emptySet();
            this.patterns = Collections.emptyList();
            this.caseSensitive = true;
            this.idPattern = JTokenConfigBuilder.DEFAULT_ID_PATTERN;
        }
    }

    @Override
    public char[] getNumberSuffixes() {
        return (numberSuffixes == null || numberSuffixes.length == 0) ? numberSuffixes : Arrays.copyOf(numberSuffixes, numberSuffixes.length);
    }

    @Override
    public JTokenEvaluator getNumberEvaluator() {
        return numberEvaluator;
    }

    @Override
    public List<JTokenPattern> getPatterns() {
        return patterns;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public JTokenConfigBuilder builder() {
        return new JTokenConfigBuilder(this);
    }

    public JTokenConfigDefinition copy() {
        try {
            return (JTokenConfigDefinition) this.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("ShouldNeverHappen");
        }
    }

    public Set<String> getOperators() {
        return operators;
    }

    @Override
    public Set<String> getKeywords() {
        return keywords;
    }

    public JTokenPattern getIdPattern() {
        return idPattern;
    }

    public boolean isAcceptIntNumber() {
        return acceptIntNumber;
    }

    public boolean isAcceptFloatNumber() {
        return acceptFloatNumber;
    }

    public boolean isAcceptInfinity() {
        return acceptInfinity;
    }

    public boolean isDoubleQuote() {
        return quoteDbl;
    }

    public boolean isSimpleQuote() {
        return quoteSmp;
    }

    public boolean isAntiQuote() {
        return quoteAnti;
    }


    public String getLineComment() {
        return lineComment;
    }

    public String getBlockCommentStart() {
        return blockCommentStart;
    }

    public String getBlockCommentEnd() {
        return blockCommentEnd;
    }

    public boolean isReadOnly() {
        return true;
    }

    @Override
    public JTokenConfigDefinition readOnly() {
        return this;
    }

    // exceptions for writes

    private JTokenConfig throwReadOnly() {
        throw new JParseException("This is a read only copy of Token configuration");
    }

    @Override
    public JTokenConfig setAll(JTokenConfig other) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addKeywords(String... keywords) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setKeywords(Set<String> keywords) {
        return null;
    }

    @Override
    public JTokenConfig setAntiQuote(boolean quoteAnti) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setOperators(Set<String> operators) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setAcceptIntNumber(boolean acceptIntNumber) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setAcceptFloatNumber(boolean acceptFloatNumber) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setBlockCommentEnd(String blockCommentEnd) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setCStyleBlockComments() {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setCStyleComments() {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setBlockComments(String blockCommentStart, String blockCommentEnd) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setCaseSensitive(boolean caseSensitive) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setIdPattern(JTokenPattern pattern) {
        return throwReadOnly();
    }


    @Override
    public JTokenConfig setLineComment(String lineComment) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setBlockCommentStart(String blockCommentStart) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setAcceptInfinity(boolean acceptInfinity) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setDoubleQuote(boolean quoteDbl) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setSimpleQuote(boolean quoteSmp) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addPatterns(JTokenPattern... patterns) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addPattern(JTokenPattern pattern) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig setPatterns(List<JTokenPattern> other) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addOperator(String operator) {
        return throwReadOnly();
    }

    @Override
    public JTokenConfig addOperators(String... operators) {
        return throwReadOnly();
    }
}
