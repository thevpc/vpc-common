/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep.core.nodes;

import net.vpc.common.jeep.*;
import net.vpc.common.jeep.util.JNodeUtils;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author vpc
 */
public abstract class AbstractJNode implements JNode {

    private JToken startToken;
    private JToken endToken;
    /**
     * information on this node when bound to Parent
     */
    private Object childInfo;
    private Object exitContextObject;
    private JNode parentNode;

    public AbstractJNode() {
    }

    @Override
    public JToken startToken() {
        return startToken;
    }

    @Override
    public JToken endToken() {
        return endToken;
    }

    @Override
    public void setStartToken(JToken startToken) {
        this.startToken = startToken == null ? null : startToken.copy();
    }

    @Override
    public void setEndToken(JToken endToken) {
        this.endToken = endToken == null ? null : endToken.copy();
    }

    public Object exitContextObject() {
        return exitContextObject;
    }

    public AbstractJNode exitContextObject(Object exitContextObject) {
        this.exitContextObject = exitContextObject;
        return this;
    }

    @Override
    public boolean isExitContext() {
        return exitContextObject() != null;
    }

    @Override
    public JNode parentNode() {
        return parentNode;
    }

    public AbstractJNode parentNode(JNode parentNode) {
        this.parentNode = parentNode;
        return this;
    }
    private Map<String, Object> userObjects = new LinkedHashMap<>();

    @Override
    public Map<String, Object> userObjects() {
        return userObjects;
    }

    @Override
    public void setUserObject(String name, Object value) {
        if (value == null || (value instanceof Boolean && !(boolean) value)) {
            userObjects.remove(name);
        } else {
            userObjects.put(name, value);
        }
    }

    public void setUserObject(String name, boolean valid) {
        if (valid) {
            userObjects.put(name, true);
        } else {
            userObjects.remove(name);
        }
    }

    public void setUserObject(String name) {
        userObjects.put(name, true);
    }

    @Override
    public void unsetUserObject(String name) {
        userObjects.remove(name);
    }

    @Override
    public boolean isTestAndSetUserObject(String name) {
        if (!isSetUserObject(name)) {
            setUserObject(name);
            return true;
        }
        return false;
    }

    @Override
    public boolean isSetUserObject(String name) {
        return userObjects.containsKey(name);
    }

    public static JPositionStyle getPosition(JNode node, int caretOffset) {
        if (node == null) {
            return null;
        }
        return node.getPosition(caretOffset);
    }

    public JPositionStyle getPosition(int caretOffset) {
        if (caretOffset < this.startToken().startCharacterNumber) {
            return JPositionStyle.BEFORE;
        } else if (caretOffset == this.startToken().startCharacterNumber) {
            return JPositionStyle.START;
        } else if (caretOffset < this.endToken().endCharacterNumber) {
            return JPositionStyle.MIDDLE;
        } else if (caretOffset == this.endToken().endCharacterNumber) {
            return JPositionStyle.END;
        } else {
            return JPositionStyle.AFTER;
        }
    }

    public void visit(JNodeVisitor visitor) {
        visitor.startVisit(this);
        visitNext(visitor, childrenNodes());
        visitor.endVisit(this);
    }

    protected void visitNext(JNodeVisitor visitor, JNode child) {
        if (child != null) {
            (child).visit(visitor);
        }
    }

    protected void visitNext(JNodeVisitor visitor, JNode[] child) {
        if (child != null) {
            for (JNode c : child) {
                visitNext(visitor, c);
            }
        }
    }

    protected <T extends JNode> void visitNext(JNodeVisitor visitor, List<T> child) {
        if (child != null) {
            for (JNode c : child) {
                visitNext(visitor, c);
            }
        }
    }

    @Override
    public List<JNode> childrenNodes() {
        return Collections.emptyList();
    }

    @Override
    public void copyFrom(JNode other) {
        copyFrom(other, null);
    }

    @Override
    public void copyFrom(JNode other, JNodeCopyFactory copyFactory) {
        if (other != null) {
            setStartToken(other.startToken());
            setEndToken(other.endToken());
            childInfo(other.childInfo());
        }
    }

    @Override
    public JNode copy() {
        return copy(null);
    }

    @Override
    public JNode copy(JNodeCopyFactory copyFactory) {
        JNode n = null;
        try {
            Constructor<? extends AbstractJNode> dc = this.getClass().getDeclaredConstructor();
            dc.setAccessible(true);
            n = dc.newInstance();
        } catch (Exception e) {
            throw new JShouldNeverHappenException();
        }
        n.copyFrom(this,copyFactory);
        return n;
    }

    public JNode findAndReplace(JNodeFindAndReplace findAndReplace) {
        boolean replaceFirst = findAndReplace.isReplaceFirst();
        if (replaceFirst) {
            if (findAndReplace.accept(this)) {
                JNode r = findAndReplace.replace(this);
                if (r != null) {
                    return r;
                }
            }
        }
        findAndReplaceChildren(findAndReplace);
        if (!replaceFirst) {
            if (findAndReplace.accept(this)) {
                JNode r = findAndReplace.replace(this);
                if (r != null) {
                    return r;
                }
            }
        }
        return this;
    }

    public <T extends JNode> T bind(T child) {
        if (child != null) {
            ((AbstractJNode) child).parentNode(this);
        }
        return child;
    }

    public <T extends JNode> T bind(T child, String asName) {
        if (child != null) {
            ((AbstractJNode) child).parentNode(this);
            ((AbstractJNode) child).childInfo(asName);
        }
        return child;
    }

    protected void findAndReplaceChildren(JNodeFindAndReplace findAndReplace) {
        //should implement me
    }

    @Override
    public JNode childInfo(Object childInfo) {
        this.childInfo = childInfo;
        return this;
    }

    @Override
    public Object childInfo() {
        return childInfo;
    }

    @Override
    public boolean containsCaret(int caretOffset) {
        return caretOffset >= startToken().startCharacterNumber
                && caretOffset <= endToken().endCharacterNumber;
    }

    public static boolean containsCaret(JNode root, int caretOffset) {
        return (root != null && root.containsCaret(caretOffset));
    }
}
