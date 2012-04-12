package org.ubiquity.bytecode;

/**
 * Simple java bean for testing utilities
 * 
 * @author François LAROCHE
 */
public class SimpleTestClass {

	private String property1;
	private String property2;
	private String property3;
	private String property4;
	
	public String getProperty1() {
		return property1;
	}
	public void setProperty1(String property1) {
		this.property1 = property1;
	}
	public String getProperty2() {
		return property2;
	}
	public void setProperty3(String property3) {
		this.property3 = property3;
	}
	@Override
	public String toString() {
		return "SimpleTestClass [property1=" + property1 + ", property2="
				+ property2 + ", property3=" + property3 + ", property4="
				+ property4 + "]";
	}

}
