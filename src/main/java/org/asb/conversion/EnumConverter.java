package org.asb.conversion;

import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/***
 * 
 * @author Akzholbek Omorov
 *
 */

@FacesConverter(value="enumConverter")
public class EnumConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {

		if (value == null || value.length() < 1 || value.equals("null"))
        {
            return null;
        }
				
        int pos = value.indexOf('@');
        if (pos < 0) throw new ConverterException(value + " do not point to an enum");

        String className = value.substring(0, pos);
        Class<?> clazz;
        int ordinal = Integer.parseInt(value.substring(pos + 1), 10);

        try
        {
            clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
            // if the clazz is not an enum it might be an enum which is inherited. In this case try to find the superclass.
            while (clazz != null && !clazz.isEnum())
            {
                clazz = clazz.getSuperclass();
            }
            
            if (clazz == null) throw new IllegalArgumentException("class " + className + " couldn't be treated as enum");

            Enum<?>[] enums = (Enum[]) clazz.getEnumConstants();
            if (enums.length >= ordinal)
            {
                return enums[ordinal];
            }
        }
        catch (ClassNotFoundException e1)
        {
            throw new RuntimeException(e1);
        }

        throw new IllegalArgumentException("ordinal " + ordinal + " not found in enum " + clazz);
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		
		if (value == null) return "";
		if(value=="null") return "";
		if(value.getClass()==String.class) return "";
        Enum<?> e = (Enum<?>) value;

        if (component instanceof UIInput || UIInput.COMPONENT_FAMILY.equals(component.getFamily()))
            return e.getClass().getName() + "@" + Integer.toString(e.ordinal(), 10);
        
        ResourceBundle messages = ResourceBundle.getBundle("org.infosystema.etender.messages.enums", 
        		facesContext.getViewRoot().getLocale());
        
        return messages.getString(e.name());
	}
	
}