package org.ubiquity.bytecode;

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
public final class BytecodeStringUtils {

    private static final Pattern NAME_CLEANER_PATTERN = Pattern.compile("[/;]");

    private BytecodeStringUtils() {}

    static String byteCodeName(Class<?> c) {
        return byteCodeName(c.getName());
    }

    static String byteCodeName(String c) {
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

    static String getDescription(String className){
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

    static String createCopierClassName(String srcBytecodeName, String targetBytecodeName) {
        int index = srcBytecodeName.startsWith("L") ? 1 : 0;
        String rightPart = srcBytecodeName.substring(index);
        index = targetBytecodeName.startsWith("L") ? 1 : 0;
        rightPart += targetBytecodeName.substring(index);
        return "org/ubiquity/bytecode/generated/Copier" + NAME_CLEANER_PATTERN.matcher(rightPart).replaceAll("") +
                System.nanoTime();
    }

    static String replaceAll(String from, String pattern, String replacement) {
        if(from == null || pattern == null || replacement == null) {
            return from;
        }
        int index = from.indexOf(pattern);
        if(index == -1) {
            return from;
        }
        return from.substring(0, index) + replacement + replaceAll(from.substring(index + pattern.length()), pattern, replacement);
    }
}
