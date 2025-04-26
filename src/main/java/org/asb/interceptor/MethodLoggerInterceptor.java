package org.asb.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class MethodLoggerInterceptor {
	
	@AroundInvoke
	public Object logger(InvocationContext inv) throws Exception {
		return inv.proceed();
	}

}
