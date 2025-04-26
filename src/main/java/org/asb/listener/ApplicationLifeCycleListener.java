package org.asb.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.asb.singleton.Configuration;
import org.asb.util.MailSender;

/***
 * 
 * @author Akzholbek Omorov
 *
 */

public class ApplicationLifeCycleListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		MailSender.destroy();
	}

	public void contextInitialized(ServletContextEvent arg0) {
		Configuration.getInstance();
	}

}
