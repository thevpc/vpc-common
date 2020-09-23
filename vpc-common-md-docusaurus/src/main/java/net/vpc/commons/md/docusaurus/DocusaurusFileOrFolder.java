package net.vpc.commons.md.docusaurus;

public interface DocusaurusFileOrFolder {
    String getShortId();

    String getLongId();

    String getTitle();

    boolean isFile();

    int getOrder();

    boolean isFolder();

    String toJSON(int indent);
}
