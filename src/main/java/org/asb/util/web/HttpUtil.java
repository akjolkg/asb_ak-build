package org.asb.util.web;

import javax.servlet.http.HttpServletRequest;

import org.asb.singleton.Configuration;


/***
 * 
 * @author Kuttubek Aidaraliev
 *
 */

public class HttpUtil {
	
	public static String getContextUrl(HttpServletRequest req) {
	    String contextPath = req.getContextPath();   // /mywebapp
	    
	    String url = Configuration.getInstance().getProperty("url");
	    if(contextPath != null) url += contextPath;
	    return url;
	}

}
