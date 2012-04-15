/**
 * 
 */
package org.ubiquity.util;

/**
 * Utility class, serving as keys for maps
 * @author François LAROCHE
 */
public final class ClassTuple<T, U> {

	private final Class<T> tClass;
	private final Class<U> uClass;
	private final int hashcode;
	
	public ClassTuple(Class<T> tClass, Class<U> uClass) {
		if(tClass == null || uClass == null) {
			throw new IllegalStateException("Provided classes musn't be null !");
		}
		this.tClass = tClass;
		this.uClass = uClass;

		// cache hashcode since class is immutable
		this.hashcode =  31 * (31 * + tClass.hashCode()) + uClass.hashCode();
	}

	@Override
	public int hashCode() {
		return hashcode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {return true;}
		if (obj == null) {return false;}
		if (!(obj instanceof ClassTuple)) {return false;}
		ClassTuple<?,?> other = (ClassTuple<?,?>) obj;
		return this.tClass == other.tClass && this.uClass == other.uClass;
	}
	
	
	
}
