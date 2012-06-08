package org.ubiquity;

import junit.framework.Assert;
import org.junit.Test;
import org.ubiquity.util.DefaultCollectionFactory;

/**
 * Date: 21/04/12
 *
 * @author François LAROCHE
 */
public class UbiquityTest {

    @Test
    public void testUbiquity() {
        Ubiquity ubiquity = new Ubiquity();
        Assert.assertEquals(ubiquity.getFactory().getClass(), DefaultCollectionFactory.class);
    }
}
