package net.vpc.common.swings.app.impl.swing;

public interface LazyTreeBackend {
    LazyTreeNode getRoot();

    LazyTreeNode[] getChildren(LazyTreeNode parent);
}
