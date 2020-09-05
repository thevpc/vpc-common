package net.vpc.commons.md.docusaurus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.vpc.commons.ljson.LJSON;

public class DocusaurusProject {

    private String docusaurusBaseFolder;

    private Map<String, List<Doc>> docs;

    private LJSON config;

    private LJSON sidebars;

    public DocusaurusProject(String docusaurusBaseFolder) {
        this.docusaurusBaseFolder = docusaurusBaseFolder;
        this.docs = loadDocusaurusProject();
    }

    public String getDocusaurusBaseFolder() {
        return this.docusaurusBaseFolder;
    }

    public LJSON getSidebars() {
        return this.sidebars;
    }

    public Map<String, List<Doc>> getDocs() {
        return this.docs;
    }

    public String getTitle() {
        return this.config.get("title").asString();
    }

    public String getProjectName() {
        return this.config.get("projectName").asString();
    }

    public static class Doc {

        public String id;

        public String title;

        public Path path;

        public Doc(String id, String title, Path path) {
            this.id = id;
            this.title = title;
            this.path = path;
        }

        public String toString() {
            return "Item{id=" + this.id + ", title=" + this.title + ", path=" + this.path.getFileName().toString() + "}";
        }
    }

    public LJSON getConfig() {
        return this.config;
    }

    private String resolvePath(String path) {
        String p = this.docusaurusBaseFolder;
        if (!p.endsWith("/") && !path.startsWith("/")) {
            p = p + "/";
        }
        return p+path;
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

    private String toCanonicalPath(String path){
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException ex) {
            return new File(path).getAbsolutePath();
        }
    }
    
    public Map<String, List<Doc>> loadDocusaurusProject() {
        if (!Files.exists(Paths.get(resolvePath("docusaurus.config.js")))) {
            throw new IllegalArgumentException("Invalid docusaurus v2 folder : "+toCanonicalPath(docusaurusBaseFolder));
        }
        try {
            this.sidebars = loadModuleExportsFile("sidebars.js");
            this.config = loadModuleExportsFile("docusaurus.config.js");
            LinkedHashMap<String, List<Doc>> project = new LinkedHashMap<>();
            for (LJSON.Member member : this.sidebars.get("someSidebar").objectMembers()) {
                ArrayList<Doc> items = new ArrayList<>();
                project.put(member.getName(), items);
                for (LJSON jsonValue : member.getValue().arrayMembers()) {
                    String s = jsonValue.asString();
                    int ls = s.lastIndexOf('/');
                    Map<String, List<Doc>> ir = null;
                    if (ls > 0) {
                        ir = allDocs(this.docusaurusBaseFolder + "/docs/" + this.docusaurusBaseFolder);
                    } else {
                        ir = allDocs(this.docusaurusBaseFolder + "/docs");
                    }
                    List<Doc> u = ir.get(s.substring(ls + 1));
                    if (u != null && u.size() > 0) {
                        if (u.size() == 1) {
                            items.add(u.get(0));
                        } else {
                            System.err.println("ambigoues: " + s + " :: " + u);
                        }
                    } else {
                        System.err.println("document id not found: " + s);
                    }
                }
            }
            return project;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static Map<String, List<Doc>> allDocs(String docusaurusBaseFolder) throws IOException {
        File[] fs = (new File(docusaurusBaseFolder)).listFiles();
        List<Doc> items = new ArrayList<>();
        if (fs != null) {
            for (File f : fs) {
                if (f.isFile()) {
                    Doc i = extractItem(f.toPath());
                    if (i != null) {
                        items.add(i);
                    }
                }
            }
        }
        Map<String, List<Doc>> all = (Map<String, List<Doc>>) items.stream().filter(x -> (x != null)).collect(
                Collectors.groupingBy(x -> x.id));
        return all;
    }

    private static Map<String, List<Doc>> allDocsRecursive(String docusaurusBaseFolder) throws IOException {
        Map<String, List<Doc>> all = (Map<String, List<Doc>>) Files.walk(Paths.get(docusaurusBaseFolder, new String[0]).resolve("docs"), new java.nio.file.FileVisitOption[0]).filter(x -> x.getFileName().toString().endsWith(".md")).map(x -> extractItem(x)).filter(x -> (x != null)).collect(
                Collectors.groupingBy(x -> x.id));
        return all;
    }

    static Doc extractItem(Path p) {
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
                if (id != null) {
                    return new Doc(id, props.get("title"), p);
                }
            }
        } catch (IOException iOException) {
        }
        return null;
    }
}
