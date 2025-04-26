package org.asb.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.asb.beans.FilterExample;
import org.asb.beans.InequalityConstants;
import org.asb.beans.SortEnum;
import org.asb.controller.admin.ClassifierController;
import org.asb.enums.AppartmentType;
import org.asb.enums.ClientFrom;
import org.asb.enums.ClientType;
import org.asb.model.Client;
import org.asb.model.Company;
import org.asb.model.Construction;
import org.asb.model.Denounce;
import org.asb.model.User;
import org.asb.service.CompanyService;
import org.asb.service.ConstructionService;
import org.asb.util.Translit;
import org.asb.util.web.Messages;
import org.primefaces.model.DefaultStreamedContent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;


@ViewScoped
@ManagedBean
public class ReportController extends BaseReportController{
	@Inject
	private JasperViewerController jasperViewerController; 
	private List<Company> companies;
	private List<Company> developers;
	private List<Construction> constructions;
	private List<Company> filterCompanies;
	private List<Company> filterDevelopers;
	private List<Construction> filterConstructions;
	private List<AppartmentType> filterTypes;
	private List<ClientType> filterClientTypes;
	private List<ClientFrom> filterClientFroms;
	private Date dateFrom=new Date();
	private Date dateTo=new Date();
	private String sortString="year";
	
	@EJB
	protected ConstructionService constructionService;
	@EJB
	protected CompanyService companyService;

	private boolean value1; 
	
