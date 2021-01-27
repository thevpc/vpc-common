/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.thevpc.common.jsf;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.component.UIOutput;
import javax.faces.event.MethodExpressionActionListener;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author taha.bensalah@gmail.com
 */
public class FacesUtils {

    public static UIOutput createVerbatimUIOutput(String html) {
        UIOutput verbatim = new UIOutput();
        verbatim.setRendererType("javax.faces.Text");
        verbatim.getAttributes().put("escape", false);
        verbatim.setValue(html);
        return verbatim;
    }

    public static SelectItem createSelectItem(String value, String label) {
        return createSelectItem(value, label, null, null, null, false);
    }

    public static SelectItem createSelectItem(String value, String label, String styleClass) {
        return createSelectItem(value, label, styleClass, null, null, false);
    }

    public static SelectItem createSelectItem(String value, String label, String styleClass, String style, String desc, boolean disabled) {
        StringBuilder sb = new StringBuilder();
        if (style != null && style.length() > 0) {
            sb.append(" style='").append(style).append('\'');
        }
        if (styleClass != null && styleClass.length() > 0) {
            sb.append(" class='").append(styleClass).append('\'');
        }
        if (sb.length() > 0) {
            sb.insert(0, "<span ");
            sb.append(">");
            sb.append(StringEscapeUtils.escapeHtml4(label));
            sb.append("</span>");
            return new SelectItem(value, sb.toString(), desc, disabled, false);
        } else {
            return new SelectItem(value, label, desc, disabled, true);
        }
    }

