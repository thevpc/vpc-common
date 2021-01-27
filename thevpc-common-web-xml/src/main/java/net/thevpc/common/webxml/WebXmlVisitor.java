package net.thevpc.common.webxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface WebXmlVisitor {
    void visitStartDocument(Document document);

    void visitEndDocument(Document document, WebXml pom);

    void visitStartServlet(Element servletElement);

    void visitEndServlet(Element servletElement, WebServlet servlet);

    void visitStartServletMapping(Element servletMappingElement);

    void visitEndServletMapping(Element servletMappingElement, WebServletMapping servletMapping);

    void visitStartFilter(Element filterElement);

    void visitEndFilter(Element filterElement, WebFilter filter);

    void visitStartFilterMapping(Element filterMappingElement);

    void visitEndFilterMapping(Element filterMappingElement, WebFilterMapping filterMapping);

    void visitStartListener(Element listenerElement);

    void visitEndListener(Element listenerElement, WebListener listener);

    void visitStartContextParam(Element contextElement);

    void visitEndContextParam(Element contextElement, String key,String val);

    void visitStartServletParam(Element servletParamElement,WebServlet servlet);

    void visitEndServletParam(Element servletParamElement,WebServlet servlet, String key,String val);

    void visitStartFilterParam(Element servletParamElement,WebFilter filter);

    void visitEndFilterParam(Element servletParamElement,WebFilter filter, String key,String val);
}
