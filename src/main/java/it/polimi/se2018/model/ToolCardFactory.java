package it.polimi.se2018.model;

import java.util.Random;

/**
 * @author dvdmff
 * This class implements a factory method pattern to decouple
 * the use of {@link ToolCard} from its instantiation.
 */
public class ToolCardFactory {

    /**
     * This factory method handles the instantiation of <code>n</code>
     * random tool cards with no repetition.
     * @param n The number of distinct tool card instances to retrieve.
     * @return An array containing exactly n instances of distinct
     *         {@link ToolCard}
     */
    public ToolCard[] getInstances(int n){
        return (new Random()).ints(1, 13)
                .limit(n)
                .boxed()
                .map(this::createCard)
                .toArray(ToolCard[]::new);

    }

    /**
     * Helper method to instantiate the tool cards
     * @param randomNumber The number of the card to instantiate.
     * @return The instance of the card corresponding to the index.
     */
    private ToolCard createCard(int randomNumber) {

        String name;
        Colour colour;

        switch (randomNumber) {
            case 1:
                name = "Grozing Pliers";
                colour = Colour.PURPLE;
                break;
            case 2:
                name = "Eglomise Brush";
                colour = Colour.BLUE;
                break;
            case 3:
                name = "Copper Foil Burnisher";
                colour = Colour.RED;
                break;
            case 4:
                name = "Lathekin";
                colour = Colour.YELLOW;
                break;
            case 5:
                name = "Lens Cutter";
                colour = Colour.GREEN;
                break;
            case 6:
                name = "Flux Brush";
                colour = Colour.PURPLE;
                break;
            case 7:
                name = "Glazing Hammer";
                colour = Colour.BLUE;
                break;
            case 8:
                name = "Running Pliers";
                colour = Colour.RED;
                break;
            case 9:
                name = "Cork-backed Straightedge";
                colour = Colour.YELLOW;
                break;
            case 10:
                name = "Grinding Stone";
                colour = Colour.GREEN;
                break;
            case 11:
                name = "Flux Remover";
                colour = Colour.PURPLE;
                break;
            case 12:
                name = "Tap Wheel";
                colour = Colour.BLUE;
                break;
            default:
                return null;
        }
        return new ToolCard(name, colour);
    }
}
