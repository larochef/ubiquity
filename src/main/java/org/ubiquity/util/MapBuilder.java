package org.ubiquity.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 05/06/12
 *
 * @author François LAROCHE
 */
public class MapBuilder {
    private static final String SOURCE = "--src##";
    private static final String DEST = "$$src||";

    private final Map<String, Object> values = new HashMap<String, Object>();

    public MapBuilder generics(String name, String value) {
        this.values.put(name, value);
        return this;
    }

    public MapBuilder source(Class<?> source) {
        this.values.put(SOURCE, source);
        return this;
    }

    public MapBuilder destination(Class<?> destination) {
        this.values.put(DEST, destination);
        return this;
    }

    public Map<String, ?> build() {
        return Collections.unmodifiableMap(this.values);
    }


}
