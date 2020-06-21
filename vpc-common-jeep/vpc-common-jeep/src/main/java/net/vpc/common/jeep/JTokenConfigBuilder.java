/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import net.vpc.common.jeep.core.tokens.JavaIdPattern;

import java.util.*;

/**
 * @author vpc
 */
public final class JTokenConfigBuilder implements Cloneable, JTokenConfig {
    public static final JTokenPattern DEFAULT_ID_PATTERN = new JavaIdPattern();
    //            Pattern.compile("[^\\s0-9\\[\\]{}()*+/~&=$£%'\"-]([^\\s\\[\\]{}()*+/~&=$£%'\"-])*");
    private boolean acceptIntNumber = true;
    private boolean acceptFloatNumber = true;
    private boolean acceptInfinity = true;
    private char[] numberSuffixes = null;
    private JTokenEvaluator numberEvaluator = null;
    private boolean doubleQuote = true;
    private boolean simpleQuote = true;
    private boolean antiQuote = true;
    private String lineComment = null;
    private String blockCommentStart = null;
    private String blockCommentEnd = null;
    private Set<String> operators = _defaultOperators();
    private List<JTokenPattern> patterns = new ArrayList<>();
    private Set<String> keywords = new HashSet<>();
    private JTokenPattern pattern = DEFAULT_ID_PATTERN;
    private JTokenConfigDefinition readOnlyCopy;
    private boolean caseSensitive = true;
    public JTokenConfigBuilder() {
    }

    public JTokenConfigBuilder(JTokenConfig other) {
        setAll(other);
    }

    public static Set<String> _defaultOperators() {
        return new HashSet<>(Arrays.asList("+", "-", "*", "/", "^", "=", "<", ">", "!", "&", "|"));
    }

    public JTokenConfigBuilder copy() {
        try {
            return (JTokenConfigBuilder) this.clone();
        } catch (CloneNotSupportedException ex) {
            throw new IllegalArgumentException("ShouldNeverHappen");
        }
    }

    public JTokenConfigBuilder unsetAll(){
        acceptIntNumber = false;
        acceptFloatNumber = false;
        acceptInfinity = false;
        numberSuffixes = null;
        numberEvaluator = null;
        doubleQuote = false;
        simpleQuote = false;
        antiQuote = false;
        lineComment = null;
        blockCommentStart = null;
        blockCommentEnd = null;
        operators.clear();;
        patterns.clear();
        keywords.clear();
        pattern = null;
        readOnlyCopy=null;
        caseSensitive = true;
        return this;
    }

    @Override
    public Set<String> getOperators() {
        return operators;
    }

    @Override
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    @Override
    public Set<String> getKeywords() {
        return keywords;
    }

    @Override
    public JTokenPattern getIdPattern() {
        return pattern;
    }

    @Override
    public boolean isAcceptIntNumber() {
        return acceptIntNumber;
    }

    @Override
    public boolean isAcceptFloatNumber() {
        return acceptFloatNumber;
    }



    @Override
    public boolean isDoubleQuote() {
        return doubleQuote;
    }

    @Override
    public boolean isAcceptInfinity() {
        return acceptInfinity;
    }

    @Override
    public boolean isSimpleQuote() {
        return simpleQuote;
    }

    @Override
    public boolean isAntiQuote() {
        return antiQuote;
    }

    @Override
    public String getLineComment() {
        return lineComment;
    }

    @Override
    public String getBlockCommentStart() {
        return blockCommentStart;
    }

