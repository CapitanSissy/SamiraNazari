package core.samira.searchable;

//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";


//#line 2 "policy_lang.y"

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
//#line 21 "Parser.java"


public class Parser {

  //#### end semantic value section ####
  public final static short ATTR = 257;
  public final static short NUM = 258;
  public final static short OR = 259;
  public final static short AND = 260;
  public final static short OF = 261;
  public final static short YYERRCODE = 256;
  //########## STATE STACK ##########
  final static int YYSTACKSIZE = 500;  //maximum stack size
  final static short[] yylhs = {-1,
    0, 1, 1, 1, 1, 1, 2, 2,
  };
  final static short[] yylen = {2,
    1, 1, 3, 3, 5, 3, 1, 3,
  };
  final static short[] yydefred = {0,
    2, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    6, 0, 4, 0, 0, 5, 0, 0,
  };
  final static short[] yydgoto = {4,
    5, 15,
  };
  final static short[] yysindex = {-37,
    0, -244, -37, 0, -255, -34, -41, -37, -37, -37,
    0, -242, 0, -255, -31, 0, -37, -255,
  };
  final static short[] yyrindex = {0,
    0, 0, 0, 0, 19, 0, 0, 0, 0, 0,
    0, 1, 0, -30, 0, 0, 0, -29,
  };
  final static short[] yygindex = {0,
    -1, 0,
  };
  final static int YYTABLESIZE = 260;
  final static short YYFINAL = 4;


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java
  final static short YYMAXTOKEN = 261;
  final static String[] yyname = {
    "end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, "'('", "')'", null, null, "','",
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
    null, null, null, null, "ATTR", "NUM", "OR", "AND", "OF",
  };
  final static String[] yyrule = {
    "$accept : result",
    "result : policy",
    "policy : ATTR",
    "policy : policy OR policy",
    "policy : policy AND policy",
    "policy : NUM OF '(' arg_list ')'",
    "policy : '(' policy ')'",
    "arg_list : policy",
    "arg_list : arg_list ',' policy",
  };
  static short[] yytable;
  static short[] yycheck;

  static {
    yytable();
  }

  static {
    yycheck();
  }

  boolean yydebug;        //do I want debug output?
  int yynerrs;            //number of errors so far
  int yyerrflag;          //was there an error?
  int yychar;             //the current working character
  int[] statestk = new int[YYSTACKSIZE]; //state stack
  int stateptr;
  int stateptrmax;                     //highest index of stackptr
  int statemax;                        //state when highest index reached
  String yytext;//user variable to return contextual strings
  ParserVal yyval; //used to return semantic vals from action routines
  ParserVal yylval;//the 'lval' (result) I got from yylex()
  ParserVal[] valstk;
  int valptr;
  StringTokenizer st;
  //The following are now global, to aid in error reporting
  int yyn;       //next next thing to do
  int yym;       //
  int yystate;   //current parsing state from state table
  String yys;    //current token string
  //#line 30 "policy_lang.y"
  private Policy res;

  /**
   * Default constructor.  Turn off with -Jnoconstruct .
   */
  public Parser() {
    //nothing to do
  }

  /**
   * Create a parser, setting the debug to true or false.
   *
   * @param debugMe true for debugging, false for no debug.
   */
  public Parser(boolean debugMe) {
    yydebug = debugMe;
  }

  static void yytable() {
    yytable = new short[]{11,
      3, 7, 3, 8, 9, 10, 12, 13, 14, 16,
      7, 8, 17, 7, 8, 18, 6, 9, 1, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 3, 0, 0, 3, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 8, 9, 1,
      2, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
    };
  }

  static void yycheck() {
    yycheck = new short[]{41,
      0, 3, 40, 259, 260, 40, 8, 9, 10, 41,
      41, 41, 44, 44, 44, 17, 261, 260, 0, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, 41, -1, -1, 44, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, 259, 260, 257,
      258, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, 259,
    };
  }

