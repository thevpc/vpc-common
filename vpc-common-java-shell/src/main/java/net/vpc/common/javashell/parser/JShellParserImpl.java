/* JShellParserImpl.java */
/* Generated By:JavaCC: Do not edit this line. JShellParserImpl.java */
package net.vpc.common.javashell.parser;
import net.vpc.common.javashell.parser.nodes.*;

class JShellParserImpl implements JShellParserImplConstants {
    private NodeTree tree=new NodeTree();

/* Production 1 */
  final public 
Node parse() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case SPACES:
    case IF:
    case WHILE:
    case GOTO:
    case LABEL:
    case LACC:
    case LPAR:
    case DLPAR:
    case DLPAR2:
    case NEWLINE:
    case ANTI_QUOTE:
    case ITEM_STRING_DBL:
    case ITEM_STRING_SMP:
    case ITEM_VAR:
    case ITEM_NAME:{
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:
        case NEWLINE:{
          ;
          break;
          }
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:{
          jj_consume_token(SPACES);
          break;
          }
        case NEWLINE:{
          jj_consume_token(NEWLINE);
          break;
          }
        default:
          jj_la1[1] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      block();
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:
        case NEWLINE:{
          ;
          break;
          }
        default:
          jj_la1[2] = jj_gen;
          break label_2;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:{
          jj_consume_token(SPACES);
          break;
          }
        case NEWLINE:{
          jj_consume_token(NEWLINE);
          break;
          }
        default:
          jj_la1[3] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      jj_consume_token(0);
Node node=tree.pop();//me
          {if ("" != null) return node;}
      break;
      }
    case 0:{
      jj_consume_token(0);
{if ("" != null) return null;}
      break;
      }
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public void commandItem() throws ParseException {tree.push(new CommandItemHolderNode());
    label_3:
    while (true) {
      commandItemPart();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DLPAR:
      case DLPAR2:
      case ANTI_QUOTE:
      case ITEM_STRING_DBL:
      case ITEM_STRING_SMP:
      case ITEM_VAR:
      case ITEM_NAME:{
        ;
        break;
        }
      default:
        jj_la1[5] = jj_gen;
        break label_3;
      }
    }
CommandItemHolderNode h=(CommandItemHolderNode) tree.pop();
    ((CommandNode)tree.peek()).add(h);
  }

  final public void commandItemPart() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ITEM_NAME:{
      jj_consume_token(ITEM_NAME);
((CommandItemHolderNode)tree.peek()).add(new WordNode(token));
      break;
      }
    case ITEM_STRING_DBL:{
      jj_consume_token(ITEM_STRING_DBL);
((CommandItemHolderNode)tree.peek()).add(new StringDoubleCotedNode(token));
      break;
      }
    case ITEM_STRING_SMP:{
      jj_consume_token(ITEM_STRING_SMP);
((CommandItemHolderNode)tree.peek()).add(new StringSimpleCotedNode(token));
      break;
      }
    case ITEM_VAR:{
      jj_consume_token(ITEM_VAR);
((CommandItemHolderNode)tree.peek()).add(new VarDollarNode(token));
      break;
      }
    case DLPAR:
    case DLPAR2:
    case ANTI_QUOTE:{
      itemstring();
CommandItemNode n=(CommandItemNode)tree.pop();
                ((CommandItemHolderNode)tree.peek()).add(n);
      break;
      }
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void commandItem_no_anti_quote() throws ParseException {tree.push(new CommandItemHolderNode());
    label_4:
    while (true) {
      commandItem_no_anti_quote_part();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DLPAR:
      case DLPAR2:
      case ITEM_STRING_DBL:
      case ITEM_STRING_SMP:
      case ITEM_VAR:
      case ITEM_NAME:{
        ;
        break;
        }
      default:
        jj_la1[7] = jj_gen;
        break label_4;
      }
    }
