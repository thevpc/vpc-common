/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.common.swings.tree;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * thanks to https://www.logicbig.com/tutorials/java-swing/jtree-filtering.html
 *
 * @author unknown
 */
public class TreeFilterDecorator {

    private final JTree tree;
    private DefaultMutableTreeNode originalRootNode;
    private final BiPredicate<Object, String> userObjectMatcher;
    private final TreeCellTextRenderer textRenderer;
    private JTextField filterField;

    public TreeFilterDecorator(JTree tree, BiPredicate<Object, String> userObjectMatcher, TreeCellTextRenderer textRenderer) {
        this.tree = tree;
        if (userObjectMatcher == null) {
            this.userObjectMatcher = (userObject, textToFilter) -> {
                return (userObject == null ? "" : userObject.toString().toLowerCase()).contains(textToFilter);
            };
        } else {
            this.userObjectMatcher = userObjectMatcher;
        }
        this.textRenderer = textRenderer;
        tree.addPropertyChangeListener(JTree.TREE_MODEL_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                modelChanged();
            }

        });
        modelChanged();
    }

    private void modelChanged() {
        this.originalRootNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        filterTree();
    }

    public static TreeFilterDecoratorBuilder decorator(JTree tree) {
        return new TreeFilterDecoratorBuilder().setTree(tree);
    }

    public static TreeFilterDecorator decorate(JTree tree, BiPredicate<Object, String> userObjectMatcher, final SubTreeCellRenderer subTreeCellRenderer, TreeCellTextRenderer textRenderer) {
        final TreeFilterDecorator tfd = new TreeFilterDecorator(tree, userObjectMatcher, textRenderer);
        tfd.init();
        tree.setCellRenderer(new TreeCellRendererHighligter(new Supplier<String>() {
            @Override
            public String get() {
                return tfd.getFilterField().getText();
            }
        }) {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean selected, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {
                if (subTreeCellRenderer != null) {
                    subTreeCellRenderer.getTreeCellRendererComponent(this, tree, value,
                            selected, expanded,
                            leaf, row, hasFocus);
                    return this;
                } else {
                    if (textRenderer != null) {
                        value = textRenderer.renderText(value);
                    }
                    return super.getTreeCellRendererComponent(tree, value,
                            selected, expanded,
                            leaf, row, hasFocus);
                }
            }
        });
        return tfd;
    }

    public JTextField getFilterField() {
        return filterField;
    }

    private void init() {
        initFilterField();
    }

    private void initFilterField() {
        filterField = new JTextField(15);
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTree();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTree();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTree();
            }
        });
    }

    private void filterTree() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        if (filterField != null) {
            String text = filterField.getText().trim().toLowerCase();
            if (text.equals("") && tree.getModel().getRoot() != originalRootNode) {
                model.setRoot(originalRootNode);
                JTreeUtil.setTreeExpandedState(tree, true);
            } else {
                DefaultMutableTreeNode newRootNode = matchAndBuildNode(text, originalRootNode);
                model.setRoot(newRootNode);
                JTreeUtil.setTreeExpandedState(tree, true);
            }
        }
    }

    private DefaultMutableTreeNode matchAndBuildNode(final String text, DefaultMutableTreeNode oldNode) {
        Object val = oldNode.getUserObject();
        if (textRenderer != null) {
            val = textRenderer.renderText(val);
        }
        if (!oldNode.isRoot() && userObjectMatcher.test(val, text)) {
            return JTreeUtil.copyNode(oldNode);
        }
        DefaultMutableTreeNode newMatchedNode = oldNode.isRoot() ? new DefaultMutableTreeNode(oldNode
                .getUserObject()) : null;
        for (DefaultMutableTreeNode childOldNode : JTreeUtil.children(oldNode)) {
            DefaultMutableTreeNode newMatchedChildNode = matchAndBuildNode(text, childOldNode);
            if (newMatchedChildNode != null) {
                if (newMatchedNode == null) {
                    newMatchedNode = new DefaultMutableTreeNode(oldNode.getUserObject());
                }
                newMatchedNode.add(newMatchedChildNode);
            }
        }
        return newMatchedNode;
    }

    public static class TreeFilterDecoratorBuilder {

        private JTree tree;
        private BiPredicate<Object, String> userObjectMatcher;
        private TreeCellTextRenderer textRenderer;
        private SubTreeCellRenderer subTreeCellRenderer;

        public JTree getTree() {
            return tree;
        }

        public TreeFilterDecoratorBuilder setTree(JTree tree) {
            this.tree = tree;
            return this;
        }

        public BiPredicate<Object, String> getUserObjectMatcher() {
            return userObjectMatcher;
        }

        public TreeFilterDecoratorBuilder setUserObjectMatcher(BiPredicate<Object, String> userObjectMatcher) {
            this.userObjectMatcher = userObjectMatcher;
            return this;
        }

        public TreeCellTextRenderer getTextRenderer() {
            return textRenderer;
        }

        public TreeFilterDecoratorBuilder setTextRenderer(TreeCellTextRenderer textRenderer) {
            this.textRenderer = textRenderer;
            return this;
        }

        public SubTreeCellRenderer getSubTreeCellRenderer() {
            return subTreeCellRenderer;
        }

        public TreeFilterDecoratorBuilder setSubTreeCellRenderer(SubTreeCellRenderer subTreeCellRenderer) {
            this.subTreeCellRenderer = subTreeCellRenderer;
            return this;
        }

        public TreeFilterDecorator decorate() {
            return TreeFilterDecorator.decorate(tree, userObjectMatcher, subTreeCellRenderer, textRenderer);
        }
    }
}
