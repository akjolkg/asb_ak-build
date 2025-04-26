package org.asb.conversion;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/***
 * 
 * @author Akzholbek Omorov
 *
 */

@FacesConverter(value="companyConverter")
public class CompanyConverter implements Converter {
	
	private static final Map<Object, String> caches = new HashMap<Object, String>();
	private final ReadWriteLock lock = new ReentrantReadWriteLock();

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			lock.readLock().lock();
			for (Entry<Object, String> entry : caches.entrySet()) {
	            if (entry.getValue().equals(value)) {
	            	return entry.getKey();
	            }
	        }
	        return null;
		} finally {
			lock.readLock().lock();
		}
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) {
		
		boolean contain = false;
		try {
			lock.readLock().lock();
			contain = caches.containsKey(object);
		} finally {
			lock.readLock().unlock();
		}
		
		if(!contain){
            String uuid = UUID.randomUUID().toString();
            try {
            	lock.writeLock().lock();
                caches.put(object, uuid);
            } finally {
            	lock.writeLock().unlock();
            }
            return uuid;
        } else {
        	try {
        		lock.readLock().lock();
        		return caches.get(object);
        	} finally {
        		lock.readLock().unlock();
        	}
        }
	}
}