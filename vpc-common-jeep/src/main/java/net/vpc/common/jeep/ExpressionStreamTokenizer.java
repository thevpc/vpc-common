/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

/**
 * @author vpc
 */
public class ExpressionStreamTokenizer {

    /**
     * A constant indicating that the end of the stream has been read.
     */
    public static final int TT_EOF = -1;
    /**
     * A constant indicating that the end of the line has been read.
     */
    public static final int TT_EOL = '\n';
    /**
     * A constant indicating that a number token has been read.
     */
    public static final int TT_NUMBER_FLOAT = -2;
    /**
     * A constant indicating that a word token has been read.
     */
    public static final int TT_WORD = -3;

    /* A constant indicating that no token has been read, used for
     * initializing ttype.  FIXME This could be made public and
     * made available as the part of the API in a future release.
     */
    private static final int TT_NOTHING = -4;
    public static final int TT_NUMBER_COMPLEX = -5;
    /**
     * A constant indicating that a word token has been read.
     */
    public static final int TT_STRING = -6;
    public static final int TT_NUMBER_INT = -7;

    public static final int TT_LONG_OPERATOR = -8;
    public static final int TT_LINE_COMMENTS = -9;
    public static final int TT_BLOCK_COMMENTS = -10;
    private Reader reader;
    private ExpressionStreamTokenizerConfig config = new ExpressionStreamTokenizerConfig();

    private LinkedList<Token> back = new LinkedList<>();

    public ExpressionStreamTokenizer(Reader reader, ExpressionStreamTokenizerConfig config) {
        this.reader = reader;
        this.config = config == null ? new ExpressionStreamTokenizerConfig() : config.copy();
    }

    public void pushBack(Token token) {
//        System.out.println("PUSH "+token);
        back.add(token);
    }

    public Token peek() throws IOException {
        Token t = nextToken0();
        back.add(t);
//        System.out.println("PEEK "+t);
        return t;
    }

    public Token[] peek(int count) throws IOException {
        Token[] all = new Token[count];
        for (int i = 0; i < count; i++) {
            all[i] = nextToken();
        }
        for (int i = 0; i < count; i++) {
            pushBack(all[i]);
        }
        return all;
    }

    public Token nextToken() throws IOException {
        Token t = nextToken0();
//        System.out.println("READ "+t);
        return t;
    }

    public boolean isIdentifierStart(char cc) {
        return config.getIdentifierFilter().isIdentifierStart(cc);
    }

    public boolean isIdentifierPart(char cc) {
        return config.getIdentifierFilter().isIdentifierPart(cc);
    }

    protected boolean isQuoteDbl(char cc) {
        char quoteDbl = config.getQuoteDbl();
        return quoteDbl != '\0' && cc == quoteDbl;
    }

    protected boolean isQuoteSmp(char cc) {
        char quoteSmp = config.getQuoteSmp();
        return quoteSmp != '\0' && cc == quoteSmp;
    }

    protected boolean isQuoteAnti(char cc) {
        char quoteAnti = config.getQuoteAnti();
        return quoteAnti != '\0' && cc == quoteAnti;
    }

    protected boolean isQuoteDblEscape(char cc) {
        char quoteDblEscape = config.getQuoteDblEscape();
        return quoteDblEscape != '\0' && cc == quoteDblEscape;
    }

    protected boolean isQuoteSmpEscape(char cc) {
        char quoteSmpEscape = config.getQuoteSmpEscape();
        return quoteSmpEscape != '\0' && cc == quoteSmpEscape;
    }

    protected boolean isQuoteAntiEscape(char cc) {
        char quoteAntiEscape = config.getQuoteAntiEscape();
        return quoteAntiEscape != '\0' && cc == quoteAntiEscape;
    }

    public String skipString(String any) throws IOException {
        StringBuilder sb = new StringBuilder();
        int len = any.length();
        reader.mark(len);
        int i = 0;
        boolean ok = true;
        for (i = 0; i < len; i++) {
            int c = reader.read();
            if (c == -1) {
                ok = false;
            } else if (c != any.charAt(i)) {
                ok = false;
                sb.append((char) c);
                break;
            } else {
                sb.append((char) c);
            }
        }
        if (ok) {
            return sb.toString();
        }
        reader.reset();
        return null;
    }

