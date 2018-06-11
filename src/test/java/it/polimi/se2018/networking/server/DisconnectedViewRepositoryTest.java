package it.polimi.se2018.networking.server;

import it.polimi.se2018.model.events.GameEnd;
import it.polimi.se2018.networking.client.DummyClient;
import it.polimi.se2018.view.VirtualView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * Unit tests for DisconnectedViewRepository.
 */
public class DisconnectedViewRepositoryTest {

    /**
     * The repository to test.
     */
    private DisconnectedViewsRepository repository;

    /**
     * Resets the repository for each test.
     */
    @Before
    public void setup(){
        repository = DisconnectedViewsRepository.getInstance();
        repository.flush();
    }

    /**
     * Tests if a view can be added and retrieved.
     */
    @Test
    public void testAddAndRetrieve() {
        repository = DisconnectedViewsRepository.getInstance();
        String name = "Pippo";
        VirtualView view = new VirtualView(new DummyClient(name));
        repository.addView(view);
        VirtualView retrievedView = repository.tryRetrieveViewFor(name);

        Assert.assertEquals(view.getPlayerName(), retrievedView.getPlayerName());
    }

    /**
     * Tests if an attempt to retrieve an already expired view returns null.
     */
    @Test
    public void testRetrieveExpiredView() {
        repository = DisconnectedViewsRepository.getInstance();
        String name = "Pippo";
        VirtualView retrievedView = repository.tryRetrieveViewFor(name);

        Assert.assertNull(retrievedView);
    }

    /**
     * Tests if an attempt to retrieve a view marked as expired returns null.
     */
    @Test
    public void testViewExpiresCorrectly() {
        repository = DisconnectedViewsRepository.getInstance();
        String name = "Pippo";

        VirtualView view = new VirtualView(new DummyClient(name));
        repository.addView(view);
        view.update(new GameEnd(new HashMap<>()));

        VirtualView retrievedView = repository.tryRetrieveViewFor(name);

        Assert.assertNull(retrievedView);
    }
}
