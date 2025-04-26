package org.asb.controller.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.asb.model.User;
import org.asb.util.web.LoginUtil;

/***
 * 
 * @author Akzholbek Omorov
 * 
 */
@ManagedBean
@RequestScoped
public class RoleController {

    @Inject
    private LoginUtil loginUtil;
    private Boolean mobile;
    
    public boolean isAllowed(String role) {
    	List<String> roles=new ArrayList<>(Arrays.asList(role.split(",")));
    	if(loginUtil.getCurrentUser()==null) return false;
    	return roles.contains(loginUtil.getCurrentUser().getRole().toString());
    	
    }
    
    public User getCurrentUser(){
    	return loginUtil.getCurrentUser();
    }


	public Boolean getMobile() {
		mobile=false;
		 HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	     String userAgent = req.getHeader("user-agent");
	     System.out.println("userAgent==="+userAgent);
	     if(userAgent.contains("ndroid")||userAgent.contains("obile")||userAgent.contains("phone"))
	    	 mobile=true;
		
		
		return mobile;
	}

	public void setMobile(Boolean mobile) {
		this.mobile = mobile;
	}
    
    
    
}
