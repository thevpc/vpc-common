package net.vpc.commons.md.docusaurus;

public interface NameResolver {
    boolean accept(DocusaurusFileOrFolder item,String name);
}
