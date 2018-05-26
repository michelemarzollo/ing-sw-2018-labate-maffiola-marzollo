package it.polimi.se2018.controller;

import it.polimi.se2018.model.Die;
import it.polimi.se2018.model.Game;
import it.polimi.se2018.model.events.Action;
import it.polimi.se2018.model.events.ViewMessage;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.MockView;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class GlazingHammerBehaviourTest {

    @Test
    public void testAskParameters(){
        MockView mockView = new MockView("Pippo");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.ACTIVATE_TOOL_CARD,
                "Pippo"
        );

        GlazingHammerBehaviour behaviour = new GlazingHammerBehaviour();
        behaviour.askParameters(message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());
    }

    @Test
    public void testUsageSuccess(){
        MockView mockView = new MockView("Pippo");
        Game game = GameUtils.getHalfwayGame();
        if(game == null)
            Assert.fail("Error on game initialization");

        ViewMessage message = new ViewMessage(
                mockView,
                Action.APPLY_TOOL_CARD,
                "Pippo"
        );

        GlazingHammerBehaviour behaviour = new GlazingHammerBehaviour();
        behaviour.useToolCard(game, message);

        Assert.assertEquals(0, mockView.getCalledMethods().size());
        List<Die> oldDraftPool = GameUtils.getDice(false);
        List<Die> newDraftPool = game.getDraftPool().getDice();

        //check dice have been rolled
        int expectedValue = new Random(0).nextInt(5) + 1;
        for(Die die : newDraftPool)
            Assert.assertEquals(expectedValue, die.getValue());

        // check all colours are kept
        for (Die expected : oldDraftPool) {
            boolean match = false;
            for (int j = 0; j < newDraftPool.size() && !match; ++j){
                match = expected.getColour() == newDraftPool.get(j).getColour();
                if(match)
                    newDraftPool.remove(j);
            }
            Assert.assertTrue(match);
        }

    }
}
