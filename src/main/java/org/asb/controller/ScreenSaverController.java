package org.asb.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.enums.Role;
import org.asb.enums.ScopeConstants;
import org.asb.model.ClientDocument;
import org.asb.model.Company;
import org.asb.service.ClientDocumentService;
import org.asb.service.CompanyService;

import org.asb.util.web.FacesScopeQualifier;

import com.lowagie.text.Document;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ViewScoped
@Logged
public class ScreenSaverController implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private ClientDocument document;

	@EJB
	private ClientDocumentService service;

	public ScreenSaverController() {
	}

	@PostConstruct
	public void initialize() {
	}

	public ClientDocument getDocument() {
		if (document == null) {
			List<FilterExample> examples = new ArrayList<>();
			examples.add(new FilterExample("global", true, InequalityConstants.EQUAL));
			Random rn = new Random();
			if (service.countByExample(examples) > 0) {
				int answer = rn.nextInt(service.countByExample(examples).intValue());
				List<ClientDocument> docs = service.findByExample(answer, 1, examples);
				document = docs.get(0);
			}
		}

		return document;
	}

	public void setDocument(ClientDocument document) {
		this.document = document;
	}

}
