package net.vpc.common.webxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WebXmlVisitorAdapter implements WebXmlVisitor {
    @Override
    public void visitStartDocument(Document document) {

    }

    @Override
    public void visitEndDocument(Document document, WebXml pom) {

    }

    @Override
    public void visitStartServlet(Element servletElement) {

    }

    @Override
    public void visitEndServlet(Element servletElement, WebServlet servlet) {

    }

    @Override
    public void visitStartServletMapping(Element servletMappingElement) {

    }

    @Override
    public void visitEndServletMapping(Element servletMappingElement, WebServletMapping servletMapping) {

    }

    @Override
    public void visitStartFilter(Element filterElement) {

    }

    @Override
    public void visitEndFilter(Element filterElement, WebFilter filter) {

    }

    @Override
    public void visitStartFilterMapping(Element filterMappingElement) {

    }

    @Override
    public void visitEndFilterMapping(Element filterMappingElement, WebFilterMapping filterMapping) {

    }

    @Override
    public void visitStartListener(Element listenerElement) {

    }

    @Override
    public void visitEndListener(Element listenerElement, WebListener listener) {

    }

    @Override
    public void visitStartContextParam(Element contextElement) {

    }

    @Override
    public void visitEndContextParam(Element contextElement, String key, String val) {

    }

    @Override
    public void visitStartServletParam(Element servletParamElement, WebServlet servlet) {

    }

    @Override
    public void visitEndServletParam(Element servletParamElement, WebServlet servlet, String key, String val) {

    }

    @Override
    public void visitStartFilterParam(Element servletParamElement, WebFilter filter) {

    }

    @Override
    public void visitEndFilterParam(Element servletParamElement, WebFilter filter, String key, String val) {

    }
}
