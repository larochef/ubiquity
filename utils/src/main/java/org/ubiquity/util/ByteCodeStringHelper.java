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

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Utility class used to manipulate Strings : create byte code names, replacements, and so on.
 * <br />
 * These methods have been moved to here to make other classes clearer.
 * <br />
 * Date: 08/06/12
 * Time: 12:49
 *
 * @author françois LAROCHE
 */
public final class ByteCodeStringHelper {

    private static final Map<String, Class<?>> PRIMITIVE_TYPES;

    static {
        PRIMITIVE_TYPES = new ImmutableMap.Builder<String, Class<?>>()
                .put("B", Byte.class)
                .put("C", Character.class)
                .put("D", Double.class)
                .put("F", Float.class)
                .put("I", Integer.class)
                .put("L", Long.class)
                .put("S", Short.class)
                .put("Z", Boolean.class)
                .build();
    }

    private ByteCodeStringHelper() {
        // Do not instantiate a utility class
    }

    /**
     * Gets the type byte code name.
     * It is used when casting and so on.
     *
     * The byte code name will not contain any [ nor L....;
     *
     * @param c the String to ensure into a byte code type name
     * @return the name to be used in the byte code
     */
    public static String byteCodeName(String c) {
        String name = c.replace('.', '/');
        if(name.startsWith("[")) {
            name = name.substring(1);
        }
        if(name.startsWith("L")) {
            name = name.substring(1, name.length() - 1);
        }
        if(name.contains("<")) {
            name = name.substring(0, name.indexOf('<'));
        }
        return name;
    }

    /**
     * Transforms a String representing a class name into its representation usable in methods prototypes.
     *
     * String transformed by this method will typically contain L...; if needed
     *
     * @param className the representation of class name (it must have slashed instead of points)
     * @return the class name, ready to be used in a prototype
     */
    public static String getDescription(String className){
        if(className.indexOf('/') < 0) {
            return className;
        }
        if(className.startsWith("[")) {
            return "[" + getDescription(className.substring(1));
        }
        if(className.startsWith("L") && className.endsWith(";")) {
            return className;
        }
        return 'L' + className + ';';
    }

    /**
     * Replace all occurrences of a String by another one.
     * This method is null-safe
     *
     * @param from the source String, containing the text to handle
     * @param pattern the STring to replace
     * @param replacement the replacement
     * @return the replaced String
     */
    public static String replaceAll(String from, String pattern, String replacement) {
        if(from == null || pattern == null || replacement == null) {
            return from;
        }
        int index = from.indexOf(pattern);
        if(index == -1) {
            return from;
        }
        return from.substring(0, index) + replacement + replaceAll(from.substring(index + pattern.length()), pattern, replacement);
    }

    /**
     * Retrieves a class from the bytecode name.
     *
     * Primitive types will be transformed to the boxed type.
     * It works for classes as well as for arrays.
     *
     * @param byteCodeName the name describing the class.
     * @return the name usable
     */
    public static Class<?> toJavaClass(String byteCodeName) {
        if(PRIMITIVE_TYPES.containsKey(byteCodeName)) {
            return PRIMITIVE_TYPES.get(byteCodeName);
        }
        // Handle arrays
        String className;
        if(byteCodeName.charAt(0) == '[') {
            className = replaceAll(getDescription(byteCodeName), "/", ".");
        }
        else {
            className = replaceAll(byteCodeName(byteCodeName), "/", ".");
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to load class " + className, e);
        }
    }

    /**
     * Transforms a signature name (ie potentially containing generics) into a signature name (no generics)
     *
     * @param signature the signature to escape
     * @return the corresponding description
     */
    public static String signatureToDesc(String signature) {
        // TODO : code.me
        return signature;
    }
}