    public String skipUntilString(String any, boolean consume) throws IOException {
        int len = any.length();
        int curr = 0;
        StringBuilder sb0 = new StringBuilder();
//        StringBuilder sb = new StringBuilder();
        while (true) {
            int c = reader.read();
            if (c == -1) {
                return sb0.toString();
            }
            sb0.append((char) c);
            if (c == any.charAt(curr)) {
//                sb.append((char) c);
                if (curr == 0) {
                    reader.mark(len);
                }
                curr++;
                if (curr == len) {
                    //all found!
                    if (!consume) {
                        sb0.delete(sb0.length() - len, sb0.length());
                        reader.reset();
                    }
                    return sb0.toString();
                }
            } else {
                curr = 0;
            }
        }
    }

    public Token skipComments() throws IOException {
        String lineComment = config.getLineComment();
        if (lineComment != null && lineComment.length() > 0) {
            String s1 = null;
            String s2 = null;
            if ((s1 = skipString(lineComment)) != null) {
                s2 = skipUntilString("\n", false);
                Token t = new Token();
                t.image = s1 + s2;
                t.sval = s2;
                t.ttype = TT_LINE_COMMENTS;
                return t;
            }
        }
        String blockCommentStart = config.getBlockCommentStart();
        if (blockCommentStart != null) {
            String blockCommentEnd = config.getBlockCommentEnd();
            if (blockCommentStart.length() > 0) {
//                if (blockCommentEnd == null || blockCommentEnd.length() == 0) {
//                    throw new IllegalArgumentException("Invalid block comments");
//                }
                String s1 = null;
                String s2 = null;
                if ((s1 = skipString(blockCommentStart)) != null) {
                    s2 = skipUntilString(blockCommentEnd, true);
                    Token t = new Token();
                    t.ttype = TT_BLOCK_COMMENTS;
                    t.image = s1 + s2;
                    return t;
                }
            }
        }
        return null;
    }

