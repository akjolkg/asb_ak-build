package org.asb.conversiation;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * 
 * @author Akzholbek OMorov
 *
 */
public abstract class Conversational implements Serializable {
	
	private static final long serialVersionUID = 282062606859800901L;
	
	@Inject
	private Conversation conversation;
	private Object data;
	
	@PostConstruct
	public void initialize() {
		if(conversation.isTransient()) conversation.begin();
		if(FacesContext.getCurrentInstance().getExternalContext().getFlash().get("filtersData") != null) data = FacesContext.getCurrentInstance().getExternalContext().getFlash().get("filtersData");
	}
	
    public void closeConversation() {
    	if(!conversation.isTransient()) conversation.end();
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("filtersData", data);
	}
    
    public void setData(Object data) {
		this.data = data;
	}
    
    public Object getData() {
		return data;
	}
    
    public String getId() {
		return conversation.getId();
	}

}
