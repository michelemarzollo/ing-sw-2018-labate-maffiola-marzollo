package it.polimi.se2018.networking.client;

import org.junit.Assert;
import org.junit.Test;

import java.rmi.RemoteException;

public class RmiClientImplementationTest {

    @Test
    public void testInstantiation(){
        final String name = "Pippo";
        RmiClientImplementation client = new RmiClientImplementation(new DummyClient(name));
        String actualName = null;
        try {
            actualName = client.getUsername();
        } catch (RemoteException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(name, actualName);
    }
}
