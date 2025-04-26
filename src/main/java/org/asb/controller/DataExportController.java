package org.asb.controller;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.component.export.PDFOptions;
 
@ManagedBean
public class DataExportController implements Serializable {
    
	private ExcelOptions excelOpt;
     
    private PDFOptions pdfOpt;
     
    @PostConstruct
    public void init() {
        customizationOptions();
    }
     
    public void customizationOptions() {
        excelOpt = new ExcelOptions();
        excelOpt.setFacetBgColor("#F88017");
        excelOpt.setFacetFontSize("10");
        excelOpt.setFacetFontColor("#0000ff");
        excelOpt.setFacetFontStyle("BOLD");
        excelOpt.setCellFontColor("#00ff00");
        excelOpt.setCellFontSize("8");
         
        pdfOpt = new PDFOptions();
        pdfOpt.setFacetBgColor("#F88017");
        pdfOpt.setFacetFontColor("#0000ff");
        pdfOpt.setFacetFontStyle("BOLD");
        pdfOpt.setCellFontSize("12");
    }
 
    public ExcelOptions getExcelOpt() {
        return excelOpt;
    }
 
    public void setExcelOpt(ExcelOptions excelOpt) {
        this.excelOpt = excelOpt;
    }
 
    public PDFOptions getPdfOpt() {
        return pdfOpt;
    }
 
    public void setPdfOpt(PDFOptions pdfOpt) {
        this.pdfOpt = pdfOpt;
    }
 
    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        sheet.createFreezePane(0, 1);
        HSSFRow header = sheet.getRow(0);
        HSSFFont my_font=wb.createFont();
        my_font.setBold(true);
        HSSFCellStyle cellStyle = wb.createCellStyle();  
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(my_font);
        
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            HSSFCell cell = header.getCell(i);
             
            cell.setCellStyle(cellStyle);
        }
        
        int colCount = header.getPhysicalNumberOfCells();
        int rowCount = sheet.getPhysicalNumberOfRows();
        HSSFCellStyle intStyle = wb.createCellStyle();
        intStyle.setDataFormat((short)1);

        HSSFCellStyle decStyle = wb.createCellStyle();
        decStyle.setDataFormat((short)2);

        HSSFCellStyle dollarStyle = wb.createCellStyle();
        dollarStyle.setDataFormat((short)5);


        for(int rowInd = 1; rowInd < rowCount; rowInd++) {
            HSSFRow row = sheet.getRow(rowInd);
            for(int cellInd = 0; cellInd < colCount; cellInd++) {
                HSSFCell cell = row.getCell(cellInd);
                String strVal = cell.getStringCellValue();
                strVal=strVal.replaceAll(",", ".");
                strVal=strVal.replaceAll(" ", "");
                if(NumberUtils.isNumber(strVal)){
	                cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
	                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
	                double dblVal = Double.valueOf(strVal);
                	cell.setCellValue(dblVal);
                }
            }
        }
        
        
    }
     
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
        pdf.setPageSize(PageSize.A4);
 
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String logo = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" + File.separator + "images" + File.separator + "prime_logo.png";
         
        pdf.add(Image.getInstance(logo));
    }
}