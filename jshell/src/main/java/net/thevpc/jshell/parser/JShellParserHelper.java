package net.thevpc.jshell.parser;

import java.util.Stack;

public class JShellParserHelper {
    public static int readQuotes(char[] chars,int i,StringBuilder v){
        Stack<Character> s=new Stack<Character>();
        s.push(chars[i]);
        int j=0;
        while (i+j < chars.length && !s.isEmpty()) {
            switch (chars[i+j]){
                case '\\':{
                    j++;
                    break;
                }
                case '\"':{
                    if(s.peek().equals('\"')){
                        s.pop();
                    }else {
                        s.push('\"');
                    }
                    break;
                }
                case '\'':{
                    if(s.peek().equals('\'')){
                        s.pop();
                    }else {
                        s.push('\'');
                    }
                    break;
                }
                case '`':{
                    if(s.peek().equals('`')){
                        s.pop();
                    }else {
                        s.push('`');
                    }
                    break;
                }
            }
            v.append(chars[i+j]);
            j++;
        }
        return i;
    }
}