	public void generateGlobalCount(){
		System.out.println("generateReport1");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		String ids="";
		for(Construction construction:filterConstructions){
			ids=ids+construction.getId()+",";
			
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один строительный объект!!!", "Необходимо выбрать как минимум один строительный объект!!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		
		map.put("construction_ids", ids);
		
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "report_1.jasper");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(jasperPrint);
				
		jasperViewerController.view(jasperPrint);
	}
	public void generateSoldCount(){
		System.out.println("generateReport2");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		String ids="";
		for(Construction construction:filterConstructions){
			ids=ids+construction.getId()+",";
			
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один строительный объект!!!", "Необходимо выбрать как минимум один строительный объект!!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		
		map.put("construction_ids", ids);
		
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "report_2.jasper");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(jasperPrint);
				
		jasperViewerController.view(jasperPrint);
	}
	public void generateTotalReport(){
		System.out.println("generateReport22");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		String ids="";
		for(Construction construction:filterConstructions){
			ids=ids+construction.getId()+",";
			
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один строительный объект!!!", "Необходимо выбрать как минимум один строительный объект!!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		
		map.put("construction_ids", ids);
		
		map.put("report_date", new Date());
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "report_6.jasper");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(jasperPrint);
				
		jasperViewerController.view(jasperPrint);
	}
	public void generatePeriodCount(){
		System.out.println("generateReport3");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		
		
		String ids="";
		for(AppartmentType ap:filterTypes){
			ids=ids+ap.ordinal()+",";
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один Тип объекта!!!", "Необходимо выбрать как минимум один Тип объекта !!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		map.put("ap_type_ids", ids);
		
		ids="";
		for(ClientType cp:filterClientTypes){
			ids=ids+cp.ordinal()+",";
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один Тип сделки!!!", "Необходимо выбрать как минимум один Тип сделки !!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		map.put("client_type_ids", ids);
		
		
		ids="";
		for(ClientFrom cp:filterClientFroms){
			ids=ids+cp.ordinal()+",";
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один Тип клиента!!!", "Необходимо выбрать как минимум один Тип клиента !!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		map.put("client_from_ids", ids);
		
		map.put("sort_string", sortString);
		
		ids="";
		for(Construction construction:filterConstructions){
			ids=ids+construction.getId()+",";
			
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один строительный объект!!!", "Необходимо выбрать как минимум один строительный объект!!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		
		map.put("construction_ids", ids);
		if(dateFrom==null||dateTo==null){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать период!!!", "Необходимо выбрать период!!!") );
			return;
		}
		map.put("date_from", dateFrom);
		map.put("date_to", dateTo);
		
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "report_3.jasper");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(jasperPrint);
				
		jasperViewerController.view(jasperPrint);
	}
	public void generateCuratorCount(){
		System.out.println("generateReport5");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		String ids="";
		for(Construction construction:filterConstructions){
			ids=ids+construction.getId()+",";
			
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один строительный объект!!!", "Необходимо выбрать как минимум один строительный объект!!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		
		map.put("construction_ids", ids);
		if(dateFrom==null||dateTo==null){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать период!!!", "Необходимо выбрать период!!!") );
			return;
		}
		map.put("dateFrom", dateFrom);
		map.put("dateTo", dateTo);
		
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "report_5.jasper");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(jasperPrint);
				
		jasperViewerController.view(jasperPrint);
	}
	
	public void generateAverageCount(){
		System.out.println("generateReport4");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		
		String ids="";
		for(AppartmentType ap:filterTypes){
			ids=ids+ap.ordinal()+",";
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один Тип объекта!!!", "Необходимо выбрать как минимум один Тип объекта !!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		map.put("ap_type_ids", ids);
		
		ids="";
		for(ClientType cp:filterClientTypes){
			ids=ids+cp.ordinal()+",";
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один Тип сделки!!!", "Необходимо выбрать как минимум один Тип сделки !!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		map.put("client_type_ids", ids);
		
		
		ids="";
		for(Construction construction:filterConstructions){
			ids=ids+construction.getId()+",";
		}
		if(ids.length()<1){ 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_ERROR,  "Необходимо выбрать как минимум один строительный объект!!!", "Необходимо выбрать как минимум один строительный объект!!!") );
			return;
		}
		ids=ids.substring(0, ids.length()-1);
		
		map.put("construction_ids", ids);
		
		
		
		
		
		
		
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "report_4.jasper");			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(jasperPrint);
				
		jasperViewerController.view(jasperPrint);
	}
	
	
	
	private InputStream getPDFStream(JasperPrint jasperPrint){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		jasperPrintList.add(jasperPrint);
		
		JRPdfExporter exporter = new JRPdfExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		configuration.setCreatingBatchModeBookmarks(true);
		exporter.setConfiguration(configuration);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		try {			
			InputStream stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
			return stream;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private InputStream getWordStream(JasperPrint jasperPrint){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		jasperPrintList.add(jasperPrint);
		
		JRDocxExporter exporter = new JRDocxExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		SimpleDocxExporterConfiguration configuration = new SimpleDocxExporterConfiguration();
		exporter.setConfiguration(configuration);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		try {			
			InputStream stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
			return stream;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	private InputStream getExcelStream(JasperPrint jasperPrint){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		jasperPrintList.add(jasperPrint);
		
		JRXlsExporter exporter = new JRXlsExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		SimpleXlsExporterConfiguration configuration = new SimpleXlsExporterConfiguration();
		exporter.setConfiguration(configuration);

		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		try {			
			InputStream stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
			return stream;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public boolean isValue1() {
        return value1;
    }
 
    public void setValue1(boolean value1) {
        this.value1 = value1;
    }
    public void addMessage() {
        String summary = value1 ? "Checked" : "Unchecked";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }
    public List<Construction> getConstructions() {
		if(constructions==null) {
			List<FilterExample> examples=new ArrayList<>();
			
			System.out.println("got herrr=="+filterDevelopers);
			
			
			if(filterCompanies!=null && filterCompanies.size()>0)
				examples.add(new FilterExample("company",filterCompanies,InequalityConstants.IN));
			if(filterDevelopers!=null && filterDevelopers.size()>0)
				examples.add(new FilterExample("developer",filterDevelopers,InequalityConstants.IN));
			examples.add(new FilterExample("id",null,InequalityConstants.IS_NOT_NULL_SINGLE));
			constructions=constructionService.findByExample(0, 10000, SortEnum.ASCENDING, examples, "title");
		}
		return constructions;
	}

	public void setConstructions(List<Construction> constructions) {
		this.constructions = constructions;
	}
	public void onCompanyChange() {
        constructions=null;
        getConstructions();
    }
	
	public List<Company> getCompanies() {
		if(companies==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("main",true,InequalityConstants.EQUAL));
			companies=companyService.findByExample(0, 1000, SortEnum.DESCENDING, examples, "id");
		}
		return companies;
	}
	public List<Company> getDevelopers() {
		if(developers==null) {
			List<FilterExample> examples=new ArrayList<>();
			examples.add(new FilterExample("developer",true,InequalityConstants.EQUAL));
			developers=companyService.findByExample(0, 1000, SortEnum.DESCENDING, examples, "id");
		}
		return developers;
	}
	public void setDevelopers(List<Company> developers) {
		this.filterDevelopers=developers;
	}
	
	public List<ClientType> getClientTypes() {
		List<ClientType> types2=Arrays.asList(ClientType.values());
		List<ClientType> types=new ArrayList<>();
		types.addAll(types2);				
		types.remove(9);
		Collections.sort(types , new Comparator<ClientType>() {
            @Override
			public int compare(ClientType o1, ClientType o2) {
            	return Messages.getEnumMessage(o1.toString()).compareTo(Messages.getEnumMessage(o2.toString()));
			}
        });
		return types;
	}
	
	public List<ClientFrom> getClientFroms() {
		List<ClientFrom> types2=Arrays.asList(ClientFrom.values());
		List<ClientFrom> types=new ArrayList<>();
		types.addAll(types2);				
		Collections.sort(types , new Comparator<ClientFrom>() {
            @Override
			public int compare(ClientFrom o1, ClientFrom o2) {
            	return Messages.getEnumMessage(o1.toString()).compareTo(Messages.getEnumMessage(o2.toString()));
			}
        });
		return types;
	}
	
	public List<AppartmentType> getTypes() {
		List<AppartmentType> types=Arrays.asList(AppartmentType.values());
		return types;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}	

	public List<Construction> getFilterConstructions() {
		return filterConstructions;
	}

	public void setFilterConstructions(List<Construction> filterConstructions) {
		this.filterConstructions = filterConstructions;
	}
	public List<Company> getFilterCompanies() {
		return filterCompanies;
	}

	public void setFilterCompanies(List<Company> filterCompanies) {
		this.filterCompanies = filterCompanies;
	}
	public Date getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Date getDateTo() {
		return dateTo;
	}
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}
	public List<AppartmentType> getFilterTypes() {
		return filterTypes;
	}
	public void setFilterTypes(List<AppartmentType> filterTypes) {
		this.filterTypes = filterTypes;
	}
	public List<ClientType> getFilterClientTypes() {
		return filterClientTypes;
	}
	public void setFilterClientTypes(List<ClientType> filterClientTypes) {
		this.filterClientTypes = filterClientTypes;
	}
	public List<ClientFrom> getFilterClientFroms() {
		return filterClientFroms;
	}
	public void setFilterClientFroms(List<ClientFrom> filterClientFroms) {
		this.filterClientFroms = filterClientFroms;
	}
	public String getSortString() {
		return sortString;
	}
	public void setSortString(String sortString) {
		this.sortString = sortString;
	}
	public List<Company> getFilterDevelopers() {
		return filterDevelopers;
	}
	public void setFilterDevelopers(List<Company> filterDevelopers) {
		this.filterDevelopers = filterDevelopers;
	}
	
}
