package org.asb.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.asb.annotation.Logged;
import org.asb.annotation.RolesAllowed;
import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.enums.Role;
import org.asb.enums.ScopeConstants;
import org.asb.model.Construction;

import org.asb.service.ConstructionService;

import org.asb.util.web.FacesScopeQualifier;
import org.primefaces.model.DualListModel;

/**
 * 
 * @author Akzholbek Omorov
 *
 */

@Named
@ConversationScoped
@Logged
@RolesAllowed(roles=0)
public class ConstructionCreator implements Serializable {

	private static final long serialVersionUID = 5651758429305872940L;
	private Construction construction;
	private DualListModel<String> fields;

	private Boolean block;
	
	@EJB
	private ConstructionService service;
	
	
	@Inject
	private Conversation conversation;
	
	
	public ConstructionCreator() {}
	
	@PostConstruct
	public void initialize() {
	if(conversation.isTransient()) conversation.begin();
	}

    public void closeConversation() {
		if(!conversation.isTransient()) conversation.end();
	}
	
	public String add() {
		construction=new Construction();
		return "form.xhtml?faces-redirect=true";
		
	}
	
	public String edit(Construction construction) {
		this.construction = construction;
		if(construction!=null && construction.getBlockText()!=null && construction.getBlockText().length()>0)
			block=true;
		return "form.xhtml?faces-redirect=true";
	}
	
	public String delete(Construction construction) {
		this.construction = construction;
		return "deleteConstruction.xhtml?faces-redirect=true";
	}
	
	public String cancel() {
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doSave() {
		
		if(block!=true) construction.setBlockText(null);
		construction = construction .getId() == null ? service.persist(construction) : service.merge(construction);
		if(fields!=null&& fields.getTarget()!=null) {
			construction.setFields(new HashSet<String>());
			construction.getFields().addAll(fields.getTarget());
			construction=service.merge(construction);
		}
		
		conversation.end();
		return "list.xhtml?faces-redirect=true";
	}
	
	public String doDelete() {
	
		service.remove(construction);
		closeConversation();
		return "list.xhtml?faces-redirect=true";
	}
		
	public Construction getConstruction() {
		return construction;
	}

	public void setConstruction(Construction construction) {
		this.construction = construction;
	}

	public DualListModel<String> getFields() {
		if(fields==null){
			List<String> fieldsSource = new ArrayList<String>();
			List<String> fieldsTarget = new ArrayList<String>();
			fieldsSource.add("системой энергопитания напряжением 220 вольт");
			fieldsSource.add("UTP кабель для интернета (проводится до каждого этажа подъездов)");
			fieldsSource.add("аудио домофон");
			fieldsSource.add("видео домофон");
			fieldsSource.add("электросчетчик");
			fieldsSource.add("системой холодного водоснабжения");
			fieldsSource.add("системой горячего водоснабжения");
			fieldsSource.add("счетчиком холодной воды");
			fieldsSource.add("счетчиком горячей воды");
			fieldsSource.add("системой канализации");
			fieldsSource.add("системой отопления");
			fieldsSource.add("системой газоснабжения");
			fieldsSource.add("газовым счетчиком");
			fieldsSource.add("гидроизоляцией санузлов");
			fieldsSource.add("электропроводкой в соответствии с проектной документацией");
			fieldsSource.add("системой вытяжной вентиляции в ванной комнате, туалете и кухне");
			fieldsSource.add("наружными окнами, согласно проекту");
			fieldsSource.add("отштукатуренными стенами");
			fieldsSource.add("стяжкой пола (возможно с открытыми участками под трубопроводы)");
			fieldsSource.add("подводкой труб (ванная, туалет, кухня)");
			fieldsSource.add("радиаторы отопления");
			fieldsSource.add("входная дверь");
			fieldsSource.add("чистовой отделкой (гипсокартон потолков)");
			fieldsSource.add("чистовой отделкой (шпатлевка стен)");
			fieldsSource.add("электрощитовая коробка");

			
	        if(construction!=null && construction.getFields()!=null){
	        	fieldsTarget.addAll(construction.getFields());
	        	fieldsSource.removeAll(fieldsTarget);
	        }	
	        fields = new DualListModel<String>(fieldsSource, fieldsTarget);
		}		
		return fields;
	}

	public void setFields(DualListModel<String> fields) {
		
		this.fields = fields;
	}

	public Boolean getBlock() {
		return block;
	}

	public void setBlock(Boolean block) {
		this.block = block;
	}
	
}
