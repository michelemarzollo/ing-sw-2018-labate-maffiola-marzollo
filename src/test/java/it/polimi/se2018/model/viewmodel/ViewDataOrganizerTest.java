package it.polimi.se2018.model.viewmodel;

import it.polimi.se2018.model.Colour;
import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.model.events.*;
import it.polimi.se2018.utils.GameUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * Unit tests for ViewDataOrganizer.
 */
public class ViewDataOrganizerTest {

    /**
     * Tests if a GameSetup message is pushed correctly into the data organizer.
     */
    @Test
    public void testPushGameSetup() {
        Game game = GameUtils.getSetUpGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");
        ModelUpdate gameSetup = new GameSetup(game);
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        organizer.push(gameSetup);

        Assert.assertEquals(gameSetup, organizer.getGameSetup());
    }

    /**
     * Tests if a GameEnd message is pushed correctly into the data organizer.
     */
    @Test
    public void testPushGameEnd() {
        Map<String, Integer> scoreboard = new HashMap<>();
        scoreboard.put("1", 1);
        scoreboard.put("2", 2);

        ModelUpdate gameEnd = new GameEnd(scoreboard);
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        organizer.push(gameEnd);

        Assert.assertEquals(scoreboard, organizer.getScoreBoard());
    }

    /**
     * Tests if a DraftPoolUpdate message is pushed correctly into the data organizer.
     */
    @Test
    public void testPushDraftPoolUpdate() {
        List<Die> draftPool = Collections.singletonList(
                new Die(new Random(), Colour.YELLOW)
        );
        ModelUpdate draftPoolUpdate = new DraftPoolUpdate(draftPool);
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        organizer.push(draftPoolUpdate);

        Assert.assertEquals(draftPool, organizer.getDraftPool());
    }

    /**
     * Tests if a RoundTrackUpdate message is pushed correctly into the data organizer.
     */
    @Test
    public void testPushRoundTrackUpdate() {
        List<List<Die>> roundTrack = Collections.singletonList(
                Collections.singletonList(
                        new Die(new Random(), Colour.YELLOW)
                ));
        ModelUpdate roundTrackUpdate = new RoundTrackUpdate(roundTrack);
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        organizer.push(roundTrackUpdate);

        Assert.assertEquals(roundTrack, organizer.getRoundTrack());
    }

    /**
     * Tests if a PlayerStatus message is pushed correctly into the data organizer.
     */
    @Test
    public void testPushPlayerStatus() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");

        Player player = game.getPlayers().get(0);

        ModelUpdate playerStatus = new PlayerStatus(player);
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        organizer.push(playerStatus);

        Assert.assertEquals(1, organizer.getAllPlayerStatus().size());
        Assert.assertEquals(playerStatus, organizer.getPlayerStatus(player.getName()));
    }

    /**
     * Tests if a PlayerConnectionStatus message is pushed correctly into the data organizer.
     */
    @Test
    public void testPushPlayerConnectionStatus() {
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");

        Player player = game.getPlayers().get(0);

        ModelUpdate playerConnectionStatus = new PlayerConnectionStatus(player);
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        organizer.push(playerConnectionStatus);

        Assert.assertEquals(1, organizer.getAllConnectionStatus().size());
        Assert.assertEquals(playerConnectionStatus, organizer.getConnectionStatus(player.getName()));
        int index = organizer.getAllConnectionStatus().indexOf(playerConnectionStatus);
        Assert.assertEquals(index, organizer.getChangedConnectionIndex());
    }

    /**
     * Tests if a NextTurn message is pushed correctly into the data organizer.
     */
    @Test
    public void testPushNextTurn(){
        Game game = GameUtils.getStartedGame(true);
        if (game == null)
            Assert.fail("Error on game initialization");

        ModelUpdate nextTurn = new NextTurn(game.getTurnManager().getCurrentTurn());
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        organizer.push(nextTurn);

        Assert.assertEquals(nextTurn, organizer.getNextTurn());
        Assert.assertTrue(organizer.isTurnChanged());
    }

    /**
     * Tests if local player name is set correctly.
     */
    @Test
    public void testSetLocalPlayer(){
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        String playerName = "pippo";
        organizer.setLocalPlayer(playerName);
        Assert.assertEquals(playerName, organizer.getLocalPlayer());
    }

    /**
     * Tests that the data organizer returns the default values for missing data.
     */
    @Test
    public void testNoPushedData(){
        ViewDataOrganizer organizer = new ViewDataOrganizer();
        Assert.assertTrue(organizer.getDraftPool().isEmpty());
        Assert.assertTrue(organizer.getRoundTrack().isEmpty());
        Assert.assertFalse(organizer.isTurnChanged());
        Assert.assertEquals(-1, organizer.getChangedConnectionIndex());
        Assert.assertNull(organizer.getGameSetup());
        Assert.assertNull(organizer.getScoreBoard());
        Assert.assertNull(organizer.getNextTurn());
        Assert.assertTrue(organizer.getAllConnectionStatus().isEmpty());
        Assert.assertTrue(organizer.getAllPlayerStatus().isEmpty());
        Assert.assertNull(organizer.getLocalPlayer());
    }
}
