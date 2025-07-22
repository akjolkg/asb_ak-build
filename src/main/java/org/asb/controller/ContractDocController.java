package org.asb.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.asb.model.Schedule;
import org.asb.model.SubSchedule;

import com.ibm.icu.text.SimpleDateFormat;

public class ContractDocController {

		
	public XWPFDocument replaceTextDocx(InputStream inputStream, Map<String,String> keyMap,Map<String,List<Schedule>> scheduleMap,Map<String,Set<String>> equipmentMap) throws IOException {
		try {
			
			XWPFDocument doc = new XWPFDocument(inputStream);
			for(String key:keyMap.keySet()) {
				
				doc = replaceText(doc, key, keyMap.get(key));
			}
			
			for(String key:scheduleMap.keySet()) {
				doc=addTable(doc, key, scheduleMap.get(key));
			}
			
			for(String key:equipmentMap.keySet()) {
				doc=addParagraph(doc, key, equipmentMap.get(key));
			}
			
			//OutputStream out=new FileOutputStream(outFile);
	        //PdfOptions options=null;
	        //PdfConverter.getInstance().convert(doc,out,options);
			
			
			return doc;
			//doc.close();

//	        System.out.println("Done");
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public XWPFDocument replaceTextDocxS(InputStream inputStream, Map<String, String> keyMap,
			Map<String, List<SubSchedule>> scheduleMap, Map<String, Set<String>> equipmentMap) throws IOException {
		try {

			XWPFDocument doc = new XWPFDocument(inputStream);
			for (String key : keyMap.keySet()) {

				doc = replaceText(doc, key, keyMap.get(key));
			}

			for (String key : scheduleMap.keySet()) {
				doc = addTableSubSchedule(doc, key, scheduleMap.get(key));
			}

			for (String key : equipmentMap.keySet()) {
				doc = addParagraph(doc, key, equipmentMap.get(key));
			}

			// OutputStream out=new FileOutputStream(outFile);
			// PdfOptions options=null;
			// PdfConverter.getInstance().convert(doc,out,options);

			return doc;
			// doc.close();

//	        System.out.println("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public XSSFWorkbook replaceTextXlsx(InputStream inputStream, Map<String, String> keyMap) throws IOException {
		try {

			// Load the workbook from the input stream
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
			// Iterate through all sheets in the workbook
			for (Sheet sheet : workbook) {
				// Iterate through all rows in the sheet
				for (Row row : sheet) {
					// Iterate through all cells in the row
					for (Cell cell : row) {
						// Check if the cell contains text
						try {
							if (cell.getCellTypeEnum().equals(CellType.STRING)) {
								String cellValue = cell.getStringCellValue();
								// Replace text based on the keyMap
								for (Map.Entry<String, String> entry : keyMap.entrySet()) {
									if (cellValue.contains(entry.getKey())) {
										cellValue = cellValue.replace(entry.getKey(), entry.getValue());
									}
								}
								if (cellValue.matches("^-?\\d+(\\.\\d+)?$")) {
									cell.setCellValue(Double.parseDouble(cellValue));
								} else {
									// Update the cell value as a string
									cell.setCellValue(cellValue);
								}
							} else if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
								// Recalculate formulas
								formulaEvaluator.evaluateFormulaCell(cell);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}

			// Return the modified workbook
			return workbook;
//	        System.out.println("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	private HWPFDocument replaceText(HWPFDocument doc, String originalText, String updatedText) {
	    Range range = doc.getRange();
	    range.replaceText(originalText, updatedText);
	    return doc;
	}
	
	private XWPFDocument addTable(XWPFDocument doc, String originalText, List<Schedule> schedules) throws IOException {
		
		
				XWPFTable stable=null;
				
				for(XWPFTable tbl:doc.getTables()) {
					for (XWPFTableRow row : tbl.getRows()) {
			            for (XWPFTableCell cell : row.getTableCells()) {
			            	if(cell.getText().contains(originalText)) {
			            		System.out.println("original run======"+originalText+"===="+cell.getText());
			            		XWPFParagraph paragraph=cell.getParagraphs().get(0);
			            		replaceTextInParagraph(paragraph, originalText, "");
			            		System.out.println("original run======"+originalText+"===="+cell.getText());
			            		stable=tbl;
			            		break;
			            	}
			            }
			        }
				}
				if(stable!=null) {
					Integer i=0;
					String pattern = "dd-MM-yyyy";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    			      //create second row
					BigDecimal total=BigDecimal.ZERO;
					for(Schedule s:schedules) {
    			      XWPFTableRow tableRowTwo = stable.createRow();
    			      tableRowTwo.setHeight(25);
    			      tableRowTwo.getCell(0).removeParagraph(0);
    			      tableRowTwo.getCell(0).addParagraph();
    			      XWPFParagraph paragraph=tableRowTwo.getCell(0).getParagraphs().get(0);    			      
    			      paragraph.setAlignment(ParagraphAlignment.CENTER);
    			      paragraph.setSpacingAfter(0);
    			      XWPFRun newRun = paragraph.insertNewRun(0);
    			      newRun.setText(i==0?"Первоначальный взнос":i+"");
    			      newRun.setFontFamily("Times New Roman");
    			      newRun.setFontSize(12);
    			      tableRowTwo.getCell(1).removeParagraph(0);
    			      tableRowTwo.getCell(1).addParagraph();
    			      XWPFParagraph paragraph1=tableRowTwo.getCell(1).getParagraphs().get(0);
    			      paragraph1.setAlignment(ParagraphAlignment.CENTER);
    			      paragraph1.setSpacingAfter(0);
    			      XWPFRun newRun1 = paragraph1.insertNewRun(0);
    			      newRun1.setText(simpleDateFormat.format(s.getDatePayment()));	
    			      newRun1.setFontFamily("Times New Roman");
    			      newRun1.setFontSize(12);
    			      tableRowTwo.getCell(2).removeParagraph(0);
    			      tableRowTwo.getCell(2).addParagraph();
    			      XWPFParagraph paragraph2=tableRowTwo.getCell(2).getParagraphs().get(0);
    			      paragraph2.setAlignment(ParagraphAlignment.CENTER);
    			      paragraph2.setSpacingAfter(0);
    			      XWPFRun newRun2 = paragraph2.insertNewRun(0);
    			      newRun2.setText(s.getAmountToPay()+"");	
    			      newRun2.setFontFamily("Times New Roman");
    			      newRun2.setFontSize(12);
    			      i++;
    			      total=total.add(s.getAmountToPay());
					}
					XWPFTableRow tableRowTwo = stable.createRow();
	  			      tableRowTwo.setHeight(25);
	  			      tableRowTwo.getCell(0).removeParagraph(0);
	  			      tableRowTwo.getCell(0).addParagraph();
	  			      XWPFParagraph paragraph=tableRowTwo.getCell(0).getParagraphs().get(0);    			      
	  			      paragraph.setAlignment(ParagraphAlignment.CENTER);
	  			      XWPFRun newRun = paragraph.insertNewRun(0);
	  			      newRun.setText("");
	  			      tableRowTwo.getCell(1).removeParagraph(0);
	  			      tableRowTwo.getCell(1).addParagraph();
	  			      XWPFParagraph paragraph1=tableRowTwo.getCell(1).getParagraphs().get(0);
	  			      paragraph1.setAlignment(ParagraphAlignment.CENTER);
	  			      XWPFRun newRun1 = paragraph1.insertNewRun(0);
	  			      newRun1.setText("Итого:");	
	  			      newRun1.setFontFamily("Times New Roman");
	  			      newRun1.setBold(true);
	  			      newRun1.setFontSize(12);
	  			      tableRowTwo.getCell(2).removeParagraph(0);
	  			      tableRowTwo.getCell(2).addParagraph();
	  			      XWPFParagraph paragraph2=tableRowTwo.getCell(2).getParagraphs().get(0);
	  			      paragraph2.setAlignment(ParagraphAlignment.CENTER);
	  			      XWPFRun newRun2 = paragraph2.insertNewRun(0);
	  			      newRun2.setText(total+"");	
	  			      newRun2.setBold(true);
	  			      newRun2.setFontFamily("Times New Roman");
	  			      newRun2.setFontSize(12);
    			      
				}
		
		
	    return doc;
	}
	private XWPFDocument addTableSubSchedule(XWPFDocument doc, String originalText, List<SubSchedule> schedules) throws IOException {

		XWPFTable stable = null;

		for (XWPFTable tbl : doc.getTables()) {
			for (XWPFTableRow row : tbl.getRows()) {
				for (XWPFTableCell cell : row.getTableCells()) {
					if (cell.getText().contains(originalText)) {
						System.out.println("original run======" + originalText + "====" + cell.getText());
						XWPFParagraph paragraph = cell.getParagraphs().get(0);
						replaceTextInParagraph(paragraph, originalText, "");
						System.out.println("original run======" + originalText + "====" + cell.getText());
						stable = tbl;
						break;
					}
				}
			}
		}
		if (stable != null) {
			Integer i = 0;
			String pattern = "dd-MM-yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			// create second row
			BigDecimal total = BigDecimal.ZERO;
			for (SubSchedule s : schedules) {
				XWPFTableRow tableRowTwo = stable.createRow();
				tableRowTwo.setHeight(25);
				tableRowTwo.getCell(0).removeParagraph(0);
				tableRowTwo.getCell(0).addParagraph();
				XWPFParagraph paragraph = tableRowTwo.getCell(0).getParagraphs().get(0);
				paragraph.setAlignment(ParagraphAlignment.CENTER);
				paragraph.setSpacingAfter(0);
				XWPFRun newRun = paragraph.insertNewRun(0);
				newRun.setText(i == 0 ? "Первоначальный взнос" : i + "");
				newRun.setFontFamily("Times New Roman");
				newRun.setFontSize(12);
				tableRowTwo.getCell(1).removeParagraph(0);
				tableRowTwo.getCell(1).addParagraph();
				XWPFParagraph paragraph1 = tableRowTwo.getCell(1).getParagraphs().get(0);
				paragraph1.setAlignment(ParagraphAlignment.CENTER);
				paragraph1.setSpacingAfter(0);
				XWPFRun newRun1 = paragraph1.insertNewRun(0);
				newRun1.setText(simpleDateFormat.format(s.getDatePayment()));
				newRun1.setFontFamily("Times New Roman");
				newRun1.setFontSize(12);
				tableRowTwo.getCell(2).removeParagraph(0);
				tableRowTwo.getCell(2).addParagraph();
				XWPFParagraph paragraph2 = tableRowTwo.getCell(2).getParagraphs().get(0);
				paragraph2.setAlignment(ParagraphAlignment.CENTER);
				paragraph2.setSpacingAfter(0);
				XWPFRun newRun2 = paragraph2.insertNewRun(0);
				newRun2.setText(s.getAmountToPay() + "");
				newRun2.setFontFamily("Times New Roman");
				newRun2.setFontSize(12);
				i++;
				total = total.add(s.getAmountToPay());
			}
			XWPFTableRow tableRowTwo = stable.createRow();
			tableRowTwo.setHeight(25);
			tableRowTwo.getCell(0).removeParagraph(0);
			tableRowTwo.getCell(0).addParagraph();
			XWPFParagraph paragraph = tableRowTwo.getCell(0).getParagraphs().get(0);
			paragraph.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun newRun = paragraph.insertNewRun(0);
			newRun.setText("");
			tableRowTwo.getCell(1).removeParagraph(0);
			tableRowTwo.getCell(1).addParagraph();
			XWPFParagraph paragraph1 = tableRowTwo.getCell(1).getParagraphs().get(0);
			paragraph1.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun newRun1 = paragraph1.insertNewRun(0);
			newRun1.setText("Итого:");
			newRun1.setFontFamily("Times New Roman");
			newRun1.setBold(true);
			newRun1.setFontSize(12);
			tableRowTwo.getCell(2).removeParagraph(0);
			tableRowTwo.getCell(2).addParagraph();
			XWPFParagraph paragraph2 = tableRowTwo.getCell(2).getParagraphs().get(0);
			paragraph2.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun newRun2 = paragraph2.insertNewRun(0);
			newRun2.setText(total + "");
			newRun2.setBold(true);
			newRun2.setFontFamily("Times New Roman");
			newRun2.setFontSize(12);

		}

		return doc;
	}
	private XWPFDocument addParagraph(XWPFDocument doc, String originalText, Set<String> equipments) throws IOException {
		System.out.println("adding paragraph=="+originalText);
		XWPFParagraph p=null;
		for(XWPFParagraph paragraph:doc.getParagraphs()) { 			
			List<XWPFRun> runs = paragraph.getRuns();
			for (XWPFRun run : runs) {
		    	String text = run.getText(0);
		        if (text != null && text.contains(originalText)) {
		        	System.out.println("===============foundddddddddd========================"+originalText+"-----");
		        	run.setText("", 0);
		        	p=paragraph;
		        	break;
		        }
		    } 
		}
		
		if(p!=null) {
			int k=0;
        	for(String s:equipments) {	        	
	        	XWPFRun r=p.createRun();
	        	r.setText(k+1+ ". "+s, 0);
	        	r.setFontFamily("Times New Roman");
  			    r.setFontSize(12);
  			    r.addBreak();
	        	
	        	k++;
	        }
			
		}
	return doc;
	}
	
	
	private void saveFile(String filePath, XWPFDocument doc) throws IOException {
	    try (FileOutputStream out = new FileOutputStream(filePath)) {
	        doc.write(out);
	    }
	}
	
	private void saveFile(String filePath, HWPFDocument doc) throws IOException {
	    try (FileOutputStream out = new FileOutputStream(filePath)) {
	        doc.write(out);
	    }
	}
	private XWPFDocument replaceText(XWPFDocument doc, String originalText, String updatedText) {
	    replaceTextInParagraphs(doc.getParagraphs(), originalText, updatedText);
	    for(XWPFFooter header:doc.getFooterList())
	    	replaceTextInParagraphs(header.getParagraphs(), originalText, updatedText);
	    for (XWPFTable tbl : doc.getTables()) {
	        for (XWPFTableRow row : tbl.getRows()) {
	            for (XWPFTableCell cell : row.getTableCells()) {
	                replaceTextInParagraphs(cell.getParagraphs(), originalText, updatedText);
	                
	            }
	        }
	    }
	    return doc;
	}

	private void replaceTextInParagraphs(List<XWPFParagraph> paragraphs, final String originalText, String updatedText) {
	    paragraphs.forEach(paragraph -> replaceTextInParagraph(paragraph, originalText, updatedText));
	    
	    
	}
	private void replaceTextInParagraph(XWPFParagraph paragraph, String originalText, String updatedText) {
		List<XWPFRun> runs = paragraph.getRuns();
		for (XWPFRun run : runs) {
	    	String text = run.getText(0);
	        if (text != null && text.contains(originalText)) {
	        	System.out.println("===============foundddddddddd========================"+originalText+"-----"+updatedText);
	        	if(updatedText==null)
	        		updatedText="";
	            String updatedRunText = text.replace(originalText, updatedText);
	            run.setText(updatedRunText, 0);
	        }
	    } 
	}
	private void removeAllRuns(XWPFParagraph paragraph) {
	    int size = paragraph.getRuns().size();
	    for (int i = 0; i < size; i++) {
	        paragraph.removeRun(0);
	    }
	}
	private void insertReplacementRuns(XWPFParagraph paragraph, String replacedText) {
	    String[] replacementTextSplitOnCarriageReturn = org.apache.commons.lang.StringUtils.split(replacedText, "\n");

	    for (int j = 0; j < replacementTextSplitOnCarriageReturn.length; j++) {
	        String part = replacementTextSplitOnCarriageReturn[j];

	        XWPFRun newRun = paragraph.insertNewRun(j);
	        newRun.setText(part);

	        if (j+1 < replacementTextSplitOnCarriageReturn.length) {
	            newRun.addCarriageReturn();
	        }
	    }       
	}

}
