package it.polimi.se2018.model;

/**
 * @author giorgiolabate
 * Exception thrown when the placement of a Die
 * doesn't respect some restriction imposed by the
 * Game's rules at a 'local' level, on a {@link Cell}
 * or at a 'global' level, on a {@link Pattern}
 */
public class PlacementErrorException extends Exception{
    public PlacementErrorException(String msg){
        super(msg);
    }
}
