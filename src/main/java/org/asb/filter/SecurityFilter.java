package org.asb.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asb.model.User;
import org.asb.util.web.LoginUtil;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

public class SecurityFilter implements Filter {
	
	private Map<String[], String> map;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		map = new HashMap<>();
		parseParam(config, "dolphin");
	}
	
	private void parseParam(FilterConfig config, String param){
		String url = config.getInitParameter(param);
		if(url == null) return;
		
		map.put(url.split("\\|"), param);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			String url = httpServletRequest.getRequestURI();
			String contentPath = httpServletRequest.getContextPath();
						
			if(url == null || url.isEmpty()) {
				chain.doFilter(request, response);
				return;
			}
			
			for (Entry<String[], String> entry : map.entrySet()) {
				for (String string : entry.getKey()) {
					if(url.matches("^" + contentPath + string + "$")){
						User user = (User)httpServletRequest.getSession().getAttribute(LoginUtil.CURRENT_USER_SESSION_KEY);
						if(user != null){
							chain.doFilter(request, response);
							return;
						}
						else {
							HttpServletResponse httpServletResponse = (HttpServletResponse) response;
							httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/view/user/login.xhtml");
						}
					}
				}
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		map = null;
	}
}