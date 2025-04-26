package org.asb.util.web;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class ElResolver<T> {
	
	public ElResolver() {}
	
	public T evaluateValueExpressionAndGet(String expression, Class<T> class1) {
	       
        FacesContext context = FacesContext.getCurrentInstance();
        if(context == null) return null;
        ELContext elContext = context.getELContext( );
        Application application = context.getApplication( );
       
        ExpressionFactory expressionFactory = application.getExpressionFactory( );
        ValueExpression ve = expressionFactory.createValueExpression(elContext, expression, class1);
        @SuppressWarnings("unchecked")
		T t = (T) ve.getValue(elContext);
       
        return t;
    }

}