  public static void main(String[] args) {
    String input = "kkk & ( 2 of ( abc , bcd , cde ) )";
    Parser parser = new Parser(true);
    Policy p = parser.parse(input);
//    System.out.println(p.k);
  }

  //########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
  void debug(String msg) {
    if (yydebug)
      System.out.println(msg);
  }

  //###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
  final void state_push(int state) {
    try {
      stateptr++;
      statestk[stateptr] = state;
    } catch (ArrayIndexOutOfBoundsException e) {
      int oldsize = statestk.length;
      int newsize = oldsize * 2;
      int[] newstack = new int[newsize];
      System.arraycopy(statestk, 0, newstack, 0, oldsize);
      statestk = newstack;
      statestk[stateptr] = state;
    }
  }

  final int state_pop() {
    return statestk[stateptr--];
  }

  final void state_drop(int cnt) {
    stateptr -= cnt;
  }

  final int state_peek(int relative) {
    return statestk[stateptr - relative];
  }

  //###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
  final boolean init_stacks() {
    stateptr = -1;
    val_init();
    return true;
  }

  //###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
  void dump_stacks(int count) {
    int i;
    System.out.println("=index==state====value=     s:" + stateptr + "  v:" + valptr);
    for (i = 0; i < count; i++)
      System.out.println(" " + i + "    " + statestk[i] + "      " + valstk[i]);
    System.out.println("======================");
  }

  //###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
  void val_init() {
    valstk = new ParserVal[YYSTACKSIZE];
    yyval = new ParserVal();
    yylval = new ParserVal();
    valptr = -1;
  }

  void val_push(ParserVal val) {
    if (valptr >= YYSTACKSIZE)
      return;
    valstk[++valptr] = val;
  }

  ParserVal val_pop() {
    if (valptr < 0)
      return new ParserVal();
    return valstk[valptr--];
  }

  void val_drop(int cnt) {
    int ptr;
    ptr = valptr - cnt;
    if (ptr < 0)
      return;
    valptr = ptr;
  }

  ParserVal val_peek(int relative) {
    int ptr;
    ptr = valptr - relative;
    if (ptr < 0)
      return new ParserVal();
    return valstk[ptr];
  }

  final ParserVal dup_yyval(ParserVal val) {
    ParserVal dup = new ParserVal();
    dup.ival = val.ival;
    dup.dval = val.dval;
    dup.sval = val.sval;
    dup.obj = val.obj;
    return dup;
  }

  private String normalize(String input) {
    input = input.replaceAll("\n", "");
    input = input.replaceAll(",", " , ");
    input = input.replaceAll("\\(", " ( ");
    input = input.replaceAll("\\)", " ) ");

    return input;
  }

  public Policy parse(String input) {
    input = normalize(input);
    this.st = new StringTokenizer(input, " \t\r\f");
    yyparse();
    return this.res;
  }

  private int yylex() {
    String s;
    int tok;
    if (!st.hasMoreTokens()) {
      return 0;
    }
    s = st.nextToken();
    if (s.equals("(") || s.equals(")") || s.equals(",")) {
      tok = s.charAt(0);
      yylval = new ParserVal(s);
    } else if (s.equals("&") || s.equalsIgnoreCase("and")) {
      tok = AND;
      yylval = new ParserVal(s);
    } else if (s.equals("|") || s.equalsIgnoreCase("or")) {
      tok = OR;
      yylval = new ParserVal(s);
    } else if (s.equalsIgnoreCase("of")) {
      tok = OF;
      yylval = new ParserVal(s);
    } else {
      boolean isNum = true;
      for (char c : s.toCharArray()) {
        if (!Character.isDigit(c)) {
          isNum = false;
          break;
        }
      }
      if (isNum) {
        tok = NUM;
        yylval = new ParserVal(Integer.parseInt(s));
      } else {
        tok = ATTR;
        yylval = new ParserVal(s);
      }
    }

    return tok;
  }

  public void yyerror(String error) {
    System.err.println("Error:" + error);
  }

