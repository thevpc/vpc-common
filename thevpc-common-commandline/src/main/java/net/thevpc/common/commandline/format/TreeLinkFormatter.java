package net.thevpc.common.commandline.format;

public interface TreeLinkFormatter {

    String formatMain(TreeFormatter.Type type);

    String formatChild(TreeFormatter.Type type);
}
