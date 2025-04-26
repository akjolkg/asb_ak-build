package org.asb.beans;

import java.io.Serializable;

/****
 * 
 * @author Akzholbek Omorov
 *
 */

public class FilterExample implements Serializable {

	private static final long serialVersionUID = 1288320065497774131L;
	private String property;
	private Object value;
	private boolean length=false;
	private boolean search=false;
	private InequalityConstants constants;
	private boolean insensitive = false;
	
	public FilterExample() {}
	
	public FilterExample(String property, Object value, InequalityConstants constants) {
		this.property = property;
		this.value = value;
		this.constants = constants;
	}
	public FilterExample(String property, InequalityConstants constants) {
		this.property = property;
		this.value = null;
		this.constants = constants;
	}
	
	public FilterExample(String property, Object value, InequalityConstants constants, boolean insensitive) {
		this.property = property;
		this.value = value;
		this.constants = constants;
		this.insensitive = insensitive;
	}
	public FilterExample(String property, Object value, InequalityConstants constants, boolean insensitive,boolean length) {
		this.property = property;
		this.value = value;
		this.constants = constants;
		this.insensitive = insensitive;
		this.length=length;
	}
	
	public FilterExample(boolean search,String property, Object value, InequalityConstants constants, boolean insensitive,boolean length) {
		this.search=search;
		this.property = property;
		this.value = value;
		this.constants = constants;
		this.insensitive = insensitive;
		this.length=length;
	}
	
	
	public FilterExample(boolean search,String property, Object value, InequalityConstants constants, boolean insensitive) {
		this.search=search;
		this.property = property;
		this.value = value;
		this.constants = constants;
		this.insensitive = insensitive;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public InequalityConstants getConstants() {
		return constants;
	}

	public void setConstants(InequalityConstants constants) {
		this.constants = constants;
	}
	
	public boolean isInsensitive() {
		return insensitive;
	}
	
	public void setInsensitive(boolean insensitive) {
		this.insensitive = insensitive;
	}

	@Override
	public String toString() {
		return "FilterExample [property=" + property + ", value=" + value
				+ ", constants=" + constants + ", insensitive=" + insensitive
				+ "]";
	}
	
	public String query(String entity, int index) {
		if(length){
			return " length(" + entity + "." + getProperty() + ") " + getOperation(getConstants()) + " :value" + index; 
			
			
			
		}
		
		if (value!=null){
			if(getConstants()!=InequalityConstants.MEMBER_OF)
				{return insensitive ? " UPPER(" + entity + "." + getProperty() + ") " + getOperation(getConstants()) + " UPPER(:value" + index + ")"
						: " " + entity + "." + getProperty() + " " + getOperation(getConstants()) + " :value" + index;}
			else
				{return insensitive ? " UPPER(" + entity + "." + getProperty() + ") " + getOperation(getConstants()) + " UPPER(:value" + index + ")"
					: " :value" + index+ " " + getOperation(getConstants()) + " " + entity + "." + getProperty() ;}
		}
		else
			return insensitive ? " UPPER(" + entity + "." + getProperty() + ") " + getOperation(getConstants())
					: " " + entity + "." + getProperty() + " " + getOperation(getConstants()) ;
			
	}
	
	protected String getOperation(final InequalityConstants constants) throws UnsupportedOperationException {
		switch (constants) {
			case GREATER:
				return ">";
			case LESSER:
				return "<";
			case EQUAL:
				return "=";
			case NOT_EQUAL:
				return "<>";
			case NOT:
				return "NOT";
			case LIKE:
				return "LIKE";
			case IN:
				return "IN";
			case NOT_IN:
				return "NOT IN";
			case IS_NULL:
				return "IS EMPTY";
			case IS_NOT_NULL:
				return "IS NOT EMPTY";
			case IS_NULL_SINGLE:
				return "IS NULL";
			case IS_NOT_NULL_SINGLE:
				return "IS NOT NULL";
			case MEMBER_OF:
				return "MEMBER OF";
			case GREATER_OR_EQUAL:
				return ">=";
			case LESSER_OR_EQUAL:
				return "<=";
			case NOT_LIKE:
				return "NOT LIKE";
			default:
				throw new UnsupportedOperationException();
		}
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}
}
