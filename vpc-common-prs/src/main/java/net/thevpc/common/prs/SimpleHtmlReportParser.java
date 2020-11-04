/**
 * ====================================================================
 *                        vpc-prs library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
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
