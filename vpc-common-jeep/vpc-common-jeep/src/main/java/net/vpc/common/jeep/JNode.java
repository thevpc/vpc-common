/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.jeep;

import java.util.List;
import java.util.Map;

/**
 * @author vpc
 */
public interface JNode {

    JNode parentNode();

    JToken startToken();

    JToken endToken();

    void setStartToken(JToken startToken);

    void setEndToken(JToken endToken);

    default boolean isExitContext() {
        return false;
    }

    Map<String, Object> userObjects();

    void setUserObject(String name, Object value);

    void setUserObject(String name, boolean valid);

    void setUserObject(String name);

    void unsetUserObject(String name);

    boolean isTestAndSetUserObject(String name);

    boolean isSetUserObject(String name);

    //    JType getType(JContext context);
    JPositionStyle getPosition(int caretOffset);

    void visit(JNodeVisitor visitor);

    JNode childInfo(Object childInfo);

    Object childInfo();

    List<JNode> childrenNodes();

    void copyFrom(JNode other, JNodeCopyFactory copyFactory);

    void copyFrom(JNode other);

    JNode copy();

    JNode copy(JNodeCopyFactory copyFactory);

    JNode findAndReplace(JNodeFindAndReplace findAndReplace);

    boolean containsCaret(int caretOffset);
    JToken[] getSeparators();
}