  Policy leaf_policy(String attr) {
    Policy p = new Policy();
    p.attr = attr;
    p.k = 1;
    return p;
  }

  Policy kof2_policy(int k, Policy l, Policy r) {
    Policy p = new Policy();
    p.k = k;
    p.children = new Policy[2];
    p.children[0] = l;
    p.children[1] = r;
    return p;
  }
//## end of method parse() ######################################


//## run() --- for Thread #######################################

  Policy kof_policy(int k, List<Policy> list) {
    Policy p = new Policy();
    p.k = k;
    p.children = new Policy[list.size()];
    list.toArray(p.children);
    return p;
  }
//## end of method run() ########################################


//## Constructors ###############################################

  //#line 301 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
  void yylexdebug(int state, int ch) {
    String s = null;
    if (ch < 0) ch = 0;
    if (ch <= YYMAXTOKEN) //check index bounds
      s = yyname[ch];    //now get it
    if (s == null)
      s = "illegal-symbol";
    debug("state " + state + ", reading " + ch + " (" + s + ")");
  }

  //###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
  @SuppressWarnings("unchecked")
  int yyparse() {
    boolean doaction;
    init_stacks();
    yynerrs = 0;
    yyerrflag = 0;
    yychar = -1;          //impossible char forces a read
    yystate = 0;            //initial state
    state_push(yystate);  //save it
    val_push(yylval);     //save empty value
    while (true) //until parsing is done, either correctly, or w/error
    {
      doaction = true;
      if (yydebug) debug("loop");
      //#### NEXT ACTION (from reduction table)
      for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {
        if (yydebug) debug("yyn:" + yyn + "  state:" + yystate + "  yychar:" + yychar);
        if (yychar < 0)      //we want a char?
        {
          yychar = yylex();  //get next token
          if (yydebug) debug(" next yychar:" + yychar);
          //#### ERROR CHECK ####
          if (yychar < 0)    //it it didn't work/error
          {
            yychar = 0;      //change it to default string (no -1!)
            if (yydebug)
              yylexdebug(yystate, yychar);
          }
        }//yychar<0
        yyn = yysindex[yystate];  //get amount to shift by (shift index)
        if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {
          if (yydebug)
            debug("state " + yystate + ", shifting to state " + yytable[yyn]);
          //#### NEXT STATE ####
          yystate = yytable[yyn];//we are in a new state
          state_push(yystate);   //save it
          val_push(yylval);      //push our lval as the input for next rule
          yychar = -1;           //since we have 'eaten' a token, say we need another
          if (yyerrflag > 0)     //have we recovered an error?
            --yyerrflag;        //give ourselves credit
          doaction = false;        //but don't process yet
          break;   //quit the yyn=0 loop
        }

        yyn = yyrindex[yystate];  //reduce
        if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {   //we reduced!
          if (yydebug) debug("reduce");
          yyn = yytable[yyn];
          doaction = true; //get ready to execute
          break;         //drop down to actions
        } else //ERROR RECOVERY
        {
          if (yyerrflag == 0) {
            yyerror("syntax error");
            yynerrs++;
          }
          if (yyerrflag < 3) //low error count?
          {
            yyerrflag = 3;
            while (true)   //do until break
            {
              if (stateptr < 0)   //check for under & overflow here
              {
                yyerror("stack underflow. aborting...");  //note lower case 's'
                return 1;
              }
              yyn = yysindex[state_peek(0)];
              if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE) {
                if (yydebug)
                  debug("state " + state_peek(0) + ", error recovery shifting to state " + yytable[yyn] + " ");
                yystate = yytable[yyn];
                state_push(yystate);
                val_push(yylval);
                doaction = false;
                break;
              } else {
                if (yydebug)
                  debug("error recovery discarding state " + state_peek(0) + " ");
                if (stateptr < 0)   //check for under & overflow here
                {
                  yyerror("Stack underflow. aborting...");  //capital 'S'
                  return 1;
                }
                state_pop();
                val_pop();
              }
            }
          } else            //discard this token
          {
            if (yychar == 0)
              return 1; //yyabort
            if (yydebug) {
              yys = null;
              if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
              if (yys == null) yys = "illegal-symbol";
              debug("state " + yystate + ", error recovery discards token " + yychar + " (" + yys + ")");
            }
            yychar = -1;  //read another
          }
        }//end error recovery
      }//yyn=0 loop
      if (!doaction)   //any reason not to proceed?
        continue;      //skip action
      yym = yylen[yyn];          //get count of terminals on rhs
      if (yydebug)
        debug("state " + yystate + ", reducing " + yym + " by rule " + yyn + " (" + yyrule[yyn] + ")");
      if (yym > 0)                 //if count of rhs not 'nil'
        yyval = val_peek(yym - 1); //get current semantic value
      yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
      switch (yyn) {
//########## USER-SUPPLIED ACTIONS ##########
        case 1:
//#line 15 "policy_lang.y"
        {
          res = (Policy) val_peek(0).obj;
        }
        break;
        case 2:
//#line 17 "policy_lang.y"
        {
          yyval.obj = leaf_policy(val_peek(0).sval);
        }
        break;
        case 3:
//#line 18 "policy_lang.y"
        {
          yyval.obj = kof2_policy(1, (Policy) val_peek(2).obj, (Policy) val_peek(0).obj);
        }
        break;
        case 4:
//#line 19 "policy_lang.y"
        {
          yyval.obj = kof2_policy(2, (Policy) val_peek(2).obj, (Policy) val_peek(0).obj);
        }
        break;
        case 5:
//#line 20 "policy_lang.y"
        {
          yyval.obj = kof_policy(val_peek(4).ival, (List<Policy>) val_peek(1).obj);
        }
        break;
        case 6:
//#line 21 "policy_lang.y"
        {
          yyval = val_peek(1);
        }
        break;
        case 7:
//#line 23 "policy_lang.y"
        {
          yyval.obj = new ArrayList<Policy>();
          ((List<Policy>) yyval.obj).add((Policy) (val_peek(0).obj));
        }
        break;
        case 8:
//#line 25 "policy_lang.y"
        {
          yyval = val_peek(2);
          ((List<Policy>) yyval.obj).add((Policy) (val_peek(0).obj));
        }
        break;
//#line 484 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
      }//switch
      //#### Now let's reduce... ####
      if (yydebug) debug("reduce");
      state_drop(yym);             //we just reduced yylen states
      yystate = state_peek(0);     //get new state
      val_drop(yym);               //corresponding value drop
      yym = yylhs[yyn];            //select next TERMINAL(on lhs)
      if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
        if (yydebug) debug("After reduction, shifting from state 0 to state " + YYFINAL + "");
        yystate = YYFINAL;         //explicitly say we're done
        state_push(YYFINAL);       //and save it
        val_push(yyval);           //also save the semantic value of parsing
        if (yychar < 0)            //we want another character?
        {
          yychar = yylex();        //get next character
          if (yychar < 0) yychar = 0;  //clean, if necessary
          if (yydebug)
            yylexdebug(yystate, yychar);
        }
        if (yychar == 0)          //Good exit (if lex returns 0 ;-)
          break;                 //quit the loop--all DONE
      }//if yystate
      else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
        yyn = yygindex[yym];      //find out where to go
        if ((yyn != 0) && (yyn += yystate) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
          yystate = yytable[yyn]; //get new state
        else
          yystate = yydgoto[yym]; //else go to new defred
        if (yydebug) debug("after reduction, shifting from state " + state_peek(0) + " to state " + yystate);
        state_push(yystate);     //going again, so push state & val...
        val_push(yyval);         //for next action
      }
    }//main loop
    return 0;//yyaccept!!
  }
//###############################################################

  /**
   * A default run method, used for operating this parser
   * object in the background.  It is intended for extending Thread
   * or implementing Runnable.  Turn off with -Jnorun .
   */
  public void run() {
    yyparse();
  }

}
//################### END OF CLASS ##############################
