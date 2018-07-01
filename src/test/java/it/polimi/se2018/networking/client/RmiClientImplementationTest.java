package it.polimi.se2018.networking.client;

import org.junit.Assert;
import org.junit.Test;

public class RmiClientImplementationTest {

    @Test
    public void testInstantiation() {
        final String name = "Pippo";
        RmiClientImplementation client = new RmiClientImplementation(new DummyClient(name));
        String actualName = client.getUsername();
        Assert.assertEquals(name, actualName);
    }
}
