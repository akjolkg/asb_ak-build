package org.asb.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.asb.model.Client;
import org.asb.model.Payment;
import org.asb.util.Translit;
import org.primefaces.model.DefaultStreamedContent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;


@RequestScoped
@ManagedBean
public class PaymentJasperController extends BaseReportController{
	@Inject
	private JasperViewerController jasperViewerController; 

	private boolean value1; 
	
	public void generatePaymentPrint(Payment payment){
		System.out.println("generateContract");
		Map<String, Object> map = new HashMap<>();
		map = new HashMap<>();
		map.put("payment_id", payment.getId());
		
		System.out.println("=== journalViewReport ===");
		System.out.println("payment_id"+payment);
		
		JasperPrint jasperPrint = null;
		try {
			jasperPrint = generateJasperPrint(map, ds.getConnection(), "pko.jasper");			
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

	
}
