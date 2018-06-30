package it.polimi.se2018.view;

import it.polimi.se2018.controller.ToolCardFactory;
import it.polimi.se2018.controller.XmlToolCardLoader;
import it.polimi.se2018.model.*;
import it.polimi.se2018.utils.Coordinates;
import it.polimi.se2018.utils.GameUtils;
import it.polimi.se2018.utils.Logger;
import it.polimi.se2018.view.cli.CliImagePrinter;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CliImagePrinterTest {


    @Test
    public void testPrintCell() throws PlacementErrorException {
        CliImagePrinter printer = new CliImagePrinter(System.out);
        Cell cell1 = new Cell(3);
        Cell cell2 = new Cell(Colour.BLUE);
        Cell cell3 = new Cell();
        Die d = new Die(3, new Random(), Colour.BLUE);
        cell3.place(d);
        printer.printCell(cell1);
        printer.printCell(cell2);
        printer.printCell(cell3);

    }

    @Test
    public void testPrintPattern() throws PlacementErrorException {
        Game game = GameUtils.getStartedGame(true);
        CliImagePrinter printer = new CliImagePrinter(System.out);
        Pattern pattern = game.getPlayers().get(0).getPattern();
        Pattern nuovo = pattern.placeDie(new Die(3, new Random(), Colour.RED), new Coordinates(0,3));
        printer.printPattern(nuovo);
        printer.printPatternLarger(nuovo);
    }

    @Test
    public void testPrintDraftPool(){
        Game game = GameUtils.getStartedGame(true);
        CliImagePrinter printer = new CliImagePrinter(System.out);
        printer.printDraftPool(game.getDraftPool().getDice());
    }

    /**
    @Test
    public void testPrintRoundTrack(){
        Game game = GameUtils.getCompleteSinglePlayerGame();
        ArrayList<Die> dice= new ArrayList<Die>();
        dice.add(new Die(6, new Random(), Colour.BLUE));
        for(int i = 0; i < 10; i++) game.getRoundTrack().addAllForRound(i+1, dice);
        CliImagePrinter printer = new CliImagePrinter(System.out);
        printer.printRoundTrack(game.getRoundTrack().getLeftovers());
    }

     */

    @Test
    public void testPrintPrivateObjectiveCard(){
        Game game = GameUtils.getStartedGame(true);
        CliImagePrinter printer = new CliImagePrinter(System.out);
        printer.printPrivateObjectiveCard(game.getPlayers().get(1).getCards()[0]);
    }

    /**
    @Test
    public void testPrintPublicObjectiveCard(){
        PublicObjectiveFactory factory = new PublicObjectiveFactory();
        PublicObjectiveCard[] cards = factory.newInstances(3);
        CliImagePrinter printer = new CliImagePrinter(System.out);
        for(int i = 0; i < 3; i++){
            printer.printPublicObjectiveCard(cards[i]);
        }
    }

     */

    @Test
    public void testPrintToolCard(){
        if (ToolCardFactory.getInstance() == null){
            try {
                XmlToolCardLoader xmlToolCardLoader = new XmlToolCardLoader();
                xmlToolCardLoader.createToolCardFactory();
            } catch (SAXException e) {
                Logger.getDefaultLogger().log("USAXException " + e);
            }
        }
        ToolCard[] cards = ToolCardFactory.getInstance().newInstances(3);
        CliImagePrinter printer = new CliImagePrinter(System.out);
        for(int i = 0; i < 3; i++){
            printer.printToolCard(cards[i]);
        }
    }

    @Test
    public void printTokens(){
        CliImagePrinter printer = new CliImagePrinter(System.out);
        printer.printTokens(5);
    }
    @Test
    public void testPrintScoreBoard(){
        CliImagePrinter printer = new CliImagePrinter(System.out);
        Map<String, Integer> scoreBoard= new HashMap<>();
        scoreBoard.put("Aldo", new Integer(3));
        scoreBoard.put("Giacomo", new Integer(4));
        scoreBoard.put("Giovanni", new Integer(9));
        printer.printScoreBoard(scoreBoard);
    }

    @Test
    public void testPrintPatternSelection(){
        Game game = GameUtils.getSetUpGame(true);
        CliImagePrinter printer = new CliImagePrinter(System.out);
    }
}