    public Token nextToken0() throws IOException {
        if (!back.isEmpty()) {
            return back.removeFirst();
        }
        while (true) {
            Token t = skipComments();
            if (t != null) {
                return t;
            }
            Token token = new Token();
            int cc = reader.read();
            boolean number = false;
            boolean floatNumber = false;
            boolean acceptAnyNumber = config.isAcceptComplexNumber() || config.isAcceptFloatNumber() || config.isAcceptIntNumber();
            if (cc == -1) {
                token.ttype=TT_EOF;
                return token;
            }
            if (cc == '-') {
//                reader.mark(1);
//                int cc1=reader.read();
//                if (cc1 >= '0' && cc1 <= '9' || cc1 == '.') {
//                    number=true;
//                }
//                reader.reset();
            } else if (acceptAnyNumber && (cc >= '0' && cc <= '9' || cc == '.')) {
                if (config.isAcceptFloatNumber() || config.isAcceptComplexNumber()) {
                    if (cc == '.') {
                        floatNumber = true;
                    }
                    number = true;
                } else if (config.isAcceptIntNumber()) {
                    if (cc != '.') {
                        number = true;
                    }
                }
            }
            if (number) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) cc);
                while (true) {
                    reader.mark(3);
                    cc = reader.read();
                    switch (cc) {
                        case -1: {
                            if (floatNumber) {
                                if (config.isAcceptFloatNumber()) {
                                    token.ttype = TT_NUMBER_FLOAT;
                                    token.image = sb.toString();
                                    token.fval = Double.valueOf(sb.toString());
                                } else if (config.isAcceptComplexNumber()) {
                                    token.ttype = TT_NUMBER_COMPLEX;
                                    token.image = sb.toString();
                                    token.cval = new ExprDoubleComplex(Double.valueOf(sb.toString()), 0);
                                }
                            } else {
                                if (config.isAcceptIntNumber()) {
                                    token.ttype = TT_NUMBER_INT;
                                    token.image = sb.toString();
                                    token.ival = Long.valueOf(sb.toString());
                                } else if (config.isAcceptFloatNumber()) {
                                    token.ttype = TT_NUMBER_FLOAT;
                                    token.image = sb.toString();
                                    token.fval = Double.valueOf(sb.toString());
                                } else if (config.isAcceptComplexNumber()) {
                                    token.ttype = TT_NUMBER_COMPLEX;
                                    token.image = sb.toString();
                                    token.cval = new ExprDoubleComplex(Double.valueOf(sb.toString()), 0);
                                }
                            }
                            return token;
                        }
                        case 'E': {
                            if (config.isAcceptFloatNumber() || config.isAcceptComplexNumber()) {
                                floatNumber = true;
                                sb.append((char) cc);
                                cc = reader.read();
                                switch (cc) {
                                    case -1: {
                                        break;
                                    }
                                    default: {
                                        sb.append((char) cc);
                                        break;
                                    }
                                }
                            } else {
                                if (config.isAcceptIntNumber()) {
                                    reader.reset();
                                    token.ttype = TT_NUMBER_INT;
                                    token.image = sb.toString();
                                    token.ival = Long.valueOf(sb.toString());
                                    return token;
                                } else {
                                    throw new IllegalArgumentException("Unexpected");
                                }
                            }
                            break;
                        }
                        default: {
                            if (cc == '.' && (config.isAcceptFloatNumber() || config.isAcceptComplexNumber())) {
                                floatNumber = true;
                                sb.append((char) cc);
                            } else if (cc >= '0' && cc <= '9') {
                                sb.append((char) cc);
                            } else if (isComplexUnit(cc, true)) {
                                token.ttype = TT_NUMBER_COMPLEX;
                                token.image = sb.toString() + ((char) cc);
                                token.cval = new ExprDoubleComplex(0, Double.valueOf(sb.toString()));
                                return token;
                            } else {
                                if(cc=='+' || cc=='-'){
                                    int cc2 = reader.read();
                                    if(cc2=='E' || cc2=='e'){
                                        sb.append((char)cc);
                                        sb.append((char)cc2);
                                        break;
                                    }else{
                                        reader.reset();
                                        if (floatNumber) {
                                            token.ttype = TT_NUMBER_FLOAT;
                                            token.image = (sb.toString());
                                            token.fval = Double.valueOf(sb.toString());
                                        } else {
                                            token.ttype = TT_NUMBER_INT;
                                            token.image = (sb.toString());
                                            token.ival = Long.valueOf(sb.toString());
                                        }
                                        return token;
                                    }
                                }
                                // now check if we can go further as word and not as number
                                //this happens when words can start with numbers
                                if (isValidWord(sb.toString()) && isIdentifierPart((char) cc)) {
                                    sb.append((char) cc);
                                    while (true) {
                                        reader.mark(1);
                                        cc = reader.read();
                                        if (isIdentifierPart((char) cc)) {
                                            sb.append((char) cc);
                                        } else {
                                            reader.reset();
                                            break;
                                        }
                                    }
//                                    if (sb.toString().length() == 1 && isComplexUnit(sb.charAt(0), false)) {
//                                        token.ttype = TT_NUMBER_COMPLEX;
//                                        token.cval = new ExprDoubleComplex(0, 1);
//                                        token.image = sb.toString();
//                                    } else {
                                    token.ttype = TT_WORD;
                                    token.sval = sb.toString();
                                    token.image = sb.toString();
//                                    }
                                    return token;
                                }
                                reader.reset();
                                if (floatNumber) {
                                    token.ttype = TT_NUMBER_FLOAT;
                                    token.image = (sb.toString());
                                    token.fval = Double.valueOf(sb.toString());
                                } else {
                                    token.ttype = TT_NUMBER_INT;
                                    token.image = (sb.toString());
                                    token.ival = Long.valueOf(sb.toString());
                                }
                                return token;
                            }
                        }
                    }
                }
            } else if (isComplexUnit(cc, false)) {
                token.ttype = TT_NUMBER_COMPLEX;
                token.image = String.valueOf((char) cc);
                token.cval = new ExprDoubleComplex(0, 1);
                return token;
            } else if (isIdentifierStart((char) cc)) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) cc);
                while (true) {
                    reader.mark(1);
                    cc = reader.read();
                    if (cc == -1) {
                        break;
                    } else if (isIdentifierPart((char) cc)) {
                        sb.append((char) cc);
                    } else {
                        reader.reset();
                        break;
                    }
                }
                if (sb.toString().length() == 1 && isComplexUnit(sb.charAt(0), false)) {
                    token.ttype = TT_NUMBER_COMPLEX;
                    token.cval = new ExprDoubleComplex(0, 1);
                    token.image = sb.toString();
                } else {
                    token.ttype = TT_WORD;
                    token.sval = sb.toString();
                    token.image = sb.toString();
                }
                return token;
            } else if (cc == ' ') {
                //ignore
            } else if (cc == '\t') {
                //ignore
            } else if (cc == '\n') {
                //ignore
            } else if (isQuoteDbl((char) cc)) {
                StringBuilder sb = new StringBuilder();
                StringBuilder image = new StringBuilder();
                image.append((char) cc);
                while (true) {
                    cc = reader.read();
                    image.append(cc);
                    // " or \
                    if (isQuoteDblEscape((char) cc)) {
                        //escape is same quote
                        // example : " hello "" you"                        
                        if (isQuoteDbl((char) cc)) {
                            reader.mark(1);
                            int cc2 = reader.read();
                            if (isQuoteDbl((char) cc2)) {
                                image.append(cc2);
                                sb.append((char) cc2);
                            } else {
                                reader.reset();
                                //this is the end of the quote
                                break;
                            }
                        } else {
                            int cc2 = reader.read();
                            image.append(cc2);
                            sb.append((char) cc2);
                        }
                    } else if (isQuoteDbl((char) cc)) {
                        image.append(cc);
                        break;
                    } else {
                        sb.append((char) cc);
                    }
                }
                token.ttype = TT_STRING;
                token.sval = sb.toString();
                token.image = image.toString();
                return token;
            } else if (isQuoteSmp((char) cc)) {
                StringBuilder sb = new StringBuilder();
                StringBuilder image = new StringBuilder();
                image.append((char) cc);
                while (true) {
                    cc = reader.read();
                    image.append(cc);
                    // " or \
                    if (isQuoteSmpEscape((char) cc)) {
                        //escape is same quote
                        // example : " hello "" you"                        
                        if (isQuoteSmp((char) cc)) {
                            reader.mark(1);
                            int cc2 = reader.read();
                            if (isQuoteSmp((char) cc2)) {
                                image.append(cc2);
                                sb.append((char) cc2);
                            } else {
                                reader.reset();
                                //this is the end of the quote
                                break;
                            }
                        } else {
                            int cc2 = reader.read();
                            image.append(cc2);
                            sb.append((char) cc2);
                        }
                    } else if (isQuoteSmp((char) cc)) {
                        image.append(cc);
                        break;
                    } else {
                        sb.append((char) cc);
                    }
                }
                token.ttype = TT_STRING;
                token.sval = sb.toString();
                token.image = image.toString();
                return token;
            } else if (isQuoteAnti((char) cc)) {
                StringBuilder sb = new StringBuilder();
                StringBuilder image = new StringBuilder();
                image.append((char) cc);
                while (true) {
                    cc = reader.read();
                    image.append(cc);
                    // " or \
                    if (isQuoteAntiEscape((char) cc)) {
                        //escape is same quote
                        // example : " hello "" you"                        
                        if (isQuoteAnti((char) cc)) {
                            reader.mark(1);
                            int cc2 = reader.read();
                            if (isQuoteAnti((char) cc2)) {
                                image.append(cc2);
                                sb.append((char) cc2);
                            } else {
                                reader.reset();
                                //this is the end of the quote
                                break;
                            }
                        } else {
                            int cc2 = reader.read();
                            image.append(cc2);
                            sb.append((char) cc2);
                        }
                    } else if (isQuoteAnti((char) cc)) {
                        image.append(cc);
                        break;
                    } else {
                        sb.append((char) cc);
                    }
                }
                token.ttype = TT_STRING;
                token.sval = sb.toString();
                token.image = image.toString();
                return token;
            } else if (isOpChar(cc)) {
                StringBuilder opString = new StringBuilder();
                int cc0 = cc;
                opString.append((char) cc);
                while (true) {
                    reader.mark(1);
                    cc = reader.read();
                    if (!isOpChar(cc)) {
                        reader.reset();
                        break;
                    }
                    opString.append((char) cc);
                }
                if (opString.length() == 1) {
                    token.ttype = cc0;
                    token.image = String.valueOf((char) cc0);
                } else {
                    token.ttype = TT_LONG_OPERATOR;
                    token.image = opString.toString();
                }
                return token;
            } else {
                token.ttype = cc;
                token.image = String.valueOf((char) cc);
                return token;
            }
        }
    }

    private boolean isComplexUnit(int cc, boolean precededWithNumber) {
        if (!config.isAcceptComplexNumber()) {
            return false;
        }
        if (precededWithNumber) {
            return cc == 'î' || cc == 'i';
        }
        return cc == 'î';
    }

    private boolean isOpChar(int cc) {
        return config.isOperatorChar(cc);
    }

    private boolean isValidWord(String s) {
        if (s.isEmpty()) {
            return false;
        }
        if (!config.getIdentifierFilter().isIdentifierStart(s.charAt(0))) {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            if (!config.getIdentifierFilter().isIdentifierPart(s.charAt(0))) {
                return false;
            }
        }
        return true;
    }

    public static class Token {

        public int LINENO = 0;
        public int ttype = TT_NOTHING;
        public double fval;
        public long ival;
        public ExprDoubleComplex cval;
        public String sval;
        public String image;

        public boolean isEOF() {
            return ttype == TT_EOF;
        }

//        public long getOpPrecedence() {
//            int x = 1;
//            for (char c : image.toCharArray()) {
//                int i = operators.indexOf(c);
//                if (i >= 0) {
//                    x = x * operators.length() + i;
//                }
//            }
//            return Math.abs(x);
//        }
//
//        public boolean isOp() {
//            for (char c : operators.toCharArray()) {
//                if (image.charAt(0) == c) {
//                    return true;
//                }
//            }
//            return false;
//        }
        public Token() {
            ival = 0;
            fval = 0;
            cval = null;
            sval = null;
            image = null;
            ttype = -1;
        }

        @Override
        public String toString() {
            String ret;
            switch (ttype) {
                case TT_EOF:
                    ret = "EOF";
                    break;
//	  case TT_EOL:
//	    ret = "EOL";
//	    break;
                case TT_WORD:
                    ret = sval;
                    break;
                case TT_NUMBER_COMPLEX:
                    ret = "c=" + cval.toString();
                    break;
                case TT_NUMBER_FLOAT:
                    ret = "f=" + fval;
                    break;
                case TT_NUMBER_INT:
                    ret = "i=" + ival;
                    break;
                case TT_STRING:
                    ret = "s=" + sval;
                    break;
                case TT_LONG_OPERATOR:
                    ret = "lop=" + image;
                    break;
                case TT_BLOCK_COMMENTS:
                    ret = "BlocComment=" + image;
                    break;
                case TT_LINE_COMMENTS:
                    ret = "LineComment=" + image;
                    break;
//   	  case TT_NOTHING:
//	    ret = "NOTHING";
//	    break;
                default: {
                    char s[] = new char[3];
                    s[0] = s[2] = '\'';
                    s[1] = (char) ttype;
                    ret = new String(s);
                    break;
                }
            }
            return "Token[" + ret + "], line " + LINENO;
        }
    }
}
