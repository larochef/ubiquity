package org.ubiquity;

import java.util.List;

/**
 * @author François LAROCHE
 *
 * @param <T>
 * @param <U>
 */
public interface Copier<T, U> {

	U map(T element);
	
	List<U> map(List<T> elements);
}