CommandItemHolderNode h=(CommandItemHolderNode) tree.pop();
    ((CommandNode)tree.peek()).add(h);
  }

  final public void commandItem_no_anti_quote_part() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ITEM_NAME:{
      jj_consume_token(ITEM_NAME);
((CommandItemHolderNode)tree.peek()).add(new WordNode(token));
      break;
      }
    case ITEM_STRING_DBL:{
      jj_consume_token(ITEM_STRING_DBL);
((CommandItemHolderNode)tree.peek()).add(new StringDoubleCotedNode(token));
      break;
      }
    case ITEM_STRING_SMP:{
      jj_consume_token(ITEM_STRING_SMP);
((CommandItemHolderNode)tree.peek()).add(new StringSimpleCotedNode(token));
      break;
      }
    case ITEM_VAR:{
      jj_consume_token(ITEM_VAR);
((CommandItemHolderNode)tree.peek()).add(new VarDollarNode(token));
      break;
      }
    case DLPAR:
    case DLPAR2:{
      itemstring_no_anti_quote();
CommandItemNode n=(CommandItemNode)tree.pop();
                ((CommandItemHolderNode)tree.peek()).add(n);
      break;
      }
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void ifblock() throws ParseException {
    jj_consume_token(IF);
tree.push(new IfNode());
    jj_consume_token(LPAR);
    block();
Node bnode=tree.pop();
        IfNode ifnode=(IfNode)tree.peek();
        ifnode.conditionNode=bnode;
    jj_consume_token(RPAR);
    jj_consume_token(LACC);
    block();
    jj_consume_token(RACC);
Node bnode1=tree.pop();
        IfNode ifnode1=(IfNode)tree.peek();
        ifnode1.thenNode=bnode1;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ELSE:{
      jj_consume_token(ELSE);
      jj_consume_token(LACC);
      block();
      jj_consume_token(RACC);
Node bnode2=tree.pop();
            IfNode ifnode2=(IfNode)tree.peek();
            ifnode2.thenNode=bnode2;
      break;
      }
    default:
      jj_la1[9] = jj_gen;
      ;
    }
  }

  final public void whileblock() throws ParseException {
    jj_consume_token(WHILE);
tree.push(new WhileNode());
    jj_consume_token(LPAR);
    block();
Node bnode=tree.pop();
            WhileNode ifnode=(WhileNode)tree.peek();
            ifnode.conditionNode=bnode;
    jj_consume_token(RPAR);
    jj_consume_token(LACC);
    block();
    jj_consume_token(RACC);
Node bnode1=tree.pop();
            WhileNode ifnode1=(WhileNode)tree.peek();
            ifnode1.whileNode=bnode1;
  }

  final public void gotoblock() throws ParseException {
    jj_consume_token(GOTO);
    jj_consume_token(ITEM_NAME);
tree.push(new GotoNode(token));
  }

  final public void labelblock() throws ParseException {
    jj_consume_token(LABEL);
    jj_consume_token(ITEM_NAME);
tree.push(new LabelNode(token));
  }

  final public void anonymousblock() throws ParseException {
    jj_consume_token(LACC);
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case NEWLINE:{
        ;
        break;
        }
      default:
        jj_la1[10] = jj_gen;
        break label_5;
      }
      jj_consume_token(NEWLINE);
    }
    block();
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case NEWLINE:{
        ;
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        break label_6;
      }
      jj_consume_token(NEWLINE);
    }
    jj_consume_token(RACC);
