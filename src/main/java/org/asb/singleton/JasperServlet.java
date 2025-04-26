package org.asb.singleton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.asb.controller.JasperViewerController;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleDocxExporterConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxExporterConfiguration;

/**
 * 
 * @author Kuttubek Aidaraliev
 *
 */

@WebServlet(urlPatterns = {"/jasper"})
public class JasperServlet extends HttpServlet {

	private static final long serialVersionUID = -7626497440520150364L;
	private static final String FORMAT_PDF = "pdf";
	private static final String FORMAT_XLS = "xls";
	private static final String FORMAT_XLSX = "xlsx";
	private static final String FORMAT_DOC = "doc";
	private static final String FORMAT_DOCX = "docx";
	
	public JasperServlet() {}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String type = req.getParameter("type");
			String k = req.getParameter("k");
			
			if(type == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			if(k == null || !k.equals("inline")) k = "attachment";
			
		    JasperPrint jasperPrint = (JasperPrint)req.getSession().getAttribute(JasperViewerController.REPORT_NAME);
		    
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			String format = getFormat(type);
			InputStream stream = getStream(jasperPrint, format);
			String contentType = mimeTypesMap.getContentType("test." + format);
			DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd_hh-mm");
			
			System.out.println("contentType = " + contentType);
			
			resp.setContentType("application/pdf");
		    resp.setHeader("Content-Disposition", k + "; filename=\"report_" + dateFormat.format(new Date()) + "." + format + "\";");
		    resp.setContentLength(stream.available());
		    OutputStream os = resp.getOutputStream();
		    
		    try {
		        IOUtils.copy(stream, os);
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        os.close();
		        stream.close();
		    }
		} catch(Exception e){
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	private InputStream getStream(JasperPrint jasperPrint, String format){
		switch (format) {
		case FORMAT_XLS:
			return getXLSStream(jasperPrint);
		case FORMAT_XLSX:
			return getXLSXStream(jasperPrint);
		case FORMAT_DOC:
		case FORMAT_DOCX:
			return getDOCXStream(jasperPrint);
		default:
			return getPDFStream(jasperPrint);
		}
	}
	
	private InputStream getDOCXStream(JasperPrint jasperPrint){
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
	
	private InputStream getXLSXStream(JasperPrint jasperPrint){
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		List<JasperPrint> jasperPrintList = new ArrayList<>();
		jasperPrintList.add(jasperPrint);
		
		JRXlsxExporter exporter = new JRXlsxExporter();
		
		exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
		SimpleXlsxExporterConfiguration configuration = new SimpleXlsxExporterConfiguration();
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
	
	private InputStream getXLSStream(JasperPrint jasperPrint){
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
	
	private String getFormat(String format) {
		switch (format) {
		case FORMAT_XLS:
			return FORMAT_XLS;
		case FORMAT_XLSX:
			return FORMAT_XLSX;
		case FORMAT_DOC:
			return FORMAT_DOC;
		case FORMAT_DOCX:
			return FORMAT_DOCX;
		default:
			return FORMAT_PDF;
		}
	}

}

