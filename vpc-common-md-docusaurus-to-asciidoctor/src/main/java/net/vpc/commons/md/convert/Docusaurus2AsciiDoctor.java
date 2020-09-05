/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.commons.md.convert;

import java.io.File;
import net.vpc.commons.md.MdElementTransformBase;
import net.vpc.commons.md.MdElement;
import net.vpc.commons.md.MdLineSeparator;
import net.vpc.commons.md.MdSequence;
import net.vpc.commons.md.MdText;
import net.vpc.commons.md.MdTitle;
import net.vpc.commons.md.MdXml;
import net.vpc.commons.md.asciidoctor.AsciiDoctorWriter;
import net.vpc.commons.md.docusaurus.DocusaurusProject;
import net.vpc.commons.md.docusaurus.DocusaurusProject.Doc;
import net.vpc.commons.md.docusaurus.DocusaurusMdParser;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.vpc.commons.ljson.LJSON;
import net.vpc.commons.md.MdElementPath;
import net.vpc.commons.md.MdFactory;

/**
 *
 * @author vpc
 */
public class Docusaurus2AsciiDoctor {

    public static void main(String[] args) {
        new Docusaurus2AsciiDoctor(new File(".")).run();
    }

    protected File adocFile;
    protected File pdfFile;
    protected boolean generatePdf;
    protected DocusaurusProject project;
    Function<String, String> placeHolderReplacer = new Function<String, String>() {
        @Override
        public String apply(String t) {
            if (t.equals("asciidoctor.baseDir")) {
                return getAsciiDoctorBaseFolder();
            }
            if (t.equals("docusaurus.baseDir")) {
                return project.getDocusaurusBaseFolder();
            }
            if (t.startsWith("asciidoctor.")) {
                return getAsciiDoctorConfig().get(t.substring("asciidoctor.".length())).asString();
            }
            if (t.startsWith("docusaurus.")) {
                return project.getConfig().get(t.substring("docusaurus.".length())).asString();
            }
            if (t.startsWith("docusaurus.")) {
                return project.getConfig().get(t).asString();
            }
            return null;
        }
    };

    public Docusaurus2AsciiDoctor(DocusaurusProject project) {
        this.project = project;
    }

    public Docusaurus2AsciiDoctor(File project) {
        this.project = new DocusaurusProject(project.getPath());
    }

    public void run() {
        generateAdoc();
        generatePdf();
    }

    public void writeHeader(PrintStream out) {
        out.println("= " + project.getTitle());
        LJSON[] h = getAsciiDoctorConfig().get("pdf/headers").arrayMembers();
        for (LJSON jsonItem : h) {
            out.println(jsonItem.asString());
        }
        out.println();
    }

    private File toCanonicalFile(File path) {
        try {
            return path.getCanonicalFile();
        } catch (IOException ex) {
            return path.getAbsoluteFile();
        }
    }
    private String toCanonicalPath(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (IOException ex) {
            return new File(path).getAbsolutePath();
        }
    }

