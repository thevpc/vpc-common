package net.thevpc.common.props.impl;

import net.thevpc.common.props.ObservableList;
import net.thevpc.common.props.WritableIndexedNode;
import net.thevpc.common.props.WritableList;

import java.util.Collection;
import java.util.Objects;

public class Helpers {

    public static <T> WritableIndexedNode<T> IndexedNode_findChild(WritableIndexedNode<T> THIS, Object[] path) {
        if(path.length==0){
            return THIS;
        }
        WritableIndexedNode<T> curr = THIS;
        for (int i = 0; i < path.length; i++) {
            if(curr==null){
                return null;
            }
            curr= IndexedNode_findNode(THIS,path[i], (ObservableList) curr.children());
        }
        return curr;
    }

    private static <T> WritableIndexedNode<T> IndexedNode_findNode(WritableIndexedNode<T> THIS, Object child, ObservableList<WritableIndexedNode<T>> children) {
        return children.stream().filter(
                (WritableIndexedNode<T> x)-> Objects.equals(x.get(),child)).findFirst().orElse(null);
    }


    public static <T> boolean WritableList_addCollection(WritableList<T> THIS, Collection<? extends T> extra) {
        boolean b = false;
        for (T t : extra) {
            b |= THIS.add(t);
        }
        return b;
    }
}
