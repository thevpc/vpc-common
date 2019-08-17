/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.common.swings.tree;

import java.awt.Component;
import java.util.function.Supplier;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author vpc
 */
public class TreeCellRendererHighligter extends DefaultTreeCellRenderer {

    private static final String SPAN_FORMAT = "<span style='color:%s;'>%s</span>";
    private Supplier<String> filterTextSupplier;

    public TreeCellRendererHighligter(Supplier<String> filterTextSupplier) {
        this.filterTextSupplier = filterTextSupplier;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        Object userObject = value;
        if(userObject instanceof DefaultMutableTreeNode){
            userObject=((DefaultMutableTreeNode) value).getUserObject();
        }
//        if (userObject instanceof ProjectParticipant) {
//            ProjectParticipant pp = (ProjectParticipant) userObject;
//            String text = String.format(SPAN_FORMAT, "rgb(0, 0, 150)",
//                    renderFilterMatch(node, pp.getName()));
//            text += " [" + String.format(SPAN_FORMAT, "rgb(90, 70, 0)",
//                    renderFilterMatch(node, pp.getRole())) + "]";
//            this.setText("<html>" + text + "</html>");
//            return this;
//        } else if (userObject instanceof Project) {
//            Project project = (Project) userObject;
//            String text = String.format(SPAN_FORMAT, "rgb(0,70,0)",
//                    renderFilterMatch(node, project.getName()));
//            this.setText("<html>" + text + "</html>");
//            return this;
//        }
        String text = String.format(SPAN_FORMAT, "rgb(120,0,0)",
                renderFilterMatch(value, userObject==null?"":String.valueOf(userObject)));
        this.setText("<html>" + text + "</html>");
        return this;
    }

    private String renderFilterMatch(Object node, String text) {
//        if (node.isRoot()) {
//            return text;
//        }
        String textToFilter = filterTextSupplier.get();
        return HtmlHighlighter.highlightText(text, textToFilter);
    }
}