    public File generatePdf() {
        String bin = getAsciiDoctorConfig().get("pdf/command/bin").asString();
        String[] args = getAsciiDoctorConfig().get("pdf/command/args").asStringArray();
        File workDir = toCanonicalFile(new File(project.getDocusaurusBaseFolder()));
        File dir = toCanonicalFile(new File(getAsciiDoctorBaseFolder()));
        if (args == null) {
            args = new String[0];
        }
        if (bin == null) {
            bin = "asciidoctor-pdf";
        }
        List<String> cmdList = new ArrayList<>();
        cmdList.add(StringUtils.replace(bin, placeHolderReplacer));
        cmdList.addAll(Arrays.asList(args).stream().map(x -> StringUtils.replace(x, placeHolderReplacer)).collect(Collectors.toList()));
        String pdfFileName = project.getProjectName() + ".pdf";
        pdfFile = new File(dir, pdfFileName);
        cmdList.add(adocFile.toString());
        ProcessBuilder pb = new ProcessBuilder(cmdList)
                .inheritIO()
                .redirectErrorStream(true)
                .directory(workDir);
        int result = 0;
        try {
            result = pb.start().waitFor();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        if (result != 0) {
            throw new RuntimeException("Pdf generation failed with error code:" + result);
        }
        return pdfFile;
    }

    public File generateAdoc() {
        try {
            LJSON asciidoctorConfig = getAsciiDoctorConfig();
            if (asciidoctorConfig == null) {
                System.err.println("missing customFields.asciidoctor in docusaurus.config.js file");
            }
            String asciiDoctorBaseFolder = getAsciiDoctorBaseFolder();
            String pn = project.getProjectName();
            if (pn == null || pn.isEmpty()) {
                pn = "docusaurus";
            }
            adocFile = toCanonicalFile(new File(asciiDoctorBaseFolder + "/" + pn + ".adoc"));
            PrintStream out = new PrintStream(adocFile);
            AsciiDoctorWriter asciiDoctorWriter = new AsciiDoctorWriter(out);
            writeHeader(out);
            for (Map.Entry<String, List<Doc>> entry : project.getDocs().entrySet()) {
                out.println();
//                out.println("# " + entry.getKey());
                out.println("");
                out.println("= " + entry.getKey());

//                out.println("\n"
//                        + "[partintro]\n"
//                        + "--\n"
//                        + "This is the introduction to the first part of our mud-encrusted journey.\n"
//                        + "-- \n"
//                );
                out.println("");

                for (Doc item : entry.getValue()) {
                    DocusaurusMdParser p = new DocusaurusMdParser(Files.newBufferedReader(item.path));
                    MdElement tree = p.parse();
                    if (tree != null) {
                        tree = new TransformImpl().transformDocument(tree);
                        if (tree != null) {
                            out.println();
                            out.println("== " + item.title);
                            asciiDoctorWriter.write(tree);
                        }
                    }
                }
            }
            out.close();
            return adocFile;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private String getAsciiDoctorBaseFolder() {
        String s = getAsciiDoctorConfig().get("path").asString();
        if (!new File(s).isAbsolute()) {
            s = project.getDocusaurusBaseFolder() + "/" + s;
        }
        return s;
    }

    private LJSON getAsciiDoctorConfig() {
        return project.getConfig().get("customFields/asciidoctor");
    }

    private static class TransformImpl extends MdElementTransformBase {

        public TransformImpl() {
        }

        @Override
        protected MdElement transformTitle(MdTitle t, MdElementPath parentPath) {
            if (t.getDepth() < 6) {
                return new MdTitle(t.getCode(), t.getValue(), t.getDepth() + 1);
            }
            return t;
        }

        @Override
        public MdElement transformDocument(MdElement e) {
            if (e instanceof MdSequence) {
                MdSequence s = (MdSequence) e;
                MdElement[] content = s.getContent();

                if (content.length > 0 && content[0] instanceof MdLineSeparator) {
                    int x = 0;
                    for (int i = 1; i < content.length; i++) {
                        if (content[i] instanceof MdLineSeparator) {
                            x = i + 1;
                            break;
                        }
                    }
                    List<MdElement> a = new ArrayList<>();
                    for (int i = x; i < content.length; i++) {
                        a.add(content[i]);
                    }
                    for (Iterator<MdElement> it = a.iterator(); it.hasNext();) {
                        MdElement mdElement = it.next();
                        if (mdElement instanceof MdText) {
                            String t = ((MdText) mdElement).getText();
                            if (t.length() == 0) {
                                it.remove();
                            } else if (t.startsWith("import ")) {
                                it.remove();
                            } else {
                                break;
                            }
                        }
                    }
                    return super.transformDocument(MdFactory.append(a));
                }
            }
            return super.transformDocument(e);
        }

        @Override
        protected MdElement transformXml(MdXml e, MdElementPath parentPath) {
            switch (e.getTag()) {
                case "Tabs": {
                    return transformElement(e.getContent(), parentPath.append(e));
                }
                case "TabItem": {
                    String tt = "Unknown";
                    LJSON v = LJSON.of(e.getProperties().get("value"));
                    if (v != null) {
                        tt = v.asString();
                    }
                    for (LJSON a : LJSON.of(parentPath.getParent().getItem().asXml().getProperties().get("values")).arrayMembers()) {
                        if (tt.equals(a.get("value").asString())) {
                            tt = a.get("label").asString();
                            break;
                        }
                    }
                    if (tt.equals("C#")) {
                        tt = "C Sharp";
                    }
                    return new MdSequence("", new MdElement[]{
                        new MdTitle("#####", tt, 5),
                        transformElement(e.getContent(), parentPath.append(e))
                    }, false);
                }

            }
            return e;
        }

    }

}
