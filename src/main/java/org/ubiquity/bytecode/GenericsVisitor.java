package org.ubiquity.bytecode;

import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;

import static org.ubiquity.util.Constants.ASM_LEVEL;

/**
 * Visitor used to make generics transparent, as if the class wasn't "generified".
 *
 * Date: 04/06/12
 *
 * @author François LAROCHE
 */
class GenericsVisitor extends ClassVisitor {

    private final Map<String, String> generics;

    public GenericsVisitor(ClassVisitor classVisitor, Map<String, String> generics) {
        super(ASM_LEVEL, classVisitor);
        this.generics = generics == null ? ImmutableMap.<String, String>of() : ImmutableMap.copyOf(generics);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        // if generics are matched, replace the generic by the concrete type defined.
        String newSignature = signature;
        if(signature != null) {
            for (Map.Entry<String, String> entry : generics.entrySet()) {
                newSignature = replaceAll(newSignature, "T" + entry.getKey() + ";", entry.getValue());
            }
        }

        return super.visitMethod(access, name, desc, newSignature, exceptions);
    }

    private static String replaceAll(String from, String pattern, String replacement) {
        if(from == null || pattern == null || replacement == null) {
            return from;
        }
        int index = from.indexOf(pattern);
        if(index == -1) {
            return from;
        }
        return from.substring(0, index) + replacement + replaceAll(from.substring(index + pattern.length()), pattern, replacement);
    }

    private String createDescFromSignature(String signature) {
        int start = signature.indexOf("<");
        if(start == -1) {
            return signature;
        }
        int end = signature.indexOf(">");
        String startStr = signature.substring(0, start);
        if(end == signature.length() - 1) {
            return startStr;
        }
        return startStr + createDescFromSignature(signature.substring(end + 1));
    }

}
