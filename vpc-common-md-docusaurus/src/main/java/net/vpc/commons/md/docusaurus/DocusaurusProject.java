package net.vpc.commons.md.docusaurus;

import net.vpc.commons.ljson.LJSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DocusaurusProject {
    public static final String DOCUSAURUS_FOLDER_CONFIG = ".docusaurus-folder-config.json";
    public static final String DOCUSAURUS_FOLDER_CONFIG_MIMETYPE = "text/x-json-docusaurus-folder-config";
    NameResolver nameResolver = new NameResolver() {
        @Override
        public boolean accept(DocusaurusFileOrFolder item, String name) {
            if (item.isFolder()) {
                if (item.getTitle().equals(name)) {
                    return true;
                }
            }
            return item.getShortId().equals(name);
        }
    };


    private String docusaurusBaseFolder;

    private LJSON config;

    private LJSON sidebars;

    public DocusaurusProject(String docusaurusBaseFolder) {
        this.docusaurusBaseFolder = docusaurusBaseFolder;
        if (!Files.exists(Paths.get(resolvePath("docusaurus.config.js")))) {
            throw new IllegalArgumentException("Invalid docusaurus v2 folder : " + toCanonicalPath(docusaurusBaseFolder));
        }
        this.sidebars = loadModuleExportsFile("sidebars.js");
        this.config = loadModuleExportsFile("docusaurus.config.js");
    }

    private static String extractPartialPathParentString(Path p, Path rootPath) {
        Path pp = extractPartialPath(p, rootPath).getParent();
        return pp == null ? null : pp.toString();
    }

    private static Path extractPartialPath(Path p, Path rootPath) {
        if (p.startsWith(rootPath)) {
            return p.subpath(rootPath.getNameCount(), p.getNameCount());
        } else {
            throw new IllegalArgumentException("Invalid partial path");
        }
    }

    static DocusaurusFile extractItem(Path p, String partialPath) {
        try {
            BufferedReader br = Files.newBufferedReader(p);
            String line1 = br.readLine();
            if ("---".equals(line1)) {
                Map<String, String> props = new HashMap<>();
                while (true) {
                    line1 = br.readLine();
                    if (line1 == null || line1.equals("---")) {
                        break;
                    }
                    if (line1.matches("[a-z_]+:.*")) {
                        int colon = line1.indexOf(':');
                        props.put(line1.substring(0, colon).trim(), line1.substring(colon + 1).trim());
                    }
                }
                String id = props.get("id");
                Integer menu_order = DocusaurusUtils.parseInt(props.get("menu_order"));
                if (menu_order != null) {
                    if (menu_order.intValue() <= 0) {
                        throw new IllegalArgumentException("invalid menu_order in " + p);
                    }
                } else {
                    menu_order = 0;
                }
                if (id != null) {
                    return DocusaurusFile.ofPath(id, (partialPath == null || partialPath.isEmpty()) ? id : (partialPath + "/" + id), props.get("title"), p, menu_order);
                }
            }
        } catch (IOException iOException) {
        }
        return null;
    }


    public String getDocusaurusBaseFolder() {
        return this.docusaurusBaseFolder;
    }

    public LJSON getSidebars() {
        return this.sidebars;
    }

    public String getTitle() {
        return this.config.get("title").asString();
    }

    public String getProjectName() {
        return this.config.get("projectName").asString();
    }

    public LJSON getConfig() {
        return this.config;
    }

    private String resolvePath(String path) {
        String p = this.docusaurusBaseFolder;
        if (!p.endsWith("/") && !path.startsWith("/")) {
            p = p + "/";
        }
        return p + path;
    }

    public LJSON loadModuleExportsFile(String path) {
        String a = null;
        try {
            a = new String(Files.readAllBytes(Paths.get(resolvePath(path))));
        } catch (IOException ex) {
            return LJSON.NULL;
        }
        String json = a.replace("module.exports =", "").trim();
        if (json.endsWith(";")) {
            json = json.substring(0, json.length() - 1);
        }
        return LJSON.of(json);
    }

    private String toCanonicalPath(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException ex) {
            return new File(path).getAbsolutePath();
        }
    }

    public DocusaurusFileOrFolder extractFileOrFolder(Path path, Path root) {
        if (Files.isDirectory(path) && path.equals(root)) {
            try {
                return DocusaurusFolder.ofRoot(
                        Files.list(path).map(p -> DocusaurusFolder.ofFileOrFolder(p, root))
                                .filter(Objects::nonNull)
                                .toArray(DocusaurusFileOrFolder[]::new)
                );
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        } else if (Files.isDirectory(path)) {
            String longId = path.subpath(root.getNameCount(), path.getNameCount()).toString();
            Path dfi = path.resolve(DOCUSAURUS_FOLDER_CONFIG);
            LJSON config = LJSON.NULL;
            if (Files.isRegularFile(dfi)) {
                try {
                    config = LJSON.of(new String(Files.readAllBytes(dfi)));
                } catch (IOException e) {
                    //ignore...
                }
            }
            try {
                Integer order = config.get("order").asInt();
                if (order == null) {
                    order = 0;
                } else if (order <= 0) {
                    throw new IllegalArgumentException("");
                }
                String title = config.get("title").asString();
                if(title==null){
                    title=path.getFileName().toString();
                }
                return new DocusaurusFolder(
                        longId,
                        title,
                        order,
                        config,
                        Files.list(path).map(p -> DocusaurusFolder.ofFileOrFolder(p, root))
                                .filter(Objects::nonNull)
                                .toArray(DocusaurusFileOrFolder[]::new)
                );
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            int from = root.getNameCount();
            int to = path.getNameCount() - 1;
            String partial = from == to ? "" : path.subpath(from, to).toString();
            return extractItem(path, partial);
        }
    }

    public DocusaurusFileOrFolder[] LJSON_to_DocusaurusFileOrFolder_list(LJSON a, String path, DocusaurusFolder root) {
        if (a.isString()) {
            return new DocusaurusFileOrFolder[]{
                    root.getPage(a.asString(), true, null)
            };
        } else if (a.isArray()) {
            List<DocusaurusFileOrFolder> aa = new ArrayList<>();
            for (LJSON ljson : a.arrayMembers()) {
                aa.addAll(Arrays.asList(LJSON_to_DocusaurusFileOrFolder_list(ljson, path, root)));
            }
            return aa.toArray(new DocusaurusFileOrFolder[0]);
        } else if (a.isObject()) {
            List<DocusaurusFileOrFolder> aa = new ArrayList<>();
            int order = 0;
            for (LJSON.Member member : a.objectMembers()) {
                DocusaurusFileOrFolder[] cc = LJSON_to_DocusaurusFileOrFolder_list(member.getValue(), DocusaurusUtils.concatPath(path, member.getName()), root);
                aa.add(new DocusaurusFolder(
                        path,
                        member.getName(),
                        ++order,
                        LJSON.NULL,
                        cc
                ));
            }
            return aa.toArray(new DocusaurusFileOrFolder[0]);
        } else {
            throw new IllegalArgumentException("Invalid");
        }
    }

    public DocusaurusFolder getSidebarsDocsFolder() {
        DocusaurusFileOrFolder[] someSidebars = LJSON_to_DocusaurusFileOrFolder_list(this.sidebars.get("someSidebar"), "/", getPhysicalDocsFolder());
        return DocusaurusFolder.ofRoot(someSidebars);
    }

    public DocusaurusFolder getPhysicalDocsFolder() {
        Path docs = Paths.get(this.docusaurusBaseFolder).resolve("docs");
        DocusaurusFolder root = (DocusaurusFolder) DocusaurusFolder.ofFileOrFolder(docs, docs);
        return root;
    }

//    public DocusaurusPart[] loadDocusaurusProject() {
//        if (!Files.exists(Paths.get(resolvePath("docusaurus.config.js")))) {
//            throw new IllegalArgumentException("Invalid docusaurus v2 folder : " + toCanonicalPath(docusaurusBaseFolder));
//        }
//        try {
//            this.sidebars = loadModuleExportsFile("sidebars.js");
//            this.config = loadModuleExportsFile("docusaurus.config.js");
//            LinkedHashMap<String, List<DocusaurusFile>> project = new LinkedHashMap<>();
//            String docs = this.docusaurusBaseFolder + "/docs";
//            Map<String, List<DocusaurusFile>> ir = allDocs(docs, docs, true);
//            for (LJSON.Member member : this.sidebars.get("someSidebar").objectMembers()) {
//                ArrayList<DocusaurusFile> items = new ArrayList<>();
//                project.put(member.getName(), items);
//                for (LJSON jsonValue : member.getValue().arrayMembers()) {
//                    String s = jsonValue.asString();
//                    int ls = s.lastIndexOf('/');
////                    Map<String, List<DocusaurusFile>> ir = null;
////                    if (ls > 0) {
////                        String z=s.substring(0,ls);
////                        ir = allDocs(docs + "/" + z, docs,false);
////                    } else {
////                        ir = allDocs(docs, docs,false);
////                    }
//                    List<DocusaurusFile> u = ir.get(s);
//                    if (u != null && u.size() > 0) {
//                        if (u.size() == 1) {
//                            items.add(u.get(0));
//                        } else {
//                            System.err.println("ambiguous: " + s + " :: " + u.stream().map(x -> x.getPath()).collect(Collectors.toList()));
//                        }
//                    } else {
//                        System.err.println("document id not found: " + s);
//                    }
//                }
//            }
//            List<DocusaurusPart> parts = new ArrayList<>();
//            for (Map.Entry<String, List<DocusaurusFile>> entry : project.entrySet()) {
//                DocusaurusPart p = new DocusaurusPart(entry.getKey(), entry.getValue().toArray(new DocusaurusFile[0]));
//                parts.add(p);
//            }
//            return parts.toArray(new DocusaurusPart[0]);
//        } catch (IOException ex) {
//            throw new UncheckedIOException(ex);
//        }
//    }
}