Node n=(Node)tree.pop();
                if(n instanceof CommandItemNode){
                    CommandItemHolderNode h=new CommandItemHolderNode();
                    h.add((CommandItemNode)n);
                    ((CommandNode)tree.peek()).add(h);
                }else{
                    CommandItemHolderNode h=new CommandItemHolderNode();
                    h.add(new CommanBlockItemNode(n));
                    ((CommandNode)tree.peek()).add(h);
                }
  }

  final public void parblock() throws ParseException {
    jj_consume_token(LPAR);
    label_7:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case NEWLINE:{
        ;
        break;
        }
      default:
        jj_la1[12] = jj_gen;
        break label_7;
      }
      jj_consume_token(NEWLINE);
    }
    block();
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case NEWLINE:{
        ;
        break;
        }
      default:
        jj_la1[13] = jj_gen;
        break label_8;
      }
      jj_consume_token(NEWLINE);
    }
    jj_consume_token(RPAR);

  }

  final public void simpleblock() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IF:
    case WHILE:
    case LACC:
    case LPAR:
    case DLPAR:
    case DLPAR2:
    case ANTI_QUOTE:
    case ITEM_STRING_DBL:
    case ITEM_STRING_SMP:
    case ITEM_VAR:
    case ITEM_NAME:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DLPAR:
      case DLPAR2:
      case ANTI_QUOTE:
      case ITEM_STRING_DBL:
      case ITEM_STRING_SMP:
      case ITEM_VAR:
      case ITEM_NAME:{
        command();
        break;
        }
      case IF:{
        ifblock();
        break;
        }
      case LPAR:{
        parblock();
        break;
        }
      case WHILE:{
        whileblock();
        break;
        }
      case LACC:{
        anonymousblock();
        break;
        }
      default:
        jj_la1[14] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OP_AND:{
        jj_consume_token(OP_AND);
CommandNode c=(CommandNode)tree.peek();
    c.nowait=true;
        break;
        }
      default:
        jj_la1[15] = jj_gen;
        ;
      }
      break;
      }
    case GOTO:{
      gotoblock();
      break;
      }
    case LABEL:{
      labelblock();
      break;
      }
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void simpleblock_no_anti_quote() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case IF:
    case WHILE:
    case LACC:
    case LPAR:
    case DLPAR:
    case DLPAR2:
    case ITEM_STRING_DBL:
    case ITEM_STRING_SMP:
    case ITEM_VAR:
    case ITEM_NAME:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DLPAR:
      case DLPAR2:
      case ITEM_STRING_DBL:
      case ITEM_STRING_SMP:
      case ITEM_VAR:
      case ITEM_NAME:{
        command_no_anti_quote();
        break;
        }
      case IF:{
        ifblock();
        break;
        }
      case LPAR:{
        parblock();
        break;
        }
      case WHILE:{
        whileblock();
        break;
        }
      case LACC:{
        anonymousblock();
        break;
        }
      default:
        jj_la1[17] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OP_AND:{
        jj_consume_token(OP_AND);
CommandNode c=(CommandNode)tree.peek();
    c.nowait=true;
        break;
        }
      default:
        jj_la1[18] = jj_gen;
        ;
      }
      break;
      }
    case GOTO:{
      gotoblock();
      break;
      }
    case LABEL:{
      labelblock();
      break;
      }
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void orAndBlockExpr() throws ParseException {
    simpleblock();
    label_9:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACES:
      case OP_AND2:
      case OP_VDASH2:{
        ;
        break;
        }
      default:
        jj_la1[20] = jj_gen;
        break label_9;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACES:{
        jj_consume_token(SPACES);
        break;
        }
      default:
        jj_la1[21] = jj_gen;
        ;
      }
      binop_or_and();
      label_10:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:
        case NEWLINE:{
          ;
          break;
          }
        default:
          jj_la1[22] = jj_gen;
          break label_10;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:{
          jj_consume_token(SPACES);
          break;
          }
        case NEWLINE:{
          jj_consume_token(NEWLINE);
          break;
          }
        default:
          jj_la1[23] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      simpleblock();
{
                                                                 Node cmd2=tree.pop();
                                                                 TokenNode op=(TokenNode)tree.pop();
                                                                 Node cmd1=tree.pop();
                                                                 tree.push(new BinoOp(op.getToken().image,cmd1,cmd2));
                                                             }
    }
  }

  final public void redirectBlockExpr() throws ParseException {
    orAndBlockExpr();
    label_11:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case RED_OUT:{
        ;
        break;
        }
      default:
        jj_la1[24] = jj_gen;
        break label_11;
      }
      binop_redirect();
      label_12:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:
        case NEWLINE:{
          ;
          break;
          }
        default:
          jj_la1[25] = jj_gen;
          break label_12;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:{
          jj_consume_token(SPACES);
          break;
          }
        case NEWLINE:{
          jj_consume_token(NEWLINE);
          break;
          }
        default:
          jj_la1[26] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      orAndBlockExpr();
{
                                                                 Node cmd2=tree.pop();
                                                                 TokenNode op=(TokenNode)tree.pop();
                                                                 Node cmd1=tree.pop();
                                                                 tree.push(new BinoOp(op.getToken().image,cmd1,cmd2));
                                                             }
    }
  }

  final public void pipeBlockExpr() throws ParseException {
    redirectBlockExpr();
    label_13:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OP_VDASH:
      case OP_VDASH_AND:{
        ;
        break;
        }
      default:
        jj_la1[27] = jj_gen;
        break label_13;
      }
      binop_pipe();
      label_14:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:
        case NEWLINE:{
          ;
          break;
          }
        default:
          jj_la1[28] = jj_gen;
          break label_14;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:{
          jj_consume_token(SPACES);
          break;
          }
        case NEWLINE:{
          jj_consume_token(NEWLINE);
          break;
          }
        default:
          jj_la1[29] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      redirectBlockExpr();
{
                                                                 Node cmd2=tree.pop();
                                                                 TokenNode op=(TokenNode)tree.pop();
                                                                 Node cmd1=tree.pop();
                                                                 tree.push(new BinoOp(op.getToken().image,cmd1,cmd2));
                                                             }
    }
  }

  final public void blockExpr() throws ParseException {
    pipeBlockExpr();
    label_15:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OP_COMMA:{
        ;
        break;
        }
      default:
        jj_la1[30] = jj_gen;
        break label_15;
      }
      binop_comma();
      label_16:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:
        case NEWLINE:{
          ;
          break;
          }
        default:
          jj_la1[31] = jj_gen;
          break label_16;
        }
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case SPACES:{
          jj_consume_token(SPACES);
          break;
          }
        case NEWLINE:{
          jj_consume_token(NEWLINE);
          break;
          }
        default:
          jj_la1[32] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
      }
      pipeBlockExpr();
{
                                    Node cmd2=tree.pop();
                                    TokenNode op=(TokenNode)tree.pop();
                                    Node cmd1=tree.pop();
                                    tree.push(new BinoOp(op.getToken().image,cmd1,cmd2));
                                }
    }
  }

  final public void blockexpr_no_anti_quote() throws ParseException {
    simpleblock_no_anti_quote();
    label_17:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OP_THEN:
      case OP_AT:
      case OP_AT2:
      case OP_LET:
      case OP_GT:
      case OP_GT2:
      case OP_GET:
      case RED_IN:
      case RED_OUT_APP:
      case RED_ERR_APP:
      case RED_OUT:
      case RED_ERR:{
        ;
        break;
        }
      default:
        jj_la1[33] = jj_gen;
        break label_17;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OP_THEN:
      case OP_AT:
      case OP_AT2:
      case OP_LET:
      case OP_GT:
      case OP_GT2:
      case OP_GET:{
        binop();
        label_18:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case NEWLINE:{
            ;
            break;
            }
          default:
            jj_la1[34] = jj_gen;
            break label_18;
          }
          jj_consume_token(NEWLINE);
        }
        simpleblock_no_anti_quote();
{
                                    Node cmd2=tree.pop();
                                    TokenNode op=(TokenNode)tree.pop();
                                    Node cmd1=tree.pop();
                                    tree.push(new BinoOp(op.getToken().image,cmd1,cmd2));
                                }
        break;
        }
      case RED_IN:
      case RED_OUT_APP:
      case RED_ERR_APP:
      case RED_OUT:
      case RED_ERR:{
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case RED_IN:{
          jj_consume_token(RED_IN);
          break;
          }
        case RED_OUT:{
          jj_consume_token(RED_OUT);
          break;
          }
        case RED_ERR:{
          jj_consume_token(RED_ERR);
          break;
          }
        case RED_OUT_APP:{
          jj_consume_token(RED_OUT_APP);
          break;
          }
        case RED_ERR_APP:{
          jj_consume_token(RED_ERR_APP);
          break;
          }
        default:
          jj_la1[35] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
tree.push(new TokenNode(token));
                            //System.out.println("commandRedirect : "+token.image);

        label_19:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
          case NEWLINE:{
            ;
            break;
            }
          default:
            jj_la1[36] = jj_gen;
            break label_19;
          }
          jj_consume_token(NEWLINE);
        }
        simpleblock();
{
                                    Node cmd2=tree.pop();
                                    TokenNode op=(TokenNode)tree.pop();
                                    Node cmd1=tree.pop();
                                    tree.push(new BinoOp(op.getToken().image,cmd1,cmd2));
                                }
        break;
        }
      default:
        jj_la1[37] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  }

  final public void block() throws ParseException {
    blockExpr();
    label_20:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACES:
      case OP_COMMA:
      case NEWLINE:{
        ;
        break;
        }
      default:
        jj_la1[38] = jj_gen;
        break label_20;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACES:{
        jj_consume_token(SPACES);
        break;
        }
      default:
        jj_la1[39] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OP_COMMA:{
        jj_consume_token(OP_COMMA);
        break;
        }
      case NEWLINE:{
        jj_consume_token(NEWLINE);
        break;
        }
      default:
        jj_la1[40] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACES:{
        jj_consume_token(SPACES);
        break;
        }
      default:
        jj_la1[41] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IF:
      case WHILE:
      case GOTO:
      case LABEL:
      case LACC:
      case LPAR:
      case DLPAR:
      case DLPAR2:
      case ANTI_QUOTE:
      case ITEM_STRING_DBL:
      case ITEM_STRING_SMP:
      case ITEM_VAR:
      case ITEM_NAME:{
        blockExpr();
{
                                    Node cmd2=tree.pop();
                                    Node cmd1=tree.pop();
                                    tree.push(new BinoOp(";",cmd1,cmd2));
                                }
        break;
        }
      default:
        jj_la1[42] = jj_gen;
        ;
      }
    }

  }

  final public void block_no_anti_quote() throws ParseException {
    blockexpr_no_anti_quote();
    label_21:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case OP_COMMA:{
        ;
        break;
        }
      default:
        jj_la1[43] = jj_gen;
        break label_21;
      }
      jj_consume_token(OP_COMMA);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case IF:
      case WHILE:
      case GOTO:
      case LABEL:
      case LACC:
      case LPAR:
      case DLPAR:
      case DLPAR2:
      case ITEM_STRING_DBL:
      case ITEM_STRING_SMP:
      case ITEM_VAR:
      case ITEM_NAME:{
        blockexpr_no_anti_quote();
{
                                    Node cmd2=tree.pop();
                                    Node cmd1=tree.pop();
                                    tree.push(new BinoOp(";",cmd1,cmd2));
                                }
        break;
        }
      default:
        jj_la1[44] = jj_gen;
        ;
      }
    }

  }

  final public void command() throws ParseException {
tree.push(new CommandNode());
    commandItem();
    label_22:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case SPACES:{
        ;
        break;
        }
      default:
        jj_la1[45] = jj_gen;
        break label_22;
      }
      jj_consume_token(SPACES);
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DLPAR:
      case DLPAR2:
      case ANTI_QUOTE:
      case ITEM_STRING_DBL:
      case ITEM_STRING_SMP:
      case ITEM_VAR:
      case ITEM_NAME:{
        commandItem();
        break;
        }
      default:
        jj_la1[46] = jj_gen;
        ;
      }
    }
