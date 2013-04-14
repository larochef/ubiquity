package org.ubiquity.bytecode;

import java.util.Map;

/**
 * Date: 13/04/13
 *
 * @author François LAROCHE
 */
public class BytecodeStringUtils {

    public static String createCopierClassName(CopierKey<?,?> key) {
        Class<?> src = key.getSourceClass();
        Class<?> destination = key.getDestinationClass();
        Map<String, String> srcGenerics = key.getDestinationAnnotations();
        Map<String, String> destinationGenerics = key.getDestinationAnnotations();
        String srcName = byteCodeName(src);
        String destinationName = byteCodeName(destination);
        String srcSafeName = srcName;
        String destinationSafeName = destinationName;
        if(OBJECT_CLASS.equals(srcSafeName)) {
            srcSafeName = byteCodeName(CopierGenerator.getDefaultGenerics(srcGenerics));
        }
        if(OBJECT_CLASS.equals(destinationSafeName)) {
            destinationSafeName = byteCodeName(CopierGenerator.getDefaultGenerics(destinationGenerics));
        }


        int index = srcSafeName.startsWith("L") ? 1 : 0;
        String rightPart = srcSafeName.substring(index);
        index = destinationSafeName.startsWith("L") ? 1 : 0;
        rightPart += destinationSafeName.substring(index);
        return "org/ubiquity/bytecode/generated/Copier" + NAME_CLEANER_PATTERN.matcher(rightPart).replaceAll("") +
                System.nanoTime();
    }

}
