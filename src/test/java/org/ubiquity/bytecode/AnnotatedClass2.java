package org.ubiquity.bytecode;

import org.ubiquity.annotation.CopyRename;

/**
 * Date: 26/05/12
 *
 * @author François LAROCHE
 */
public class AnnotatedClass2 {
    private String toto;

    @CopyRename(propertyName = "property1")
    public String getToto() {
        return toto;
    }

    public void setToto(String toto) {
        this.toto = toto;
    }
}