//System.out.println("readAll..."+tree.peek());
                tree.peek();
  }

  final public void command_no_anti_quote() throws ParseException {
tree.push(new CommandNode());
    label_23:
    while (true) {
      commandItem_no_anti_quote();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case DLPAR:
      case DLPAR2:
      case ITEM_STRING_DBL:
      case ITEM_STRING_SMP:
      case ITEM_VAR:
      case ITEM_NAME:{
        ;
        break;
        }
      default:
        jj_la1[47] = jj_gen;
        break label_23;
      }
    }
tree.peek();
  }

//void commandRedirect() :
//{}
//{
//  ((<RED_IN>|<RED_OUT>|<RED_ERR>|<RED_OUT_APP>|<RED_ERR_APP>)
//            {
//                tree.push(new TokenNode(token));
//                System.out.println("commandRedirect : "+token.image);
//            }
//  (
//  (<ITEM_NAME> )
//            {
//		    {
//		       TokenNode red=(TokenNode)tree.pop();
//		       CommandNode cmd=(CommandNode)tree.peek();
//		       cmd.redirectItems.add(new CommandRedirectItemNode(red,new ItemNode(token)));
//		    }
//            }
// | (<ITEM_STRING_DBL>)
//            {
//		    {
//		       TokenNode red=(TokenNode)tree.pop();
//		       CommandNode cmd=(CommandNode)tree.peek();
//		       cmd.redirectItems.add(new CommandRedirectItemNode(red,new StringDoubleCotedNode(token)));
//		    }
//            }
// | (<ITEM_STRING_SMP>)
//            {
//		    {
//		       TokenNode red=(TokenNode)tree.pop();
//		       CommandNode cmd=(CommandNode)tree.peek();
//		       cmd.redirectItems.add(new CommandRedirectItemNode(red,new StringSimpleCotedNode(token)));
//		    }
//            }
// | (<ITEM_STRING_ANTI>)
//            {
//		    {
//		       TokenNode red=(TokenNode)tree.pop();
//		       CommandNode cmd=(CommandNode)tree.peek();
//		       cmd.redirectItems.add(new CommandRedirectItemNode(red,new StringAntiCotedNode(token)));
//		    }
//            }
// | (<ITEM_VAR>)
//            {
//		    {
//		       TokenNode red=(TokenNode)tree.pop();
//		       CommandNode cmd=(CommandNode)tree.peek();
//		       cmd.redirectItems.add(new CommandRedirectItemNode(red,new VarDollarNode(token)));
//		    }
//            }
//
//  )
//  )
//
//}
  final public 
