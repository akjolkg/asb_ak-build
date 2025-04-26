package org.asb.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/***
 * 
 * @author Akzholbek Omorov
 *
 */

public abstract class BaseReportController {
	
	@Resource(mappedName="java:jboss/datasources/asbDS") 
	protected DataSource ds;

	protected JasperPrint generateJasperPrint(Map<String, Object> map, Collection<Object> collection, String path){
		JasperPrint jasperPrint = null;
		InputStream inputStream = getStreamFromReports(path);
		
		Thread cur = Thread.currentThread();
		ClassLoader save = cur.getContextClassLoader();
		cur.setContextClassLoader(JasperCompileManager.class.getClassLoader());
		
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(report, map, new JRBeanCollectionDataSource(collection)) ;
		} catch (Exception e) {
			if ((e instanceof JRException)) {
				JRException exception = (JRException)e;
				exception.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			cur.setContextClassLoader(save);
		}
		
		return jasperPrint;
	}
	
	protected JasperPrint generateJasperPrint(Map<String, Object> map, Connection connection, String path){
		JasperPrint jasperPrint = null;
		InputStream inputStream = getStreamFromReports(path);
		
		Thread cur = Thread.currentThread();
		ClassLoader save = cur.getContextClassLoader();
		cur.setContextClassLoader(JasperCompileManager.class.getClassLoader());
		
		try {
			JasperReport report = (JasperReport)JRLoader.loadObject(inputStream);
			jasperPrint = JasperFillManager.fillReport(report, map, connection) ;
		} catch (Exception e) {
			if ((e instanceof JRException)) {
				JRException exception = (JRException)e;
				exception.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			cur.setContextClassLoader(save);
			if(connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		
		return jasperPrint;
	}
	
	protected StreamedContent getStream(JasperPrint jasperPrint) {
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
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			InputStream stream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			byteArrayOutputStream.close();
			String name = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.ENGLISH).format(new Date()) + "report.pdf";
			return new DefaultStreamedContent(stream, mimeTypesMap.getContentType(name), name);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	private InputStream getStreamFromReports(String filename) {
		System.out.println("load jasper from " + "/WEB-INF/reports/" + filename);
		return this.getClass().getClassLoader().getResourceAsStream("/META-INF/reports/" + filename);
	}
	
}