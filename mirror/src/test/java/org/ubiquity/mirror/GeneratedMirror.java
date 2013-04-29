package org.ubiquity.mirror;

import com.google.common.collect.ImmutableMap;
import org.ubiquity.mirror.objects.ValueObject;

import java.util.Map;

/**
 * Date: 29/04/13
 *
 * @author François LAROCHE
 */
public class GeneratedMirror implements Mirror<ValueObject> {

    static enum Properties implements Property<ValueObject, Object> {
        PROPERTY1 {
            @Override
            public String get(ValueObject object) {
                return object.getProperty1();
            }

            @Override
            public void set(ValueObject object, Object value) {
                object.setProperty1((String) value);
            }

            @Override
            public boolean isReadable() {
                return true;
            }

            @Override
            public boolean isWritable() {
                return true;
            }

            @Override
            public Class getWrappedClass() {
                return String.class;
            }
        },
        PROPERTY2 {
            @Override
            public String get(ValueObject object) {
                return object.getProperty2();
            }

            @Override
            public void set(ValueObject object, Object value) {
                object.setProperty2((String) value);
            }

            @Override
            public boolean isReadable() {
                return true;
            }

            @Override
            public boolean isWritable() {
                return true;
            }

            @Override
            public Class getWrappedClass() {
                return String.class;
            }
        },
        PROPERTY3 {
            @Override
            public String get(ValueObject object) {
                return object.getProperty3();
            }

            @Override
            public void set(ValueObject object, Object value) {
                object.setProperty3((String) value);
            }

            @Override
            public boolean isReadable() {
                return true;
            }

            @Override
            public boolean isWritable() {
                return true;
            }

            @Override
            public Class getWrappedClass() {
                return String.class;
            }
        };

        static final Map<String, Properties> props = ImmutableMap.<String, Properties>builder()
                .put("property1", PROPERTY1)
                .put("property2", PROPERTY2)
                .put("property3", PROPERTY3)
                .build();
    }

    @Override
    public <U> Function<ValueObject, U> getFunction(String name) {
        throw new IllegalStateException("Not yet implemented");
    }

    @Override
    public <U> Property<ValueObject, U> getProperty(String name) {
        return (Property<ValueObject, U>) Properties.props.get(name);
    }
}