    public static Object evaluateEL(String p_expression) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();
        ValueExpression vex = expressionFactory.createValueExpression(elContext, p_expression, String.class);
        return vex.getValue(elContext);
    }

    public static SetPropertyActionListenerImpl createSetPropertyActionListener(String expression, String value, Class type) {
        return new SetPropertyActionListenerImpl(createValueExpression(expression, type),
                createValueExpression(value, type));
    }

    public static MethodExpressionActionListener createActionListener(String expression, Class... expectedParameterTypes) {
        return new MethodExpressionActionListener(createMethodExpression(expression, Void.class, expectedParameterTypes));
    }

    public static MethodExpression createMethodExpression(String expression, Class returnType, Class... expectedParameterTypes) {
        return FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), expression, returnType, expectedParameterTypes);
    }

    public static ValueExpression createValueExpression(String expression, Class returnType) {
        return FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), expression, returnType);
    }

    public static ServletContext getServletContext() {
        return (ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext();
    }

    public static String getRootViewId() {
        return FacesContext.getCurrentInstance().getViewRoot().getViewId();
    }

    public static void valueBinding(String param, String code, UIComponent Nom1) {
        String valueBinding = "#{" + code + "}";
        ValueBinding createValueBinding = FacesContext.getCurrentInstance()
                .getApplication().createValueBinding(valueBinding);
        ValueBinding myBinding = createValueBinding;
        Nom1.setValueBinding(param, myBinding);
    }

    public static ExternalContext getExternalContext() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.getExternalContext();
    }

    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(create);
    }

    public static HttpServletRequest getHttpRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
    }

    /**
     * Get managed bean based on the bean name.
     *
     * @param beanName the bean name
     * @return the managed bean associated with the bean name
     */
    public static Object getManagedBean(String beanName) {

        return getValueBinding(getJsfEl(beanName)).getValue(
                FacesContext.getCurrentInstance());
    }

    /**
     * Remove the managed bean based on the bean name.
     *
     * @param beanName the bean name of the managed bean to be removed
     */
    public static void resetManagedBean(String beanName, Object o) {
        getValueBinding(getJsfEl(beanName)).setValue(
                FacesContext.getCurrentInstance(), o);
    }

    /**
     * Store the managed bean inside the session scope.
     *
     * @param beanName the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInSession(String beanName,
            Object managedBean) {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance == null) {
            return;
        }
        currentInstance.getExternalContext().getSessionMap()
                .put(beanName, managedBean);
    }

    public static void redirect(String url) throws IOException {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance == null) {
            return;
        }
        currentInstance.getExternalContext().redirect(currentInstance.getExternalContext().getRequestContextPath() + url);
    }

    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static String getRequestParameter(String name) {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance == null) {
            return null;
        }
        return currentInstance.getExternalContext()
                .getRequestParameterMap().get(name);
    }

    public static String setRequestParameter(String key, String value) {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance == null) {
            return null;
        }
        return currentInstance.getExternalContext()
                .getRequestParameterMap().put(key, value);
    }

    public static boolean getMessagesPresent() {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance == null) {
            return false;
        }
        Iterator<FacesMessage> itr = currentInstance
                .getMessages();
        return itr != null && itr.hasNext();
    }

    public static void addInfoMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_INFO, msg);
    }

    public static void addInfoMessage(String msg, String title) {
        addMessage(FacesMessage.SEVERITY_INFO, msg, title);
    }

    /**
     * Add information message to a specific client.
     *
     * @param title summary message
     * @param clientId the client id
     * @param msg the information message
     */
    public static void addInfoMessage(String msg, String title, String clientId) {
        addMessage(FacesMessage.SEVERITY_INFO, msg, title, clientId);

    }

    public static void addWarnMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_WARN, msg);
    }

    public static void addWarnMessage(String msg, String title) {
        addMessage(FacesMessage.SEVERITY_WARN, msg, title);
    }

    public static void addWarnMessage(String msg, String title, String clientId) {
        addMessage(FacesMessage.SEVERITY_WARN, msg, clientId);
    }

    public static void addErrorMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_ERROR, msg);
    }

    public static void addErrorMessage(String msg, String title) {
        addMessage(FacesMessage.SEVERITY_ERROR, msg, title);
    }

    public static void addErrorMessage(String msg, String title, String clientId) {
        addMessage(FacesMessage.SEVERITY_ERROR, msg, clientId);
    }

    private static String messageFromThrowable(Throwable th) {
        if (th == null) {
            return "Unknown Error";
        }
        String m = th.getMessage();
        if (m == null || m.trim().isEmpty()) {
            m = th.toString();
        }
        return m;
    }

    public static void addErrorMessage(Throwable msg) {
        addMessage(FacesMessage.SEVERITY_ERROR, messageFromThrowable(msg));
    }

    public static void addErrorMessage(Throwable msg, String title) {
        addMessage(FacesMessage.SEVERITY_ERROR, messageFromThrowable(msg), title);
    }

    public static void addErrorMessage(Throwable msg, String title, String clientId) {
        addMessage(FacesMessage.SEVERITY_ERROR, messageFromThrowable(msg), clientId);
    }

    public static void addFatalMessage(String msg) {
        addMessage(FacesMessage.SEVERITY_FATAL, msg);
    }

    public static void addFatalMessage(String msg, String title) {
        addMessage(FacesMessage.SEVERITY_FATAL, msg, title);
    }

    public static void addFatalMessage(String msg, String title, String clientId) {
        addMessage(FacesMessage.SEVERITY_FATAL, msg, clientId);
    }

    public static void addFatalMessage(Throwable msg) {
        addMessage(FacesMessage.SEVERITY_FATAL, messageFromThrowable(msg));
    }

    public static void addFatalMessage(Throwable msg, String title) {
        addMessage(FacesMessage.SEVERITY_FATAL, messageFromThrowable(msg), title);
    }

    public static void addFatalMessage(Throwable msg, String title, String clientId) {
        addMessage(FacesMessage.SEVERITY_FATAL, messageFromThrowable(msg), clientId);
    }

    public static void clearMessages() {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance == null) {
            return;
        }
        if (currentInstance != null) {
            Iterator<FacesMessage> msgIterator = currentInstance.getMessages();
            while (msgIterator.hasNext()) {
                msgIterator.next();
                msgIterator.remove();
            }
        }
    }

    private static Application getApplication() {
        ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder
                .getFactory(FactoryFinder.APPLICATION_FACTORY);
        return appFactory.getApplication();
    }

    private static ValueBinding getValueBinding(String el) {
        return getApplication().createValueBinding(el);
    }

    private static HttpServletRequest getServletRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
    }

    private static Object getElValue(String el) {
        return getValueBinding(el).getValue(FacesContext.getCurrentInstance());
    }

    private static String getJsfEl(String value) {
        return "#{" + value + "}";
    }

    public static void renderResponse() {
        FacesContext.getCurrentInstance().renderResponse();
    }

    public static void responseComplete() {
        FacesContext.getCurrentInstance().responseComplete();
    }

    public static UIComponent getComponent(String id) {
        return getComponent(FacesContext.getCurrentInstance().getViewRoot(), id);
    }

    public static UIComponent getComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }
        Iterator<UIComponent> kids = c.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = getComponent(kids.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public static MethodExpressionActionListener createActionListener(String actionListenerExpression) {
        FacesContext context = FacesContext.getCurrentInstance();
        return new MethodExpressionActionListener(context.getApplication().getExpressionFactory()
                .createMethodExpression(context.getELContext(), actionListenerExpression, null, new Class[]{ActionEvent.class}));
    }

    public static InputStream openStream(String path) {
        return ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(path);
    }

    public static String getRealRootFolder() {
        return ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/");
    }

    public static void invalidateSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

    public static void addMessage(FacesMessage.Severity severity, String msg, String title) {
        addMessage(severity, msg, title, null);
    }

    public static void addMessage(FacesMessage.Severity severity, String msg) {
        addMessage(severity, msg, null, null);
    }

    public static void addMessage(FacesMessage.Severity severity, String msg, String title, String clientId) {
        FacesContext currentInstance = FacesContext.getCurrentInstance();
        if (currentInstance == null) {
            return;
        }
        if (severity == null) {
            severity = FacesMessage.SEVERITY_INFO;
        }

        if (title == null || title.trim().isEmpty()) {
            if (severity == FacesMessage.SEVERITY_INFO) {
                title = "Information";
            } else if (severity == FacesMessage.SEVERITY_ERROR) {
                title = "Error";
            } else if (severity == FacesMessage.SEVERITY_FATAL) {
                title = "Fatal";
            } else if (severity == FacesMessage.SEVERITY_WARN) {
                title = "Attention";
            } else {
                title = "Information";
            }
        }
        if (msg == null || msg.trim().isEmpty()) {
            msg = "Sorry, i've forgotten what to say ...";
        }
        currentInstance.addMessage(clientId,
                new FacesMessage(severity, title, msg));

    }

}
