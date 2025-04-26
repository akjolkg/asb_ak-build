package org.asb.singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 * 
 * @author Akzholbek Omorov
 *
 */

public class Configuration {

	private static Configuration configration;
	private final Properties properties = new Properties();
	private Configuration() {
		init();
	}
	
	public static synchronized Configuration getInstance() {
		if(configration == null){
			configration = new Configuration();
		}
		return configration;
	}
	
	private void init(){
		try {
			InputStream stream = getClass().getClassLoader().getResourceAsStream("setting.properties");
			properties.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public Integer getIntProperty(String key) {
		try {
			return Integer.parseInt(properties.getProperty(key));
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}