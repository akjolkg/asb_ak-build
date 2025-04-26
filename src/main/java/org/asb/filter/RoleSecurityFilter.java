package org.asb.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asb.model.User;


public class RoleSecurityFilter implements Filter {
	public static final String CURRENT_USER_SESSION_KEY = "org.asb.current_user_session_key";

	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			
			
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			User user = (User)httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION_KEY);
			
			
			if (user!=null && user.getRole() != null) {
				
			}
			
			
			if(isGlobal(httpServletRequest)){
				chain.doFilter(request, response);				
				return;
			}
			else if(user!=null && user.getRole() != null && isPrivateGlobal(httpServletRequest)){
				chain.doFilter(request, response);				
				return;
			}
			else if (user!=null && user.getRole() != null && 
					isAdminPage(httpServletRequest) && user.getRole().ordinal()==0) {
				chain.doFilter(request, response);				
				return;
				
			}else if (user!=null && user.getRole() != null && 
					isJuniorPage(httpServletRequest) && (user.getRole().ordinal()==2 || user.getRole().ordinal()==10)) {
				chain.doFilter(request, response);				
				return;
				
			} else if (user!=null && user.getRole() != null && 
					isOperatorPage(httpServletRequest) && user.getRole().ordinal()==5) {
				chain.doFilter(request, response);				
				return;
			} else if (user!=null && user.getRole() != null && 
					isKassirPage(httpServletRequest) && user.getRole().ordinal()==17) {
				chain.doFilter(request, response);				
				return;
				
			} else {
				HttpServletResponse httpServletResponse = (HttpServletResponse)response;
				System.out.println("user=="+user);
				System.out.println("request=="+httpServletRequest);
				System.out.println("Unuthorized user!!!");
				httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/view/user/login.xhtml");			
			}
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {}
	
	
	private boolean isGlobal(final HttpServletRequest httpServletRequest){
		boolean global=false;
		global=(httpServletRequest.getServletPath() != null && 
				(httpServletRequest.getServletPath().startsWith("/view/user/")||
				httpServletRequest.getServletPath().contains("javax.faces.resource")||
				httpServletRequest.getServletPath().contains("favicon.ico")||
				httpServletRequest.getServletPath().contains("/faces_resources")));
		return global;
	}
	
	private boolean isPrivateGlobal(final HttpServletRequest httpServletRequest){
		boolean global=false;
		global=(httpServletRequest.getServletPath() != null && 
				httpServletRequest.getServletPath().contains("download")||
				httpServletRequest.getServletPath().contains("jasper")||
				httpServletRequest.getServletPath().contains("client")||
				httpServletRequest.getServletPath().contains("forecast")||
				httpServletRequest.getServletPath().contains("garant")||
				httpServletRequest.getServletPath().contains("responsiblePerson")||
				httpServletRequest.getServletPath().contains("deletedReserve")||
				httpServletRequest.getServletPath().contains("payment")||
				httpServletRequest.getServletPath().contains("schedules")||
				httpServletRequest.getServletPath().contains("debtors")||
				httpServletRequest.getServletPath().contains("user")||
				httpServletRequest.getServletPath().contains("appartment")||
				httpServletRequest.getServletPath().contains("report")||
				httpServletRequest.getServletPath().contains("offices")||
				httpServletRequest.getServletPath().contains("garages")||
				httpServletRequest.getServletPath().contains("denouncement")||
				httpServletRequest.getServletPath().contains("scheduleTemplate")||
				httpServletRequest.getServletPath().contains("contractTemplate")||
				httpServletRequest.getServletPath().contains("construction")||
				httpServletRequest.getServletPath().contains("company")||
				httpServletRequest.getServletPath().contains("log")||
				httpServletRequest.getServletPath().contains("main")
				);
		return global;
	}

	private boolean isAdminPage(final HttpServletRequest httpServletRequest) {
		return (httpServletRequest.getServletPath() != null && (httpServletRequest.getServletPath().contains("admin")||httpServletRequest.getServletPath().contains("main")));
	}
	private boolean isJuniorPage(final HttpServletRequest httpServletRequest) {
		return (httpServletRequest.getServletPath() != null && (httpServletRequest.getServletPath().contains("controller")||httpServletRequest.getServletPath().contains("main")));
	}
	private boolean isOperatorPage(final HttpServletRequest httpServletRequest) {
		return (httpServletRequest.getServletPath() != null && (httpServletRequest.getServletPath().contains("operator")||httpServletRequest.getServletPath().contains("main")));
	}
	private boolean isKassirPage(final HttpServletRequest httpServletRequest) {
		return (httpServletRequest.getServletPath() != null && (httpServletRequest.getServletPath().contains("kassir")||httpServletRequest.getServletPath().contains("main")));
	}
	

}
