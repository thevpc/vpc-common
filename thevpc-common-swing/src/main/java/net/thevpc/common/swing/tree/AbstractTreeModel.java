/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swing.tree;

import java.util.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author vpc
 */
public abstract class AbstractTreeModel implements TreeModel {

    protected EventListenerList listenerList = new EventListenerList();

    /**
     * This sets the user object of the TreeNode identified by path and posts a
     * node changed. If you use custom user objects in the TreeModel you're
     * going to need to subclass this and set the user object of the changed
     * node to something meaningful.
     */
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
//        System.out.println(
//                "valueForPathChanged:"
//                        +"path="+ path+", "
//                        +"newValue="+ newValue
//        );
        Object aNode = (Object) path.getLastPathComponent();
        nodeChanged(aNode);
    }

    public void valueForPathChangedImpl(TreePath path, Object newValue) {

    }

    /**
     * Invoked this to insert newChild at location index in parents children.
     * This will then message nodesWereInserted to create the appropriate event.
     * This is the preferred way to add children as it will create the
     * appropriate event.
     */
    public void insertNodeInto(Object newChild, Object parent, int index) {
//        System.out.println(
//                "insertNodeInto:"
//                        +"parent="+ parent+", "
//                        +"index="+ index+", "
//                        +"child="+ newChild
//        );
        insertNodeIntoImpl(parent, newChild, index);

//        int[] newIndexs = new int[1];
//
//        newIndexs[0] = index;
//        nodesWereInserted(parent, newIndexs);
    }

    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    protected abstract void insertNodeIntoImpl(Object parent, Object newChild, int index);

    protected abstract void removeNodeFromParentImpl(Object parent, int childIndex);

    public void removeNodeFromParent(Object node) {
//        System.out.println(
//                "removeNodeFromParent:"
//                        +"node="+ node
//        );
        Object parent = (Object) getParent(node);

        if (parent == null) {
            throw new IllegalArgumentException("node does not have a parent.");
        }

        int[] childIndex = new int[1];
        Object[] removedArray = new Object[1];

        childIndex[0] = getIndexOfChild(parent, node);
        int cc = childIndex[0];
        if (cc >= 0) {
            removeNodeFromParentImpl(parent, cc);
//            removedArray[0] = node;
//            nodesWereRemoved(parent, childIndex, removedArray);
        }
    }

    public void nodeChanged(Object node) {
//        System.out.println(
//                "nodeChanged:"
//                        +"node="+ node+", "
//        );
        if (listenerList != null && node != null) {
            Object parent = getParent(node);

            if (parent != null) {
                int anIndex = getIndexOfChild(parent, node);
                if (anIndex != -1) {
                    int[] cIndexs = new int[1];

                    cIndexs[0] = anIndex;
                    nodesChanged(parent, cIndexs);
                }
            } else if (node == getRoot()) {
                nodesChanged(node, null);
            }
        }
    }

    public void nodesWereInserted(Object node, int[] childIndices) {
//        System.out.println(
//                "nodesWereInserted:"
//                        +"node="+ node+", "
//                        +"indices="+ (childIndices==null?"null":Arrays.toString(childIndices))
//        );
        if (listenerList != null && node != null && childIndices != null
                && childIndices.length > 0) {
            int cCount = childIndices.length;
            Object[] newChildren = new Object[cCount];

            for (int counter = 0; counter < cCount; counter++) {
                newChildren[counter] = getChild(node, childIndices[counter]);
            }
            fireTreeNodesInserted(this, getPathToRoot(node), childIndices,
                    newChildren);
        }
    }

    public void nodesWereRemoved(Object node, int[] childIndices,
            Object[] removedChildren) {
//        System.out.println(
//                "nodesWereRemoved:"
//                        +"node="+ node+", "
//                        +"indices="+ (childIndices==null?"null":Arrays.toString(childIndices))+", "
//                        +"children="+ (removedChildren==null?"null":Arrays.toString(removedChildren))
//        );
        if (node != null && childIndices != null) {
            fireTreeNodesRemoved(this, getPathToRoot(node), childIndices,
                    removedChildren);
        }
    }

    public void nodesChanged(Object node, int[] childIndices) {
//        System.out.println(
//                "nodesChanged:"
//                        +"node="+ node+", "
//                        +"indices="+ (childIndices==null?"null":Arrays.toString(childIndices))+", "
//        );
        if (node != null) {
            if (childIndices != null) {
                int cCount = childIndices.length;

                if (cCount > 0) {
                    Object[] cChildren = new Object[cCount];

                    for (int counter = 0; counter < cCount; counter++) {
                        cChildren[counter] = getChild(node, childIndices[counter]);
                    }
                    fireTreeNodesChanged(this, getPathToRoot(node),
                            childIndices, cChildren);
                }
            } else if (node == getRoot()) {
                fireTreeNodesChanged(this, getPathToRoot(node), null, null);
            }
        }
    }

    public void nodeStructureChanged(Object node) {
//        System.out.println(
//                "nodeStructureChanged:"
//                        +"node="+ node
//        );
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    public Object[] getPathToRoot(Object aNode) {
        return getPathToRoot(aNode, 0);
    }

    protected Object[] getPathToRoot(Object aNode, int depth) {
        Object[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new Object[depth];
            }
        } else {
            depth++;
            if (aNode == getRoot()) {
                retNodes = new Object[depth];
            } else {
                retNodes = getPathToRoot(getParent(aNode), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    public TreeModelListener[] getTreeModelListeners() {
        return listenerList.getListeners(TreeModelListener.class);
    }

    protected void fireTreeNodesChanged(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {
//        System.out.println(
//                "fireTreeNodesChanged:"
//                        +"path="+ (path==null?"null":Arrays.toString(path))+", "
//                        +"indices="+ (childIndices==null?"null":Arrays.toString(childIndices))+", "
//                        +"children="+ (children==null?"null":Arrays.toString(children))
//        );
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    protected void fireTreeStructureChanged(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {
//        System.out.println(
//                "fireTreeStructureChanged:"
//                        +"path="+ (path==null?"null":Arrays.toString(path))+", "
//                        +"indices="+ (childIndices==null?"null":Arrays.toString(childIndices))+", "
//                        +"children="+ (children==null?"null":Arrays.toString(children))
//        );
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    protected void fireTreeNodesInserted(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {
//        System.out.println(
//                "fireTreeNodesInserted:"
//                        +"parent="+ (path==null?"null":Arrays.toString(path))+", "
//                        +"indices="+ (childIndices==null?"null":Arrays.toString(childIndices))+", "
//                        +"children="+ (children==null?"null":Arrays.toString(children))
//        );
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    protected void fireTreeStructureChanged(Object source, TreePath path) {
//        System.out.println(
//                "fireTreeStructureChanged:"
//                        +"path="+ (path==null?"null":String.valueOf(path))
//        );
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return listenerList.getListeners(listenerType);
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path,
            int[] childIndices,
            Object[] children) {
//        System.out.println(
//                "fireTreeNodesRemoved:"
//                        +"parent="+ Arrays.toString(path)+", "
//                        +"indices="+ (childIndices==null?"null":Arrays.toString(childIndices))+", "
//                        +"children="+ (children==null?"null":Arrays.toString(children))
//        );
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                try {
                    ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public Enumeration<Object> breadthFirstEnumeration() {
        return new BreadthFirstEnumeration(getRoot());
    }

    public List<Object> getChildren(Object parent) {
        int c = getChildCount(parent);
        List<Object> all = new ArrayList<>();
        for (int i = 0; i < c; i++) {
            all.add(getChild(parent, i));
        }
        return all;
    }

    public abstract Object getParent(Object target);

    public int getLevel(Object target) {
        if (target == null || target == getRoot()) {
            return 0;
        }
        return 1 + getLevel(getParent(target));
    }

    public boolean isNodeChild(Object first, Object next) {
        return getParent(first) == next;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        int count = getChildCount(parent);
        for (int i = 0; i < count; i++) {
            Object object = getChild(parent, i);
            if (object == child) {
                return i;
            }
        }
        return -1;
    }

    public abstract Object copyNode(Object node);

    final class BreadthFirstEnumeration implements Enumeration<Object> {

        protected Queue queue;

        public BreadthFirstEnumeration(Object rootNode) {
            super();
            List<Object> v = new ArrayList<Object>(1);
            v.add(rootNode);
            queue = new Queue();
            queue.enqueue(Collections.enumeration(v));
        }

        public boolean hasMoreElements() {
            return (!queue.isEmpty()
                    && ((Enumeration) queue.firstObject()).hasMoreElements());
        }

        public Object nextElement() {
            Enumeration enumer = (Enumeration) queue.firstObject();
            Object node = (Object) enumer.nextElement();
            Enumeration children = Collections.enumeration(getChildren(node));

            if (!enumer.hasMoreElements()) {
                queue.dequeue();
            }
            if (children.hasMoreElements()) {
                queue.enqueue(children);
            }
            return node;
        }

        // A simple queue with a linked list data structure.
        final class Queue {

            QNode head; // null if empty
            QNode tail;

            final class QNode {

                public Object object;
                public QNode next;   // null if end

                public QNode(Object object, QNode next) {
                    this.object = object;
                    this.next = next;
                }
            }

            public void enqueue(Object anObject) {
                if (head == null) {
                    head = tail = new QNode(anObject, null);
                } else {
                    tail.next = new QNode(anObject, null);
                    tail = tail.next;
                }
            }

            public Object dequeue() {
                if (head == null) {
                    throw new NoSuchElementException("No more elements");
                }

                Object retval = head.object;
                QNode oldHead = head;
                head = head.next;
                if (head == null) {
                    tail = null;
                } else {
                    oldHead.next = null;
                }
                return retval;
            }

            public Object firstObject() {
                if (head == null) {
                    throw new NoSuchElementException("No more elements");
                }

                return head.object;
            }

            public boolean isEmpty() {
                return head == null;
            }

        } // End of class Queue

    }  // End of class BreadthFirstEnumeration

    public interface ParentResolver {

        Object getParent(Object o);
    }

    final class PathBetweenNodesEnumeration implements Enumeration<Object> {

        protected Stack<Object> stack;

        public PathBetweenNodesEnumeration(Object ancestor,
                Object descendant, ParentResolver p) {
            super();

            if (ancestor == null || descendant == null) {
                throw new IllegalArgumentException("argument is null");
            }

            Object current;

            stack = new Stack<Object>();
            stack.push(descendant);

            current = descendant;
            while (current != ancestor) {
                current = p.getParent(current);
                if (current == null && descendant != ancestor) {
                    throw new IllegalArgumentException("node " + ancestor
                            + " is not an ancestor of " + descendant);
                }
                stack.push(current);
            }
        }

        public boolean hasMoreElements() {
            return stack.size() > 0;
        }

        public Object nextElement() {
            try {
                return stack.pop();
            } catch (EmptyStackException e) {
                throw new NoSuchElementException("No more elements");
            }
        }

    } // 
}