    @Override
    public String getBlockCommentEnd() {
        return blockCommentEnd;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    public char[] getNumberSuffixes() {
        return numberSuffixes;
    }

    public JTokenConfigBuilder setNumberSuffixes(char[] numberSuffixes) {
        this.numberSuffixes = numberSuffixes;
        return this;
    }

    public JTokenEvaluator getNumberEvaluator() {
        return numberEvaluator;
    }

    public JTokenConfigBuilder setNumberEvaluator(JTokenEvaluator numberEvaluator) {
        this.numberEvaluator = numberEvaluator;
        return this;
    }

    @Override
    public JTokenConfig setAll(JTokenConfig other) {
        if (other != null) {
            this.acceptIntNumber = other.isAcceptIntNumber();
            this.acceptFloatNumber = other.isAcceptFloatNumber();
            this.numberSuffixes = other.getNumberSuffixes()==null?null:Arrays.copyOf(other.getNumberSuffixes(),other.getNumberSuffixes().length);
            this.numberEvaluator = other.getNumberEvaluator();
            this.acceptInfinity = other.isAcceptInfinity();
            this.doubleQuote = other.isDoubleQuote();
            this.simpleQuote = other.isSimpleQuote();
            this.antiQuote = other.isAntiQuote();
            this.lineComment = other.getLineComment();
            this.blockCommentStart = other.getBlockCommentStart();
            this.blockCommentEnd = other.getBlockCommentEnd();
            this.operators = new HashSet<>(other.getOperators());
            this.keywords = new HashSet<>(other.getKeywords());
            this.patterns = new ArrayList<>(other.getPatterns());
            this.caseSensitive = other.isCaseSensitive();
            this.pattern = other.getIdPattern();
        }
        return this;
    }

    @Override
    public List<JTokenPattern> getPatterns() {
        return patterns;
    }

    public JTokenPattern getPattern(String name){
        return getPatterns().stream().filter(x -> x.name().equals(name)).findFirst().get();
    }
    
    public void removePattern(JTokenPattern p){
        patterns.remove(p);
    }

    @Override
    public JTokenConfig addPatterns(JTokenPattern... patterns) {
        if (patterns != null) {
            for (JTokenPattern pattern : patterns) {
                addPattern(pattern);
            }
        }
        return this;
    }

    @Override
    public JTokenConfig addPattern(JTokenPattern pattern) {
        if (pattern != null) {
            this.patterns.add(pattern);
        }
        return this;
    }

    @Override
    public JTokenConfig setPatterns(List<JTokenPattern> other) {
        this.patterns.clear();
        if (other != null) {
            for (JTokenPattern tokenPattern : other) {
                addPattern(tokenPattern);
            }
        }
        return this;
    }


    @Override
    public JTokenConfig addKeywords(String... keywords) {
        for (String keyword : keywords) {
            if (keyword != null) {
                getKeywords().add(keyword);
            }
        }
        return this;
    }

    @Override
    public JTokenConfig setKeywords(Set<String> keywords) {
        this.keywords = keywords == null ? new HashSet<>() : keywords;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfigBuilder addOperator(String s) {
        if (s != null) {
            operators.add(s);
        }
        return this;
    }

    @Override
    public JTokenConfigBuilder addOperators(String... s) {
        for (String s1 : s) {
            addOperator(s1);
        }
        return this;
    }

    @Override
    public JTokenConfig setOperators(Set<String> operators) {
        this.operators = operators == null ? _defaultOperators() : operators;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setAcceptIntNumber(boolean acceptIntNumber) {
        this.acceptIntNumber = acceptIntNumber;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setAcceptFloatNumber(boolean acceptFloatNumber) {
        this.acceptFloatNumber = acceptFloatNumber;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setBlockCommentEnd(String blockCommentEnd) {
        this.blockCommentEnd = blockCommentEnd;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setCStyleBlockComments() {
        return setBlockComments("/*", "*/");
    }

    @Override
    public JTokenConfig setCStyleComments() {
        setBlockComments("/*", "*/");
        return setLineComment("//");
    }

    @Override
    public JTokenConfig setBlockComments(String blockCommentStart, String blockCommentEnd) {
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
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setIdPattern(JTokenPattern pattern) {
        this.pattern = pattern == null ? DEFAULT_ID_PATTERN : pattern;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setLineComment(String lineComment) {
        if (lineComment == null || lineComment.length() == 0) {
            lineComment = null;
        }
        this.lineComment = lineComment;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setBlockCommentStart(String blockCommentStart) {
        this.blockCommentStart = blockCommentStart;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setAcceptInfinity(boolean acceptInfinity) {
        this.acceptInfinity = acceptInfinity;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setDoubleQuote(boolean doubleQuote) {
        this.doubleQuote = doubleQuote;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setSimpleQuote(boolean quoteSmp) {
        this.simpleQuote = quoteSmp;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfig setAntiQuote(boolean antiQuote) {
        this.antiQuote = antiQuote;
        readOnlyCopy = null;
        return this;
    }

    @Override
    public JTokenConfigDefinition readOnly() {
        if (readOnlyCopy == null) {
            readOnlyCopy = new JTokenConfigDefinition(this);
        }
        return readOnlyCopy;
    }
}
