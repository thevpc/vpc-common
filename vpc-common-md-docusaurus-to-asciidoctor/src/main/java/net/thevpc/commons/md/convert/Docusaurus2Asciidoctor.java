/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.commons.md.convert;

import java.io.File;
import net.thevpc.commons.md.docusaurus.DocusaurusProject;

import java.nio.file.Path;
import java.nio.file.Paths;

import net.thevpc.commons.ljson.LJSON;

/**
 *
 * @author vpc
 */
public class Docusaurus2Asciidoctor {

    protected Path adocFile;
    protected Path pdfFile;
    protected boolean generatePdf;
    protected DocusaurusProject project;

    public Docusaurus2Asciidoctor(DocusaurusProject project) {
        this.project = project;
    }

    public Docusaurus2Asciidoctor(File project) {
        this.project = new DocusaurusProject(project.getPath());
    }

    public void run() {
        Docusaurus2Adoc d2a = new Docusaurus2Adoc(project);
        String asciiDoctorBaseFolder = getAsciiDoctorBaseFolder();
        String pn = project.getProjectName();
        if (pn == null || pn.isEmpty()) {
            pn = "docusaurus";
        }
        adocFile = toCanonicalFile(Paths.get(asciiDoctorBaseFolder).resolve(pn + ".adoc"));
        d2a.run(adocFile);
        pdfFile = new Adoc2Pdf().generatePdf(getAdoc2PdfConfig());
    }

    private Path toCanonicalFile(Path path) {
        return path.toAbsolutePath().normalize();
    }

    public Adoc2PdfConfig getAdoc2PdfConfig() {
        Adoc2PdfConfig config = new Adoc2PdfConfig();
        LJSON asciiDoctorConfig = getAsciiDoctorConfig();
        config.setBin(asciiDoctorConfig.get("pdf/command/bin").asString());
        config.setArgs(asciiDoctorConfig.get("pdf/command/args").asStringArray());
        config.setWorkDir(toCanonicalFile(Paths.get(project.getDocusaurusBaseFolder())).toString());
        config.setBaseDir(toCanonicalFile(Paths.get(getAsciiDoctorBaseFolder())).toString());
        config.setInputAdoc(adocFile.toString());
        LJSON output = asciiDoctorConfig.get("pdf/output");
        String pdfFile=project.getProjectName();
        if(output.isString()){
            String s=output.asString().trim();
            if(!s.isEmpty()){
                if(s.endsWith("/") ||s.endsWith("\\")){
                    s+=project.getProjectName()+".pdf";
                }
                pdfFile=s;
            }
        }else{
            pdfFile=project.getProjectName()+".pdf";
        }
        pdfFile=toCanonicalFile(Paths.get(pdfFile)).toString();
        config.setOutputPdf(pdfFile);
        config.setPlaceHolderReplacer((String varName) -> {
            if (varName.equals("asciidoctor.baseDir")) {
                return getAsciiDoctorBaseFolder();
            }
            if (varName.equals("docusaurus.baseDir")) {
                return project.getDocusaurusBaseFolder();
            }
            if (varName.startsWith("asciidoctor.")) {
                return asciiDoctorConfig.get(varName.substring("asciidoctor.".length())).asString();
            }
            if (varName.startsWith("docusaurus.")) {
                return project.getConfig().get(varName.substring("docusaurus.".length())).asString();
            }
            //if (varName.startsWith("docusaurus.")) {
                return project.getConfig().get(varName).asString();
            //}
            //return null;
        });
        return config;
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

}
