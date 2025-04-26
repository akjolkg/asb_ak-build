package org.asb.util.web;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.asb.enums.ScopeConstants;

/***
 * 
 * @author Akzholbek Omorov
 *
 */

public class FacesScopeQualifier implements ScopeQualifier {

	@SuppressWarnings("unchecked")
	public <U> U getValue(String name, ScopeConstants scope) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		U u = null;
		
		switch (scope) {
		case REQUEST_SCOPE:
			u = (U)externalContext.getRequestMap().get(name);
			break;
		case FLASH_SCOPE:
			u = (U)externalContext.getFlash().get(name);
			break;
		case SESSION_SCOPE:
			u = (U)externalContext.getSessionMap().get(name);
			break;
		case APPLICATION_SCOPE:
			u = (U)((ServletContext)externalContext.getContext()).getAttribute(name);
			break;
		default:
			break;
		}
		
		return u;
	}
	
	public <U> void setValue(String name, U u, ScopeConstants scope) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		switch (scope) {
		case REQUEST_SCOPE:
			externalContext.getRequestMap().put(name, u);
			break;
		case FLASH_SCOPE:
			externalContext.getFlash().put(name, u);
			break;
		case SESSION_SCOPE:
			externalContext.getSessionMap().put(name, u);
			break;
		case APPLICATION_SCOPE:
			((ServletContext)externalContext.getContext()).setAttribute(name, u);
			break;
		default:
			break;
		}
	}
	
	public void remove(String name, ScopeConstants scope) {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		
		switch (scope) {
		case REQUEST_SCOPE:
			externalContext.getRequestMap().remove(name);
			break;
		case FLASH_SCOPE:
			externalContext.getFlash().remove(name);
			break;
		case SESSION_SCOPE:
			externalContext.getSessionMap().remove(name);
			break;
		case APPLICATION_SCOPE:
			((ServletContext)externalContext.getContext()).removeAttribute(name);
			break;
		default:
			break;
		}
	}
	
	public HttpSession getSession() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		return (HttpSession) externalContext.getSession(false);
	}
}