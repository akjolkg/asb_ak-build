package org.asb.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class ProxyHandler implements InvocationHandler {
	
	public ProxyHandler(Object object) {
		value = object;
	}
	
	private Object value;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Method[] methods = value.getClass().getMethods();
		for (Method m : methods) {
			if(m.getName().equals(method.getName())) return m.invoke(value, args);
		}
		return null;
	}

}
