package org.ubiquity.bytecode;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Collections;
import java.util.HashMap;
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
        Map<String, String> gen = new HashMap<String, String>();
        if(generics != null) {
            gen.putAll(generics);
        }
        this.generics = Collections.unmodifiableMap(gen);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        // if generics are matched, replace the generic by the concre type defined.
        String newSignature = signature;
        if(signature != null) {
            for(String key : generics.keySet()) {
                newSignature = replaceAll(newSignature, "T" + key + ";", generics.get(key));
            }
            System.out.println("Changed signature from " + signature + " to " + newSignature);
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
}
