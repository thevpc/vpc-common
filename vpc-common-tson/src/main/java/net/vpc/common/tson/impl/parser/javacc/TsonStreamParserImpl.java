/* Generated By:JavaCC: Do not edit this line. TsonStreamParserImpl.java */
package net.vpc.common.tson.impl.parser.javacc;
import net.vpc.common.tson.impl.elements.InputStreamTsonBinaryStreamSource;
import net.vpc.common.tson.impl.elements.TsonBinaryStreamImpl;
import net.vpc.common.tson.impl.elements.TsonBooleanImpl;
import net.vpc.common.tson.impl.elements.TsonNullImpl;
import net.vpc.common.tson.impl.parser.*;
import net.vpc.common.tson.*;

public class TsonStreamParserImpl implements ITsonStreamParser, TsonStreamParserImplConstants {
    private TsonStreamParserImplConfig config;
    private TsonParserVisitor visitor;
    public void setConfig(TsonStreamParserImplConfig c){
        this.config=c;
        this.visitor=config.getVisitor();
    }

    private String prepareComments(String comments){
        if(!config.isRawComments()){
            return TsonParserUtils.escapeComments(comments);
        }
        return comments;
    }

  final public void parseDocument() throws ParseException {
    parseElement();
        visitor.visitDocumentEnd();
  }

  final public void parseElement() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DATETIME:
    case DATE:
    case TIME:
    case REGEX:
    case LBRACE:
    case SHORT:
    case BYTE:
    case LONG:
    case INTEGER:
    case INTEGER_H:
    case INTEGER_O:
    case INTEGER_B:
    case LONG_H:
    case LONG_O:
    case LONG_B:
    case SHORT_H:
    case SHORT_O:
    case SHORT_B:
    case BYTE_H:
    case BYTE_O:
    case BYTE_B:
    case FLOAT:
    case DOUBLE:
    case STRING:
    case CHARACTER:
    case TRUE:
    case FALSE:
    case NULL:
    case LPAREN:
    case LBRACK:
    case AT:
    case CHARSTREAM_START:
    case NAN:
    case POS_INF:
    case NEG_INF:
    case POS_BOUND:
    case NEG_BOUND:
    case COMMENT:
    case NAME:
      elementLevel2();
      break;
    case 0:
      jj_consume_token(0);
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void elementLevel2() throws ParseException {
visitor.visitInstructionStart();
boolean simple=true;
    elementLevel1();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case COLON:
      jj_consume_token(COLON);
      elementLevel1();
                              simple=false;visitor.visitKeyValueEnd();
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
        if(simple){
            visitor.visitSimpleEnd();
        }
  }

