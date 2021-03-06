package it.polimi.se2018.model;

import java.io.Serializable;

/**
 * The class represents a Cell of a {@link Pattern}.
 * Each Cell may have a value restriction, a Colour restriction
 * or no restriction for the Die to be placed. The restrictions are exclusive:
 * a Cell may have only a value or a colour restriction, not both of them.
 *
 * @author giorgiolbt
 */
public class Cell implements Serializable {

    /**
     * Value restriction (0 if there isn't a restriction of this type)
     */
    private int value;

    /**
     * Colour restriction (null if there isn't a restriction of this type)
     */
    private Colour colour;

    /**
     * Die placed in the Cell: it is left to null until a {@code place}
     * call (until a placedDie is placed).
     */
    private Die placedDie;

    /**
     * Default constructor: constructs a new Cell with no restriction.
     * At the beginning the Cell is empty.
     */
    public Cell() {
    }

    /**
     * Constructs a new Cell with a value restriction.
     * At the beginning the Cell is empty.
     * An  IllegalArgumentException is thrown if the value is not between 1 and 6.
     *
     * @param value The value of the restriction.
     */
    public Cell(int value) {
        if (value < 1 || value > 6) {
            throw new IllegalArgumentException("Restriction on the Cell's value must be between 1 and 6 ");
        } else {
            this.value = value;
        }
    }

    /**
     * Constructs a Cell with a Colour restriction.
     * At the beginning the Cell is empty.
     *
     * @param colour The colour of the restriction.
     */
    public Cell(Colour colour) {
        this.colour = colour;
    }

    /**
     * Copy constructor
     *
     * @param cell The cell that has to be copied.
     */
    public Cell(Cell cell) {
        this.colour = cell.colour;
        this.value = cell.value;
        this.placedDie = cell.placedDie;
    }

    /**
     * Getter for the value restriction on the Cell.
     *
     * @return The value of the restriction
     * (0 if the Cell has no value restriction).
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter for the colour restriction on the Cell.
     *
     * @return The colour of the restriction
     * (null if the Cell has no Colour restriction).
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Getter for the placedDie placed in the Cell.
     *
     * @return the placedDie placed in the Cell
     * (null if the Cell is still empty).
     */
    public Die getDie() {
        return placedDie;
    }

    /**
     * Place the Die {@code die} in the Cell.
     *
     * @param die the Die to be placed in the Cell.
     * @throws PlacementErrorException if the Cell is full or
     *                                 if it doesn't respect value or Colour restriction.
     *                                 The control is made only at a 'local' level:
     *                                 it checks only the restriction on the specific
     *                                 Cell, not other placement restrictions.
     */
    public void place(Die die) throws PlacementErrorException {
        place(die, Restriction.DEFAULT);
    }

    private boolean hasBadValue(Die die) {
        return value != 0 && value != die.getValue();
    }

    private boolean hasBadColour(Die die) {
        return colour != null && colour != die.getColour();
    }

    public void place(Die die, Restriction restriction) throws PlacementErrorException {
        if (placedDie != null) {
            throw new PlacementErrorException(
                    "This cell has already a placed placedDie. Choose another cell.");
        }
        if (restriction.checkColourConstraint() && hasBadColour(die)) {
            throw new PlacementErrorException(
                    "The placedDie's colour is different from the colour restriction of the cell");
        }
        if (restriction.checkValueConstraint() && hasBadValue(die)) {
            throw new PlacementErrorException(
                    "The placedDie's value is different from the value restriction of the cell");
        }
        placedDie = die;
    }


    /**
     * Remove the placedDie from the Cell if there is one or left the
     * Cell empty otherwise. It's necessary for ToolCards that allows
     * to move dice on the {@link Pattern}.
     *
     * @return the Die removed.
     */
    public Die remove() {
        Die tmp = placedDie;
        placedDie = null;
        return tmp;
    }
}
