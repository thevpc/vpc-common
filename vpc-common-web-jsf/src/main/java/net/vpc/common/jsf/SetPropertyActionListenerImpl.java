/*
 * To change this license header, choose License Headers in Project Properties.
 *
 * and open the template in the editor.
 */
package net.vpc.common.jsf;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

/**
 *
 * @author taha.bensalah@gmail.com
 */
public class SetPropertyActionListenerImpl implements ActionListener, StateHolder {
    private ValueExpression target;
    private ValueExpression source;

    // ------------------------------------------------------------ Constructors
    public SetPropertyActionListenerImpl() {
    }

    public SetPropertyActionListenerImpl(ValueExpression target, ValueExpression value) {
        this.target = target;
        this.source = value;
    }

    // --------------------------------------------- Methods from ActionListener
    public void processAction(ActionEvent e) throws AbortProcessingException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        try {
            Object value = source.getValue(elContext);
            if (value != null) {
                ExpressionFactory factory = facesContext.getApplication().getExpressionFactory();
                value = factory.coerceToType(value, target.getType(elContext));
            }
            target.setValue(elContext, value);
        } catch (ELException ele) {
            throw new AbortProcessingException(ele);
        }
    }

    // ------------------------------------------------ Methods from StateHolder
    public void setTransient(boolean trans) {
    }

    public boolean isTransient() {
        return false;
    }

    public Object saveState(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        Object[] state = new Object[2];
        state[0] = target;
        state[1] = source;
        return state;
    }

    public void restoreState(FacesContext context, Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        if (state == null) {
            return;
        }
        Object[] stateArray = (Object[]) state;
        target = (ValueExpression) stateArray[0];
        source = (ValueExpression) stateArray[1];
    }
    
}