void itemstring() throws ParseException {Token tt=null;Node b;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case ANTI_QUOTE:{
      jj_consume_token(ANTI_QUOTE);
tt=token;
      block_no_anti_quote();
      jj_consume_token(ANTI_QUOTE);
b=tree.pop();
        tree.push(new StringAntiCotedNode(tt,b));
      break;
      }
    case DLPAR:{
      jj_consume_token(DLPAR);
tt=token;
      block();
      jj_consume_token(RPAR);
b=tree.pop();
        tree.push(new StringAntiCotedNode(tt,b));
      break;
      }
    case DLPAR2:{
      jj_consume_token(DLPAR2);
tt=token;
      block();
      jj_consume_token(RPAR2);
b=tree.pop();
        tree.push(new StringAntiCotedNode(tt,b));
      break;
      }
    default:
      jj_la1[48] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void itemstring_no_anti_quote() throws ParseException {Token tt=null;Node b;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case DLPAR:{
      jj_consume_token(DLPAR);
tt=token;
      block();
      jj_consume_token(RPAR);
b=tree.pop();
        tree.push(new StringAntiCotedNode(tt,b));
      break;
      }
    case DLPAR2:{
      jj_consume_token(DLPAR2);
tt=token;
      block();
      jj_consume_token(RPAR2);
b=tree.pop();
        tree.push(new StringAntiCotedNode(tt,b));
      break;
      }
    default:
      jj_la1[49] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void binop() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case OP_THEN:{
      jj_consume_token(OP_THEN);
      break;
      }
    case OP_AT:{
      jj_consume_token(OP_AT);
      break;
      }
    case OP_AT2:{
      jj_consume_token(OP_AT2);
      break;
      }
    case OP_LET:{
      jj_consume_token(OP_LET);
      break;
      }
    case OP_GT:{
      jj_consume_token(OP_GT);
      break;
      }
    case OP_GT2:{
      jj_consume_token(OP_GT2);
      break;
      }
    case OP_GET:{
      jj_consume_token(OP_GET);
      break;
      }
    default:
      jj_la1[50] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
tree.push(new TokenNode(token));
  }

  final public void binop_pipe() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case OP_VDASH:{
      jj_consume_token(OP_VDASH);
      break;
      }
    case OP_VDASH_AND:{
      jj_consume_token(OP_VDASH_AND);
      break;
      }
    default:
      jj_la1[51] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
tree.push(new TokenNode(token));
  }

  final public void binop_comma() throws ParseException {
    jj_consume_token(OP_COMMA);
tree.push(new TokenNode(token));
  }

  final public void binop_or_and() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case OP_AND2:{
      jj_consume_token(OP_AND2);
      break;
      }
    case OP_VDASH2:{
      jj_consume_token(OP_VDASH2);
      break;
      }
    default:
      jj_la1[52] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
