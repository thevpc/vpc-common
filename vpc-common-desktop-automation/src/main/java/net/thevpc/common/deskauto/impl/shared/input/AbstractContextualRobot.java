/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.deskauto.impl.shared.input;

import net.thevpc.common.deskauto.ContextualRobot;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vpc
 */
public abstract class AbstractContextualRobot implements ContextualRobot {

    public static final int LEFT_BUTTON = InputEvent.BUTTON1_MASK;
    public static final int RIGHT_BUTTON = InputEvent.BUTTON2_MASK;
    private static ContextualRobot robot = new DesktopContextualRobot();
    private static int releaseThinkTime = 300;
    private static int doubleClickThinkTime = 200;
    private static final Set<Integer> modifiersSet = new HashSet<Integer>();

    private static final Map<String, KeyButton> keyButtonsMap = new HashMap<String, KeyButton>();

    static {
        modifiersSet.add(KeyEvent.VK_CONTROL);
        modifiersSet.add(KeyEvent.VK_SHIFT);
        modifiersSet.add(KeyEvent.VK_ALT);
        modifiersSet.add(KeyEvent.VK_ALT_GRAPH);
        modifiersSet.add(KeyEvent.VK_META);
        modifiersSet.add(KeyEvent.VK_WINDOWS);

        for (Field f : KeyEvent.class.getDeclaredFields()) {
            if (f.getName().startsWith("VK_")) {
                try {
                    int vkey = (Integer) f.get(null);
                    String name = f.getName().substring(3).replace("_", " ").toLowerCase();
                    registerKey(new KeyButton(name, vkey, modifiersSet.contains(vkey)));
                } catch (Exception ex) {
                    Logger.getLogger(AbstractContextualRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        registerKey(new KeyButton("ctrl", KeyEvent.VK_CONTROL, true));
        registerKey(new KeyButton("altgr", KeyEvent.VK_ALT_GRAPH, true));
        registerKey(new KeyButton("alphanum", KeyEvent.VK_ALPHANUMERIC, false));
    }

    private static void registerKey(KeyButton b) {
        keyButtonsMap.put(b.name, b);
    }

    private static InputAction[] parseInputActionCommands(String cmd) {
        try {
            List<InputAction> actions = new ArrayList<>();
            List<Integer> pressed = new ArrayList<>();
//            AutoTracker pressed = new AutoTracker();
            StreamTokenizer t = new StreamTokenizer(new StringReader(cmd));
            while (t.nextToken() != StreamTokenizer.TT_EOF) {
                switch (t.ttype) {
                    case StreamTokenizer.TT_EOL: {
                        System.out.println("EOL : " + t.sval);
                        //ignore
                        break;
                    }
                    case StreamTokenizer.TT_WORD: {
                        String s = t.sval.toLowerCase();
                        KeyButton k = keyButtonsMap.get(s);
                        if (k == null) {
                            if (s.equals("release-last")) {
                                pressed.remove(pressed.size() - 1);
                                actions.add(new ReleaseLastAction());
                            } else if (s.equals("release-all")) {
                                pressed.clear();
                                actions.add(new ReleaseAllAction());
                            } else if (s.equals("release")) {
                                pressed.clear();
                                actions.add(new ReleaseAllAction());
                            } else if (s.equals("move")) {
                                int x = 0;
                                int y = 0;
                                if (t.nextToken() == StreamTokenizer.TT_NUMBER) {
                                    if (Math.floor(t.nval) != t.nval) {
                                        throw new IllegalArgumentException("Expected integer x for move");
                                    }
                                    x = (int) Math.floor(t.nval);
                                } else {
                                    throw new IllegalArgumentException("Expected x for move");
                                }
                                if (t.nextToken() == StreamTokenizer.TT_NUMBER) {
                                    if (Math.floor(t.nval) != t.nval) {
                                        throw new IllegalArgumentException("Expected integer y for move");
                                    }
                                    y = (int) Math.floor(t.nval);
                                } else {
                                    throw new IllegalArgumentException("Expected y for move");
                                }
                                actions.add(new MouseMoveAction(x, y));
                            } else if (s.equals("leftclick") || s.equals("lclick") || s.equals("click")) {
                                actions.add(new MouseClickAction("leftclick", LEFT_BUTTON));
                            } else if (s.equals("rightclick") || s.equals("rclick")) {
                                actions.add(new MouseClickAction("rightclick", RIGHT_BUTTON));
                            } else if (s.equals("leftdblclick") || s.equals("ldblclick") || s.equals("dblclick")) {
                                actions.add(new MouseDblClickAction("leftdblclick", LEFT_BUTTON));
                            } else if (s.equals("rightdblclick") || s.equals("rdblclick")) {
                                actions.add(new MouseDblClickAction("rightdblclick", RIGHT_BUTTON));
                            } else if (s.equals("wait")) {
                                actions.add(new DelayAction("Think", releaseThinkTime));
                            } else if (s.equals("waitfor")) {
                                int nextType = t.nextToken();
                                long w = 0;
                                if (nextType == StreamTokenizer.TT_NUMBER) {
                                    if (Math.floor(t.nval) != t.nval) {
                                        throw new IllegalArgumentException("Expected integer waitfor value");
                                    }
                                    w = (long) Math.floor(t.nval);
                                } else if (nextType == StreamTokenizer.TT_WORD) {
                                    if (t.sval.endsWith("ms")) {
                                        double d = Double.parseDouble(t.sval.substring(0, t.sval.length() - 1));
                                        if (Math.floor(d) != d || d <= 0) {
                                            throw new IllegalArgumentException("Expected valid integer waitfor value");
                                        }
                                        w = (long) Math.floor(d);
                                    } else if (t.sval.endsWith("s")) {
                                        double d = Double.parseDouble(t.sval.substring(0, t.sval.length() - 1));
                                        if (Math.floor(d) != d || d <= 0) {
                                            throw new IllegalArgumentException("Expected valid integer waitfor value");
                                        }
                                        w = 1000 * (long) Math.floor(d);
                                    } else {
                                        throw new IllegalArgumentException("Invalid waitfor value " + t.sval);
                                    }
                                    actions.add(new DelayAction("Think", releaseThinkTime));
                                } else {
                                    throw new IllegalArgumentException("Invalid waitfor value " + t);
                                }
                                //
                            }
                        } else {
                            if (k.modifier) {
                                if (pressed.contains(k.vkey)) {
                                    throw new IllegalArgumentException(k.name + " already pressed");
                                }
                                pressed.add(k.vkey);
                                actions.add(new PressKeyboardAction(k.name, k.vkey));
                            } else {
                                actions.add(new TypeKeyboardAction(k.name, k.vkey));
                            }
                        }
                        break;
                    }
                    case '\'': {
                        for (char cc : t.sval.toCharArray()) {
                            for (InputAction ia : parseInputActionText(cc)) {
                                actions.add(ia);
                            }

                        }
                        break;
                    }
                    case ';': {
                        actions.add(new ReleaseAllAction());
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Unexpected token " + t);
                    }
                }
            }
            return actions.toArray(new InputAction[actions.size()]);
        } catch (IOException ex) {
            Logger.getLogger(AbstractContextualRobot.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException(ex);
        }
    }

    private static InputAction[] parseInputActionText(char c) {
        return parseInputActionTextAZERTY(c);
    }

    private static InputAction[] parseInputActionTextAZERTY(char c) {
        if (c >= 'a' && c <= 'z') {
            int p = KeyEvent.getExtendedKeyCodeForChar(Character.toUpperCase(c));
            if (p == KeyEvent.VK_UNDEFINED) {
                throw new IllegalArgumentException("Unsupported " + c);
            }
            return new InputAction[]{new TypeKeyboardAction(String.valueOf(c), p)};
        }else if(c >= 'A' && c <= 'Z'){
            int p = KeyEvent.getExtendedKeyCodeForChar(Character.toUpperCase(c));
            if (p == KeyEvent.VK_UNDEFINED) {
                throw new IllegalArgumentException("Unsupported " + c);
            }
            return new InputAction[]{
                new PressKeyboardAction("shift", KeyEvent.SHIFT_DOWN_MASK),
                new PressKeyboardAction(String.valueOf(c), p),
            };
        }
        int p = KeyEvent.getExtendedKeyCodeForChar(c);
        if (p == KeyEvent.VK_UNDEFINED) {
            throw new IllegalArgumentException("Unsupported " + c);
        }
        return new InputAction[]{new PressKeyboardAction(String.valueOf(c), p)};
    }

    public void moveMouse(Point point) {
        robot.mouseMove(point.x, point.y);
    }

    @Override
    public void think() {
        delay(releaseThinkTime);
    }

    @Override
    public void leftClick() {
        click(LEFT_BUTTON);
    }

    @Override
    public void leftDblClick() {
        dblClick(LEFT_BUTTON);
    }

    @Override
    public void rightClick() {
        click(RIGHT_BUTTON);
    }

    @Override
    public void rightDblClick() {
        dblClick(RIGHT_BUTTON);
    }

    public void click(int button) {
        robot.mousePress(button);
        delay(releaseThinkTime);
        robot.mouseRelease(button);
    }

    public void dblClick(int button) {
        robot.mousePress(button);
        delay(releaseThinkTime);
        robot.mouseRelease(button);
        delay(doubleClickThinkTime);
        robot.mousePress(button);
        delay(releaseThinkTime);
        robot.mouseRelease(button);
    }

    public void click(Point point, int button) {
        moveMouse(point);
        click(button);
    }

    public void dblClick(Point point, int button) {
        moveMouse(point);
        dblClick(button);
    }

    /**
     * example hello world
     *
     * @param cmd string
     */
    public void sendCommand(String cmd) {
        InputAction[] actions = parseInputActionCommands(cmd);
        InputCommandTracker t = new InputCommandTracker(this);
        for (InputAction a : actions) {
            t.run(a);
        }
        t.releaseAll();
    }

    public void keyType(int vkey) {
        keyPress(vkey);
        delay(releaseThinkTime);
        keyRelease(vkey);
    }

}