  final public void elementLevel1() throws ParseException {
    net.vpc.common.tson.impl.parser.javacc.Token comments=null;
    visitor.visitElementStart();
    Token typeName=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case COMMENT:
      comments = jj_consume_token(COMMENT);
            if(!config.isSkipComments()) {
            visitor.visitComments(prepareComments(comments.image));
            }
      break;
    default:
      jj_la1[2] = jj_gen;
      ;
    }
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AT:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_1;
      }
      annotation();
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NULL:
      jj_consume_token(NULL);
             visitor.visitPrimitiveEnd(TsonNullImpl.INSTANCE);
      break;
    case TRUE:
      jj_consume_token(TRUE);
              visitor.visitPrimitiveEnd(TsonBooleanImpl.TRUE);
      break;
    case FALSE:
      jj_consume_token(FALSE);
               visitor.visitPrimitiveEnd(TsonBooleanImpl.FALSE);
      break;
    case DATETIME:
      jj_consume_token(DATETIME);
                   visitor.visitPrimitiveEnd(TsonParserUtils.parseDateTimeElem(token.image));
      break;
    case DATE:
      jj_consume_token(DATE);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseDateElem(token.image));
      break;
    case TIME:
      jj_consume_token(TIME);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseTimeElem(token.image));
      break;
    case REGEX:
      jj_consume_token(REGEX);
                visitor.visitPrimitiveEnd(TsonParserUtils.parseRegexElem(token.image));
      break;
    case BYTE:
      jj_consume_token(BYTE);
             visitor.visitPrimitiveEnd(TsonParserUtils.parseByteElem(token.image));
      break;
    case SHORT:
      jj_consume_token(SHORT);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseShortElem(token.image));
      break;
    case LONG:
      jj_consume_token(LONG);
              visitor.visitPrimitiveEnd(TsonParserUtils.parseLongElem(token.image));
      break;
    case INTEGER:
      jj_consume_token(INTEGER);
                visitor.visitPrimitiveEnd(TsonParserUtils.parseIntElem(token.image));
      break;
    case BYTE_B:
      jj_consume_token(BYTE_B);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseByteElemBin(token.image));
      break;
    case BYTE_O:
      jj_consume_token(BYTE_O);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseByteElemOctal(token.image));
      break;
    case BYTE_H:
      jj_consume_token(BYTE_H);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseByteElemHex(token.image));
      break;
    case SHORT_B:
      jj_consume_token(SHORT_B);
                visitor.visitPrimitiveEnd(TsonParserUtils.parseShortElemBin(token.image));
      break;
    case SHORT_O:
      jj_consume_token(SHORT_O);
                visitor.visitPrimitiveEnd(TsonParserUtils.parseShortElemOctal(token.image));
      break;
    case SHORT_H:
      jj_consume_token(SHORT_H);
                visitor.visitPrimitiveEnd(TsonParserUtils.parseShortElemHex(token.image));
      break;
    case INTEGER_B:
      jj_consume_token(INTEGER_B);
                  visitor.visitPrimitiveEnd(TsonParserUtils.parseIntElemBin(token.image));
      break;
    case INTEGER_O:
      jj_consume_token(INTEGER_O);
                  visitor.visitPrimitiveEnd(TsonParserUtils.parseIntElemOctal(token.image));
      break;
    case INTEGER_H:
      jj_consume_token(INTEGER_H);
                  visitor.visitPrimitiveEnd(TsonParserUtils.parseIntElemHex(token.image));
      break;
    case LONG_B:
      jj_consume_token(LONG_B);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseLongElemBin(token.image));
      break;
    case LONG_O:
      jj_consume_token(LONG_O);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseLongElemOctal(token.image));
      break;
    case LONG_H:
      jj_consume_token(LONG_H);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseLongElemHex(token.image));
      break;
    case FLOAT:
      jj_consume_token(FLOAT);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseFloatElem(token.image));
      break;
    case DOUBLE:
      jj_consume_token(DOUBLE);
               visitor.visitPrimitiveEnd(TsonParserUtils.parseDoubleElem(token.image));
      break;
    case CHARACTER:
      jj_consume_token(CHARACTER);
                   visitor.visitPrimitiveEnd(TsonParserUtils.parseCharElem(token.image));
      break;
    case STRING:
      jj_consume_token(STRING);
                 visitor.visitPrimitiveEnd(TsonParserUtils.parseStringElem(token.image));
      break;
    case NAN:
      jj_consume_token(NAN);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LPAREN:
        jj_consume_token(LPAREN);
        typeName = jj_consume_token(NAME);
        jj_consume_token(RPAREN);
        break;
      default:
        jj_la1[4] = jj_gen;
        ;
      }
                                                visitor.visitPrimitiveEnd(TsonParserUtils.parseNaNElem(token==null?null:token.image));
      break;
    case POS_INF:
      jj_consume_token(POS_INF);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LPAREN:
        jj_consume_token(LPAREN);
        typeName = jj_consume_token(NAME);
        jj_consume_token(RPAREN);
        break;
      default:
        jj_la1[5] = jj_gen;
        ;
      }
                                                    visitor.visitPrimitiveEnd(TsonParserUtils.parsePosInfElem(token==null?null:token.image));
      break;
    case NEG_INF:
      jj_consume_token(NEG_INF);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LPAREN:
        jj_consume_token(LPAREN);
        typeName = jj_consume_token(NAME);
        jj_consume_token(RPAREN);
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
      }
                                                    visitor.visitPrimitiveEnd(TsonParserUtils.parseNegInfElem(token==null?null:token.image));
      break;
    case POS_BOUND:
      jj_consume_token(POS_BOUND);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LPAREN:
        jj_consume_token(LPAREN);
        typeName = jj_consume_token(NAME);
        jj_consume_token(RPAREN);
        break;
      default:
        jj_la1[7] = jj_gen;
        ;
      }
                                                      visitor.visitPrimitiveEnd(TsonParserUtils.parsePosBoundElem(token==null?null:token.image));
      break;
    case NEG_BOUND:
      jj_consume_token(NEG_BOUND);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LPAREN:
        jj_consume_token(LPAREN);
        typeName = jj_consume_token(NAME);
        jj_consume_token(RPAREN);
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
                                                      visitor.visitPrimitiveEnd(TsonParserUtils.parseNegBoundElem(token==null?null:token.image));
      break;
    case CHARSTREAM_START:
      bin();
      break;
    case LBRACE:
      object(false);
      break;
    case LBRACK:
      array(false);
      break;
    case NAME:
      named();
      break;
    case LPAREN:
      unnamed();
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void annotation() throws ParseException {
  Token id=null;
    jj_consume_token(AT);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NAME:
      id = jj_consume_token(NAME);
      break;
    default:
      jj_la1[10] = jj_gen;
      ;
    }
        visitor.visitAnnotationStart(id==null?null:id.image);
    jj_consume_token(LPAREN);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case RPAREN:
      jj_consume_token(RPAREN);
      break;
    case DATETIME:
    case DATE:
    case TIME:
    case REGEX:
    case LBRACE:
    case SHORT:
    case BYTE:
    case LONG:
    case INTEGER:
    case INTEGER_H:
    case INTEGER_O:
    case INTEGER_B:
    case LONG_H:
    case LONG_O:
    case LONG_B:
    case SHORT_H:
    case SHORT_O:
    case SHORT_B:
    case BYTE_H:
    case BYTE_O:
    case BYTE_B:
    case FLOAT:
    case DOUBLE:
    case STRING:
    case CHARACTER:
    case TRUE:
    case FALSE:
    case NULL:
    case LPAREN:
    case LBRACK:
    case AT:
    case CHARSTREAM_START:
    case NAN:
    case POS_INF:
    case NEG_INF:
    case POS_BOUND:
    case NEG_BOUND:
    case COMMENT:
    case NAME:
      annotationParam();
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_2;
        }
        jj_consume_token(COMMA);
        annotationParam();
      }
      jj_consume_token(RPAREN);
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
         visitor.visitAnnotationEnd();
  }

  final public void bin() throws ParseException {
    InputStreamTsonBinaryStreamSource bin=new InputStreamTsonBinaryStreamSource();
    jj_consume_token(CHARSTREAM_START);
    bin.pushBase64(token.image);
    jj_consume_token(CHARSTREAM_END);
    visitor.visitPrimitiveEnd(new TsonBinaryStreamImpl(bin));
  }

  final public void named() throws ParseException {
  Token id;
    boolean hasPars=false;
    boolean hasArr=false;
    boolean hasObj=false;
    id = jj_consume_token(NAME);
        visitor.visitNamedStart(id.image);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
      unnamed0();
                  hasPars=true;
      break;
    default:
      jj_la1[13] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
    case LBRACK:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACK:
        array(true);
                      hasArr=true;
        break;
      case LBRACE:
        object(true);
                       hasObj=true;
        break;
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[15] = jj_gen;
      ;
    }
        if(!hasArr && !hasObj){
            if(!hasPars){
                visitor.visitPrimitiveEnd(Tson.name(id.image));
            }else {
                visitor.visitFunctionEnd();
            }
        }
  }

  final public void param() throws ParseException {
    visitor.visitParamElementStart();
    elementLevel2();
        visitor.visitParamElementEnd();
  }

  final public void unnamed() throws ParseException {
    boolean hasArr=false;
    boolean hasObj=false;
    unnamed0();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
    case LBRACK:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACK:
        array(true);
                      hasArr=true;
        break;
      case LBRACE:
        object(true);
                       hasObj=true;
        break;
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[17] = jj_gen;
      ;
    }
        if(!hasArr && !hasObj){
                visitor.visitUpletEnd();
        }
  }

  final public void unnamed0() throws ParseException {
    jj_consume_token(LPAREN);
    visitor.visitParamsStart();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case RPAREN:
      jj_consume_token(RPAREN);
      break;
    case DATETIME:
    case DATE:
    case TIME:
    case REGEX:
    case LBRACE:
    case SHORT:
    case BYTE:
    case LONG:
    case INTEGER:
    case INTEGER_H:
    case INTEGER_O:
    case INTEGER_B:
    case LONG_H:
    case LONG_O:
    case LONG_B:
    case SHORT_H:
    case SHORT_O:
    case SHORT_B:
    case BYTE_H:
    case BYTE_O:
    case BYTE_B:
    case FLOAT:
    case DOUBLE:
    case STRING:
    case CHARACTER:
    case TRUE:
    case FALSE:
    case NULL:
    case LPAREN:
    case LBRACK:
    case AT:
    case CHARSTREAM_START:
    case NAN:
    case POS_INF:
    case NEG_INF:
    case POS_BOUND:
    case NEG_BOUND:
    case COMMENT:
    case NAME:
      param();
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[18] = jj_gen;
          break label_3;
        }
        jj_consume_token(COMMA);
        param();
      }
      jj_consume_token(RPAREN);
      break;
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
     visitor.visitParamsEnd();
  }

  final public void objectElement() throws ParseException {
    visitor.visitObjectElementStart();
    elementLevel2();
        visitor.visitObjectElementEnd();
  }

  final public void arrayElement() throws ParseException {
    visitor.visitArrayElementStart();
    elementLevel2();
        visitor.visitArrayElementEnd();
  }

  final public void object(boolean named) throws ParseException {
    jj_consume_token(LBRACE);
        if(named){
            visitor.visitNamedObjectStart();
        }else{
            visitor.visitObjectStart();
        }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case RBRACE:
      jj_consume_token(RBRACE);
      break;
    case DATETIME:
    case DATE:
    case TIME:
    case REGEX:
    case LBRACE:
    case SHORT:
    case BYTE:
    case LONG:
    case INTEGER:
    case INTEGER_H:
    case INTEGER_O:
    case INTEGER_B:
    case LONG_H:
    case LONG_O:
    case LONG_B:
    case SHORT_H:
    case SHORT_O:
    case SHORT_B:
    case BYTE_H:
    case BYTE_O:
    case BYTE_B:
    case FLOAT:
    case DOUBLE:
    case STRING:
    case CHARACTER:
    case TRUE:
    case FALSE:
    case NULL:
    case LPAREN:
    case LBRACK:
    case AT:
    case CHARSTREAM_START:
    case NAN:
    case POS_INF:
    case NEG_INF:
    case POS_BOUND:
    case NEG_BOUND:
    case COMMENT:
    case NAME:
      objectElement();
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[20] = jj_gen;
          break label_4;
        }
        jj_consume_token(COMMA);
        objectElement();
      }
      jj_consume_token(RBRACE);
      break;
    default:
      jj_la1[21] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
            if(named){
                visitor.visitNamedObjectEnd();
            }else{
                visitor.visitObjectEnd();
            }
  }

  final public void array(boolean named) throws ParseException {
    jj_consume_token(LBRACK);
        if(named){
           visitor.visitNamedArrayStart();
        }else{
            visitor.visitArrayStart();
        }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case RBRACK:
      jj_consume_token(RBRACK);
      break;
    case DATETIME:
    case DATE:
    case TIME:
    case REGEX:
    case LBRACE:
    case SHORT:
    case BYTE:
    case LONG:
    case INTEGER:
    case INTEGER_H:
    case INTEGER_O:
    case INTEGER_B:
    case LONG_H:
    case LONG_O:
    case LONG_B:
    case SHORT_H:
    case SHORT_O:
    case SHORT_B:
    case BYTE_H:
    case BYTE_O:
    case BYTE_B:
    case FLOAT:
    case DOUBLE:
    case STRING:
    case CHARACTER:
    case TRUE:
    case FALSE:
    case NULL:
    case LPAREN:
    case LBRACK:
    case AT:
    case CHARSTREAM_START:
    case NAN:
    case POS_INF:
    case NEG_INF:
    case POS_BOUND:
    case NEG_BOUND:
    case COMMENT:
    case NAME:
      arrayElement();
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[22] = jj_gen;
          break label_5;
        }
        jj_consume_token(COMMA);
        arrayElement();
      }
      jj_consume_token(RBRACK);
      break;
    default:
      jj_la1[23] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
         if(named){
              visitor.visitNamedArrayEnd();
         }else{
            visitor.visitArrayEnd();
         }
  }

  final public void annotationParam() throws ParseException {
  visitor.visitAnnotationParamStart();
    elementLevel2();
    visitor.visitAnnotationParamEnd();
  }

  /** Generated Token Manager. */
  public TsonStreamParserImplTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[24];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0xfdffffbf,0x0,0x0,0x0,0x80000000,0x80000000,0x80000000,0x80000000,0x80000000,0xfdffffbe,0x0,0x0,0xfdffffbe,0x80000000,0x20,0x20,0x20,0x20,0x0,0xfdffffbe,0x0,0xfdfffffe,0x0,0xfdffffbe,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x3fe2,0x8,0x1000,0x20,0x0,0x0,0x0,0x0,0x0,0x2fc2,0x2000,0x10,0x3fe3,0x0,0x2,0x2,0x2,0x2,0x10,0x3fe3,0x10,0x3fe2,0x10,0x3fe6,};
   }

  /** Constructor with InputStream. */
  public TsonStreamParserImpl(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public TsonStreamParserImpl(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new TsonStreamParserImplTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public TsonStreamParserImpl(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new TsonStreamParserImplTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public TsonStreamParserImpl(TsonStreamParserImplTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(TsonStreamParserImplTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 24; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List jj_expentries = new java.util.ArrayList();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[55];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 24; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 55; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
