/* Generated By:JavaCC: Do not edit this line. JShellParserImplConstants.java */
package net.vpc.common.javashell.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface JShellParserImplConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SPACES = 1;
  /** RegularExpression Id. */
  int USING = 2;
  /** RegularExpression Id. */
  int IF = 3;
  /** RegularExpression Id. */
  int ELSE = 4;
  /** RegularExpression Id. */
  int WHILE = 5;
  /** RegularExpression Id. */
  int BREAK = 6;
  /** RegularExpression Id. */
  int GOTO = 7;
  /** RegularExpression Id. */
  int LABEL = 8;
  /** RegularExpression Id. */
  int OP_EQ = 9;
  /** RegularExpression Id. */
  int OP_COMMA = 10;
  /** RegularExpression Id. */
  int OP_AND = 11;
  /** RegularExpression Id. */
  int OP_AND2 = 12;
  /** RegularExpression Id. */
  int OP_THEN = 13;
  /** RegularExpression Id. */
  int OP_AT = 14;
  /** RegularExpression Id. */
  int OP_AT2 = 15;
  /** RegularExpression Id. */
  int OP_VDASH = 16;
  /** RegularExpression Id. */
  int OP_VDASH_AND = 17;
  /** RegularExpression Id. */
  int OP_VDASH2 = 18;
  /** RegularExpression Id. */
  int OP_LT = 19;
  /** RegularExpression Id. */
  int OP_LT2 = 20;
  /** RegularExpression Id. */
  int OP_LET = 21;
  /** RegularExpression Id. */
  int OP_GT = 22;
  /** RegularExpression Id. */
  int OP_GT2 = 23;
  /** RegularExpression Id. */
  int OP_GET = 24;
  /** RegularExpression Id. */
  int LACC = 25;
  /** RegularExpression Id. */
  int RACC = 26;
  /** RegularExpression Id. */
  int LPAR = 27;
  /** RegularExpression Id. */
  int LPAR2 = 28;
  /** RegularExpression Id. */
  int DLPAR = 29;
  /** RegularExpression Id. */
  int DLPAR2 = 30;
  /** RegularExpression Id. */
  int RPAR = 31;
  /** RegularExpression Id. */
  int RPAR2 = 32;
  /** RegularExpression Id. */
  int RED_IN = 33;
  /** RegularExpression Id. */
  int RED_OUT_APP = 34;
  /** RegularExpression Id. */
  int RED_ERR_APP = 35;
  /** RegularExpression Id. */
  int RED_OUT = 36;
  /** RegularExpression Id. */
  int RED_ERR = 37;
  /** RegularExpression Id. */
  int NEWLINE = 38;
  /** RegularExpression Id. */
  int ANTI_QUOTE = 39;
  /** RegularExpression Id. */
  int ITEM_STRING_DBL = 40;
  /** RegularExpression Id. */
  int ITEM_STRING_SMP = 41;
  /** RegularExpression Id. */
  int ITEM_VAR = 42;
  /** RegularExpression Id. */
  int ITEM_NAME = 43;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"using\"",
    "\"if\"",
    "\"else\"",
    "\"while\"",
    "\"break\"",
    "\"goto\"",
    "\"label\"",
    "\"=\"",
    "\";\"",
    "\"&\"",
    "\"&&\"",
    "\"==>\"",
    "\"@\"",
    "\"@@\"",
    "\"|\"",
    "\"|&\"",
    "\"||\"",
    "\"<\"",
    "\"<<\"",
    "\"<=\"",
    "\">\"",
    "\">>\"",
    "\">=\"",
    "\"{\"",
    "\"}\"",
    "\"(\"",
    "\"((\"",
    "\"$(\"",
    "\"$((\"",
    "\")\"",
    "\"))\"",
    "\"&<\"",
    "<RED_OUT_APP>",
    "\"&2>>\"",
    "<RED_OUT>",
    "\"&2>\"",
    "\"\\n\"",
    "\"`\"",
    "<ITEM_STRING_DBL>",
    "<ITEM_STRING_SMP>",
    "<ITEM_VAR>",
    "<ITEM_NAME>",
    "<token of kind 44>",
  };

}