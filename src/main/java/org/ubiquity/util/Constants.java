package org.ubiquity.util;

import org.objectweb.asm.Opcodes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Constants {

    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Z", "Ljava/lang/Boolean;");
        map.put("Ljava/lang/Boolean;", "Z");
        map.put("C", "Ljava/lang/Character;");
        map.put("Ljava/lang/Character;", "C");
        map.put("B", "Ljava/lang/Byte;");
        map.put("Ljava/lang/Byte;", "B");
        map.put("S", "Ljava/lang/Short;");
        map.put("Ljava/lang/Short;", "S");
        map.put("I", "Ljava/lang/Integer;");
        map.put("Ljava/lang/Integer;", "I");
        map.put("F", "Ljava/lang/Float;");
        map.put("Ljava/lang/Floats;", "F");
        map.put("J", "Ljava/lang/Long;");
        map.put("Ljava/lang/Long;", "J");
        map.put("D", "Ljava/lang/Double;");
        map.put("Ljava/lang/Double;", "D");
        map.put("Ljava/lang/String;", "Ljava/lang/String;");
        SIMPLE_PROPERTIES = Collections.unmodifiableMap(map);
    }

    public static final Map<String, String> SIMPLE_PROPERTIES;
	public static final int ASM_LEVEL = Opcodes.ASM4;
    public static final String IGNORE_ANNOTATION = "Lorg/ubiquity/annotation/CopyIgnore;";
    public static final String RENAME_ANNOTATION = "Lorg/ubiquity/annotation/CopyRename;";
    public static final String RENAMES_ANNOTATION = "Lorg/ubiquity/annotation/CopyRenames;";
}
