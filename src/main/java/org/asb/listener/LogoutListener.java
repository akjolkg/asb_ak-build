package org.asb.listener;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.asb.util.web.LoginUtil;

@WebListener
public class LogoutListener implements HttpSessionListener {

	@Inject 
	private LoginUtil lm;
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // NOOP.
    }
 
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    		lm.getUsers().remove(event.getSession().getAttribute("org.asb.current_user_session_key"));        
    }

}