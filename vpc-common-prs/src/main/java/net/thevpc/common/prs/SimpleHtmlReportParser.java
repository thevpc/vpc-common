/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Description: <start><end>
 *
 * <br>
 *
 * Copyright [2020] [thevpc]
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License.
 * <br>
 * ====================================================================
*/
package net.thevpc.common.prs;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class SimpleHtmlReportParser {
    private abstract class Part {
        abstract void eval(PrintStream out, Stack<Object> instances);
    }

    private class Block extends Part {
        List<Part> parts = new ArrayList<Part>();

        void eval(PrintStream out, Stack<Object> instances) {
            for (Part part1 : parts) {
                part1.eval(out, instances);
            }
        }
    }

    private class Text extends Part {
        String value;

        private Text(String value) {
            this.value = value;
        }

        void eval(PrintStream out, Stack<Object> instances) {
            out.print(value);
        }
    }

    private class Var extends Part {
        String name;

        private Var(String name) {
            this.name = name;
        }

        void eval(PrintStream out, Stack<Object> instances) {
            try {
                Object obj = instances.peek();
                Object o = null;
                if (obj != null) {
                    if (obj instanceof Map) {
                        o = ((Map)obj).get(name);
                    } else {
                        o = obj.getClass().getDeclaredField(name).get(obj);
                    }
                }
                if (o != null) {
                    if (o instanceof Double) {
                        NumberFormat f = DecimalFormat.getInstance();
                        out.print(f.format(o));
                    } else {
                        out.print(o);
                    }
                }
            } catch (Exception e) {
                //
            }
//            for (int i = instances.size() - 1; i >= 0; i--) {
//                try {
//                    Object obj=instances.get(i);
//                    Object o = obj.getClass().getDeclaredField(name).get(obj);
//                    if (o instanceof Double) {
//                        DecimalFormat f = UserFormats.getDecimalFormat();
//                        out.print(f.format(o));
//                    } else {
//                        out.print(o);
//                    }
//                    break;
//                } catch (Exception e) {
//                    //
//                }
//            }
        }
    }

    private class Iteration extends Part {
        String name;
        Block block = new Block();

        private Iteration(String name) {
            this.name = name;
        }

        void eval(PrintStream out, Stack<Object> instances) {
            Object obj = instances.peek();
            Object o;
            try {
                o = obj.getClass().getDeclaredField(name).get(obj);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
            if (o.getClass().isArray()) {
                int x = Array.getLength(o);
                for (int i = 0; i < x; i++) {
                    instances.push(Array.get(o, i));
                    block.eval(out, instances);
                    instances.pop();
                }
            } else {
                throw new IllegalArgumentException("not an array");
            }
        }
    }

    //    private class VisibleIf extends Part {
//        String name;
//        Block block = new Block();
//
//        private VisibleIf(String name) {
//            this.name = name;
//        }
//
//        void eval(PrintStream out, Stack<Object> instances) {
//            try {
//                Object obj=instances.peek();
//                Object o = obj.getClass().getDeclaredField(name).get(obj);
//                if (((o instanceof Boolean) && ((Boolean)o)) || (!(o instanceof Boolean) && o!=null)) {
//                    block.eval(out,instances);
//                }
//            } catch (Exception e) {
//                //
//            }
//        }
//    }
//
    public String evalString(Object instance, String in) throws IOException {
        return evalString(instance, new StringReader(in));
    }

    public String evalString(Object instance, Reader in) throws IOException {
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(s);
        eval(instance, in, ps);
        ps.flush();
        s.flush();
        return new String(s.toByteArray());
    }

    public void eval(Object instance, Reader in, PrintStream out) throws IOException {
        Stack<Object> instances = new Stack<Object>();
        instances.push(instance);
        parse(in).eval(out, instances);
    }

    public Block parse(Reader r) throws IOException {
        Stack<Block> stack = new Stack<Block>();
        stack.push(new Block());
        StringBuilder text = new StringBuilder();
        while (true) {
            int x = r.read();
            if (x == -1) {
                break;
            }
            char c = (char) x;
            switch (c) {
                case '<': {
                    x = r.read();
                    if (x == -1) {
                        break;
                    } else if ('%' == x) {
                        if (text.length() > 0) {
                            stack.peek().parts.add(new Text(text.toString()));
                            text.delete(0, text.length());
                        }
                        StringBuilder sb = new StringBuilder();
                        while2:
                        while (true) {
                            x = r.read();
                            c = (char) x;
                            if (c == '%') {
                                x = r.read();
                                c = (char) x;
                                if (c == '>') {
                                    break while2;
                                } else {
                                    sb.append("%").append(c);
                                }
                            } else {
                                sb.append(c);
                            }
                        }
                        if (sb.charAt(0) == '=') {
                            sb.delete(0, 1);
                            Block b = stack.peek();
                            b.parts.add(new Var(sb.toString().trim()));
                        } else {
                            String cmd = sb.toString().trim();
                            if (cmd.startsWith("foreach(")) {
                                Block b = stack.peek();
                                String iterName = cmd.substring("foreach(".length(), cmd.length() - 1).trim();
                                Iteration e = new Iteration(iterName);
                                b.parts.add(e);
                                stack.push(e.block);
                            } else if (cmd.equals("endforeach()")) {
                                stack.pop();
                            }
                        }
                    } else {
                        text.append(c);
                        text.append((char) x);
                    }
                    break;
                }
                default: {
                    text.append(c);
                }
            }
        }
        if (text.length() > 0) {
            stack.peek().parts.add(new Text(text.toString()));
            text.delete(0, text.length());
        }
        return stack.pop();
    }
}
