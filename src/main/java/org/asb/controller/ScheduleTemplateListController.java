package org.asb.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.controller.admin.ClassifierController;
import org.asb.data.ScheduleTemplateModel;
import org.asb.enums.ClientStatus;
import org.asb.enums.Role;
import org.asb.model.ScheduleTemplate;
import org.asb.model.User;
import org.asb.service.ScheduleTemplateService;
import org.asb.service.UserService;
import org.asb.util.web.LoginUtil;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.data.PageEvent;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@ManagedBean
@ViewScoped
@RolesAllowed(roles = 0)
public class ScheduleTemplateListController extends ClassifierController implements Serializable {

	private static final long serialVersionUID = 8475958315897562353L;
	private ScheduleTemplateModel model;
	private ScheduleTemplate ScheduleTemplate;

	@Inject
	private LoginUtil loginUtil;

	@EJB
	private ScheduleTemplateService service;
	@EJB
	private UserService userService;

	public ScheduleTemplateListController() {
	}

	@PostConstruct
	private void init() {
		restoreState();
		filterData();
	}

	public void filter() {
		first = 0;
		filterData();

	}

	public void filterData() {
		List<FilterExample> filters = new ArrayList<>();
		if (filterCompanies != null && !filterCompanies.isEmpty()) {
			filters.add(new FilterExample("client.appartment.construction.company", filterCompanies,
					InequalityConstants.IN));
		}

		if (filterConstructions != null && !filterConstructions.isEmpty()) {
			filters.add(
					new FilterExample("client.appartment.construction", filterConstructions, InequalityConstants.IN));
		}
		if (filterBlocks != null && !filterBlocks.isEmpty()) {
			filters.add(new FilterExample("client.appartment.blockNumber", filterBlocks, InequalityConstants.IN));
		}

		if (filterTitle != null && filterTitle.length() > 0) {
			filters.add(new FilterExample("client.appartment.title", "%" + filterTitle + "%", InequalityConstants.LIKE,
					true));
		}
		
		if (searchString != null && searchString.length() > 0) {
			filters.add(new FilterExample(true, "note", "%" + getSearchString() + "%", InequalityConstants.LIKE, true));
			filters.add(new FilterExample(true, "client.contractNumber", "%" + searchString + "%",InequalityConstants.LIKE, true));
			filters.add(new FilterExample(true, "client.fio", "%" + searchString + "%", InequalityConstants.LIKE, true));
			filters.add(new FilterExample(true, "client.uptFio", "%" + searchString + "%", InequalityConstants.LIKE, true));
			filters.add(new FilterExample(true, "client.contacts", "%" + searchString + "%", InequalityConstants.LIKE,true));
			filters.add(new FilterExample(true, "client.note", "%" + searchString + "%", InequalityConstants.LIKE, true));
			filters.add(new FilterExample(true, "client.whatsappNumber", "%" + searchString + "%",
					InequalityConstants.LIKE, true));
		}
		filters.add(new FilterExample("id", null, InequalityConstants.IS_NOT_NULL_SINGLE));
		if (loginUtil.getCurrentUser().getConstructions() != null
				&& !loginUtil.getCurrentUser().getConstructions().isEmpty()) {
			filters.add(new FilterExample("client.appartment.construction",
					loginUtil.getCurrentUser().getConstructions(), InequalityConstants.IN));
		}

		if (filterStatus != null && !filterStatus.isEmpty()) {
			filters.add(new FilterExample("status", filterStatus, InequalityConstants.IN));
		}
		if (filterCuratorOrks != null && !filterCuratorOrks.isEmpty()) {
			filters.add(new FilterExample("client.curatorOrk", filterCuratorOrks, InequalityConstants.IN));
		}

		model = new ScheduleTemplateModel(filters, service);
		saveState();
	}

	public void clearFilter() {
        super.clearFilter();
        filter();
    }
	
	public ScheduleTemplateModel getModel() {
		return model;
	}

	public void setModel(ScheduleTemplateModel model) {
		this.model = model;
	}

	public ScheduleTemplate getScheduleTemplate() {
		return ScheduleTemplate;
	}

	public void setScheduleTemplate(ScheduleTemplate ScheduleTemplate) {
		this.ScheduleTemplate = ScheduleTemplate;
	}

}
