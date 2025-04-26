package org.asb.util.web;

import java.io.Serializable;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.security.auth.Subject;

import org.asb.enums.ScopeConstants;
import org.asb.model.User;
 
/**
 * 
 * @author Akzholbek Omorov
 *
 */
@ManagedBean(eager=true)
@ApplicationScoped
public class LoginUtil implements Serializable {
	
	private static final long serialVersionUID = 5910093770049428002L;
	public static final String CURRENT_USER_SESSION_KEY = "org.asb.current_user_session_key";
	public static final String CURRENT_USER_SESSION_ROLE_KEY = "org.asb.current_user_role.session_key";
	private static final Set<User>users=new HashSet<User>();
	
	
	public boolean userHasRole(User user, int roleName) {
		if(user == null || user.getRole() == null) return false;
		return user.getRole().ordinal()==roleName;
	}
	
	public boolean userHasRole(User user, int[] roleNames) {
		if(roleNames == null || user == null) return false;
		
		for (int role : roleNames) {
			if(userHasRole(user, role)) return true;
		}
		return false;		
	}
	
	public boolean userHasRole(Subject subject, int[] roleNames) {
		if(roleNames == null) return false;
		
		for (int role : roleNames) {
			for (Principal principal : subject.getPrincipals()) {
				if(principal.getName().equals(role)) return true;
			}
		}
			
		return false;		
	}
	
	public void setCurrentUser(User user) {
		new FacesScopeQualifier().setValue(CURRENT_USER_SESSION_KEY, user, ScopeConstants.SESSION_SCOPE);
		new FacesScopeQualifier().setValue(CURRENT_USER_SESSION_ROLE_KEY, user.getRole(), ScopeConstants.SESSION_SCOPE);
		Random rand = new Random();
		user.setRed(rand.nextInt(255));
		user.setBlue(rand.nextInt(255));
		user.setGreen(rand.nextInt(255));
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
		String strDate = dateFormat.format(new Date());  
		
		user.setLastLogin(strDate);
		user.setShortFio(shortener(user.getFio()));
		if(user.getShortFio()!=null && user.getShortFio().length()>3)
			user.setShortFio(user.getShortFio().substring(0, 3));
		
		getUsers().add(user);
		System.out.println("users==="+getUsers());
	}
	
	
	
	public String shortener(String fio) {
		String sh="";
	    String[] myName = fio.split(" ");
	    for (int i = 0; i < myName.length; i++) {
	        String s = myName[i].toUpperCase();
	        System.out.println(s.charAt(0));
	        sh=sh+s.charAt(0);
	    }
	    return sh;
	}
	
	
	public User getCurrentUser() {
		return new FacesScopeQualifier().getValue(CURRENT_USER_SESSION_KEY, ScopeConstants.SESSION_SCOPE);
	}
	
	public boolean isLogged() {
		User user = new FacesScopeQualifier().getValue(CURRENT_USER_SESSION_KEY, ScopeConstants.SESSION_SCOPE);
		return (user == null ? false : true);
	}
	
	public void logout() {
		new FacesScopeQualifier().getSession().invalidate();
	}

	public Set<User> getUsers() {
		System.out.println("asking usersss=="+users);
		return users;
	}

	

}
