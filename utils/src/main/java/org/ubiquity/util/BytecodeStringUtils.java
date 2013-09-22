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
import java.util.regex.Pattern;

/**
 * Utility class used to manipulate Strings : create bytecode names, replacements, and so on.
 * <br />
 * These methods have been moved to here to make other classes clearer.
 * <br />
 * Date: 08/06/12
 * Time: 12:49
 *
 * @author françois LAROCHE
 */
public final class ByteCodeStringUtils {

    private static final Map<String, Class<?>> PRIMITIVE_TYPES;

    private static final Pattern NAME_CLEANER_PATTERN = Pattern.compile("[/;]");

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

    private ByteCodeStringUtils() {
        // Do not instantiate a utility class
    }

    static final String OBJECT_CLASS = "java/lang/Object";

    static String byteCodeName(Class<?> c) {
        return byteCodeName(c.getName());
    }

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
    public static Class<?> toJavaClas(String byteCodeName) {
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

    public static String signatureToDesc(String signature) {
        // TODO : code.me
        return signature;
    }
}
