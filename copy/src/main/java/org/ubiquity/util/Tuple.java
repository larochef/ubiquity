/*
 * Copyright 2012 ubiquity-copy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ubiquity.util;

import com.google.common.base.Objects;

/**
 * Utility class, serving as keys for maps
 * @author François LAROCHE
 */
public class Tuple<T, U> {

	public final T tObject;
	public final U uObject;
	private final int hashcode;
	
	public Tuple(T tObject, U uObject) {
//		if(tObject == null || uObject == null) {
//			throw new IllegalStateException("Provided classes musn't be null !");
//		}
		this.tObject = tObject;
		this.uObject = uObject;

		// cache hashcode since class is immutable (note: only if T & U are)
		this.hashcode = Objects.hashCode(tObject, uObject);
	}

	@Override
	public int hashCode() {
		return hashcode;
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj) {return true;}
		if (!(obj instanceof Tuple<?,?>)) {return false;}
		Tuple<?,?> other = (Tuple<?,?>) obj;
		return this.tObject == other.tObject && this.uObject == other.uObject;
	}

}