tree.push(new TokenNode(token));
  }

  final public void binop_redirect() throws ParseException {
    jj_consume_token(RED_OUT);
tree.push(new TokenNode(token));
  }

  /** Generated Token Manager. */
  public JShellParserImplTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[53];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x2,0x2,0x2,0x2,0x6a0001ab,0x60000000,0x60000000,0x60000000,0x60000000,0x10,0x0,0x0,0x0,0x0,0x6a000028,0x800,0x6a0001a8,0x6a000028,0x800,0x6a0001a8,0x41002,0x2,0x2,0x2,0x0,0x2,0x2,0x30000,0x2,0x2,0x400,0x2,0x2,0x1e0e000,0x0,0x0,0x0,0x1e0e000,0x402,0x2,0x400,0x2,0x6a0001a8,0x400,0x6a0001a8,0x2,0x60000000,0x60000000,0x60000000,0x60000000,0x1e0e000,0x30000,0x41000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x40,0x40,0x40,0x40,0xfc0,0xf80,0xf80,0xf00,0xf00,0x0,0x40,0x40,0x40,0x40,0xf80,0x0,0xf80,0xf00,0x0,0xf00,0x0,0x0,0x40,0x40,0x10,0x40,0x40,0x0,0x40,0x40,0x0,0x40,0x40,0x3e,0x40,0x3e,0x40,0x3e,0x40,0x0,0x40,0x0,0xf80,0x0,0xf00,0x0,0xf80,0xf00,0x80,0x0,0x0,0x0,0x0,};
   }

  /** Constructor with InputStream. */
  public JShellParserImpl(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public JShellParserImpl(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new JShellParserImplTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 53; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 53; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public JShellParserImpl(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new JShellParserImplTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 53; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 53; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public JShellParserImpl(JShellParserImplTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 53; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(JShellParserImplTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 53; i++) jj_la1[i] = -1;
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

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[45];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 53; i++) {
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
    for (int i = 0; i < 45; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
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
