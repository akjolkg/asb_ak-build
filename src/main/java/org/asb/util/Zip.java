package org.asb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * 
 * @author Omorov Akzholbek
 *
 */

public class Zip {
	
	private ZipOutputStream zos;
	private File file;
	private OutputStream bos;
	private Set<String> zipEntryNames;
	
	public Zip(String tikNumber){
		try {
			file = new File("/opt/cec/"+tikNumber+".zip");
			bos = new FileOutputStream(file);
			zos = new ZipOutputStream(bos);
			zipEntryNames = new HashSet<String>();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Zip(String tikNumber,Boolean temp){
		if (temp){
			try {
				file = new File("/opt/cec/temp/"+tikNumber+".zip");
				bos = new FileOutputStream(file);
				zos = new ZipOutputStream(bos);
				zipEntryNames = new HashSet<String>();
			} catch(Exception e){
				e.printStackTrace();
			}
		} else {
			try {
				file = new File("/opt/cec/"+tikNumber+".zip");
				bos = new FileOutputStream(file);
				zos = new ZipOutputStream(bos);
				zipEntryNames = new HashSet<String>();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void addToZip(byte[] bytes, String fileName){		
		
		ZipEntry zipEntry = new ZipEntry(getCorrectName(fileName));
	    try {
			zos.putNextEntry(zipEntry);	
	    	zos.write(bytes);
	    	zos.flush();
	        zos.closeEntry();   	
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getCorrectName(String fileName){
		fileName=Translit.translit(fileName);
		fileName = zipEntryNames.contains(fileName) ? getCorrectName(fileName, 1) : fileName;
		
		zipEntryNames.add(fileName);
		return fileName;
		
	}
	
	private String getCorrectName(String fileName, int count){
		int index = fileName.indexOf('.') == -1 ? fileName.length() : fileName.indexOf('.');
		String name = fileName.substring(0, index) + count + fileName.substring(index);
			
		if(zipEntryNames.contains(name)) fileName = getCorrectName(fileName, ++count);
		else fileName = name;
		
		return fileName;
	}
	
	public String identify() {
		try {
			System.out.println(file.getAbsolutePath() + " exists = " + file.exists());
			zos.close();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(file.getAbsolutePath() + ", exists = " + file.exists());
		return file.getAbsolutePath();		
	}
	
	public InputStream download() throws IOException {
		try {
			zos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new FileInputStream(file);		
	}
	
}
