package org.asb.controller.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage; 
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.enums.LogType;
import org.asb.enums.ObjectType;
import org.asb.enums.ScopeConstants;
import org.asb.enums.UserStatus;
import org.asb.model.Log;
import org.asb.model.User;
import org.asb.service.LogService;
import org.asb.service.UserService;
import org.asb.util.Digest;
import org.asb.util.web.FacesScopeQualifier;
import org.asb.util.web.LoginUtil;
import org.asb.util.web.Messages;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.Visibility;

/***
 * 
 * @author Akzholbek Omorov
 * 
 */
@ManagedBean
@RequestScoped
public class UserController {

	@EJB
	private UserService userService;
	@EJB
	private LogService logService;

    private String username;
    private String password;
    public static final String CURRENT_USER_SESSION_KEY = "org.asb.current_user_session_key";
    
    private DualListModel<String> fields;
    
    
    @Inject
    private LoginUtil loginUtil;
    
    public void register() {
    	
       
    }
    
    
    public String saveFields() throws IOException{
    	User user=loginUtil.getCurrentUser();
    	user.setFields(new HashSet<String>());
    	user.getFields().addAll(fields.getTarget());
    	user=userService.merge(user);
    	loginUtil.setCurrentUser(user);
    	
    	System.out.println("redirecting to main page");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/main.xhtml");
		return "";
    }
    
    public String cancel() throws IOException{
    	System.out.println("redirecting to main page");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/main.xhtml");
		return "";
    }
    
    public String login() throws Exception{
    	Date d = new Date();
    	String year=String.format("%ty",d);
	    System.out.println("year="+year);
	    
	    
	    
	    
	    
    	if( username.equals("") ) {
    		return null;
		} else if ( password.equals("") ) {
			return null;
		}
		
    	List<FilterExample> examples = new ArrayList<FilterExample>();
		examples.add(new FilterExample("username", username, InequalityConstants.EQUAL, true));	
		examples.add(new FilterExample("password", password, InequalityConstants.EQUAL));
		List<User> userList = userService.findByExample(0, 1, SortEnum.ASCENDING, examples, "id");
		if(userList.isEmpty()){
			FacesContext.getCurrentInstance().addMessage("login-form", new FacesMessage( FacesMessage.SEVERITY_ERROR,  Messages.getMessage("usernameOrPasswordIncorrect"), null) );
			return null;
		}
			
		User user = userList.get(0);
		user=userService.findByProperty("id", user.getId()).get(0);
		
		
		if(user.getStatus() == null || user.getStatus().equals(UserStatus.INACTIVE)){
			FacesContext.getCurrentInstance().addMessage("login-form", new FacesMessage( FacesMessage.SEVERITY_ERROR,  Messages.getMessage("userIsNotActive"), null) );
			FacesContext.getCurrentInstance().getExternalContext().redirect("blocked.xhtml");
			
			return null;
		}
		if(user.getStatus() == null || user.getStatus().equals(UserStatus.BLOCKED)){
			FacesContext.getCurrentInstance().addMessage("login-form", new FacesMessage( FacesMessage.SEVERITY_ERROR,  Messages.getMessage("userIsBlocked"), null) );
			return null;
		}
		
		
		if(user.getFields()==null||user.getFields().size()<1){
			user.setFields(new HashSet<String>());
			List<String> fieldsSource = new ArrayList<String>();
			fieldsSource.add("№");
			fieldsSource.add("Этаж");
			fieldsSource.add("Строительный объект");
			fieldsSource.add("Предварительный номер");
			fieldsSource.add("Количество комнат");
			fieldsSource.add("Общая площадь ориентировочно");
			fieldsSource.add("Ф.И.О.");
			fieldsSource.add("Телефон");
			fieldsSource.add("Примечание");
			fieldsSource.add("Примечание куратора");
			fieldsSource.add("Статус");
			fieldsSource.add("Куратор");
			fieldsSource.add("Действия");
	    	user.getFields().addAll(fieldsSource);
	    	user=userService.merge(user);
		}
		
		
		loginUtil.setCurrentUser(user);
		
		Log log=new Log();
		log.setObjectType(ObjectType.USER);
		log.setType(LogType.LOGIN);
		log.setUser(user);
		logService.persist(log);
		
		
		
		System.out.println("user logged="+user);
		
		new FacesScopeQualifier().setValue("org.infosystema.projects.votingsystem.current_user_fio", user.getFio(), ScopeConstants.SESSION_SCOPE);
		System.out.println("redirecting to main page");
		FacesContext.getCurrentInstance().getExternalContext().redirect("/asb/view/main.xhtml");
		
		return "";
	
    	
    }
    
