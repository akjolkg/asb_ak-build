package org.asb.util.web;

import javax.servlet.http.HttpSession;

import org.asb.enums.ScopeConstants;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public interface ScopeQualifier {
	
	public <U> U getValue(String name, ScopeConstants scope);
	public <U> void setValue(String name, U u, ScopeConstants scope);
	public void remove(String name, ScopeConstants scope);
	public HttpSession getSession();

}