    public String logout() {
		loginUtil.logout();
		System.out.println("logouuuuut");
		return "/view/user/login.xhtml";
	}
    
    
    public String settings() {
		return "/view/user/settings.xhtml";
	}
    
    public String home() {
		return "/view/main.xhtml";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void onToggle(ToggleEvent e) {
		List<String> fieldsSource = new ArrayList<String>();
		fieldsSource.add("№");
		fieldsSource.add("Этаж");
		fieldsSource.add("Строительный объект");
		fieldsSource.add("Предварительный номер");
		fieldsSource.add("Постоянный номер, согласно техпаспорту");
		fieldsSource.add("Количество комнат");
		fieldsSource.add("Общая площадь ориентировочно");
		fieldsSource.add("Общая площадь по техпаспорту");
		fieldsSource.add("Дата договора");
		fieldsSource.add("Ф.И.О.");
		fieldsSource.add("Паспортные данные");
		fieldsSource.add("Адрес по паспорту");
		fieldsSource.add("Цена за 1 м.кв.");
		fieldsSource.add("Сумма договора");
		fieldsSource.add("Оплачено");
		fieldsSource.add("Остаток на оплату");
		fieldsSource.add("Остаток после инвентеризации");
		fieldsSource.add("Телефон");
		fieldsSource.add("Примечание");
		fieldsSource.add("Примечание куратора");
		fieldsSource.add("Примечание куратора ОРК");		
		fieldsSource.add("Тип сделки");
		fieldsSource.add("Тип клиента");
		fieldsSource.add("КП|Оф.");
		fieldsSource.add("Статус");
		fieldsSource.add("Куратор ОП");
		fieldsSource.add("Куратор ОРК");
		fieldsSource.add("Действия");
		
		
		if(e.getVisibility().equals(Visibility.VISIBLE)){			
			User user=loginUtil.getCurrentUser();
			user.getFields().add(fieldsSource.get((Integer)e.getData()));
			user=userService.merge(user);
			loginUtil.setCurrentUser(user);
		}
		if(e.getVisibility().equals(Visibility.HIDDEN)){
			User user=loginUtil.getCurrentUser();
			user.getFields().remove(fieldsSource.get((Integer)e.getData()));
			user=userService.merge(user);
			loginUtil.setCurrentUser(user);
		}
		
	}
	
	
	public DualListModel<String> getFields() {
		if(fields==null){
			List<String> fieldsSource = new ArrayList<String>();
			List<String> fieldsTarget = new ArrayList<String>();
			fieldsSource.add("№");
			fieldsSource.add("Этаж");
			fieldsSource.add("Строительный объект");
			fieldsSource.add("Предварительный номер");
			fieldsSource.add("Постоянный номер, согласно техпаспорту");
			fieldsSource.add("Количество комнат");
			fieldsSource.add("Общая площадь ориентировочно");
			fieldsSource.add("Общая площадь по техпаспорту");
			fieldsSource.add("Дата договора");
			fieldsSource.add("Ф.И.О.");
			fieldsSource.add("Паспортные данные");
			fieldsSource.add("Адрес по паспорту");
			fieldsSource.add("Цена за 1 м.кв.");
			fieldsSource.add("Сумма договора");
			fieldsSource.add("Оплачено");
			fieldsSource.add("Остаток на оплату");
			fieldsSource.add("Остаток после инвентеризации");
			fieldsSource.add("Телефон");
			fieldsSource.add("Примечание");
			fieldsSource.add("Примечание куратора");
			fieldsSource.add("Примечание куратора ОРК");		
			fieldsSource.add("Тип сделки");
			fieldsSource.add("Тип клиента");
			fieldsSource.add("КП|Оф.");
			fieldsSource.add("Статус");
			fieldsSource.add("Куратор ОП");
			fieldsSource.add("Куратор ОРК");
			fieldsSource.add("Действия");
	        if(loginUtil.getCurrentUser().getFields()!=null){
	        	fieldsTarget.addAll(loginUtil.getCurrentUser().getFields());
	        	fieldsSource.removeAll(fieldsTarget);
	        }	
	        fields = new DualListModel<String>(fieldsSource, fieldsTarget);
		}
		return fields;
	}

	public void setFields(DualListModel<String> fields) {
		this.fields = fields;
	}

	public LoginUtil getLoginUtil() {
		return loginUtil;
	}
}
