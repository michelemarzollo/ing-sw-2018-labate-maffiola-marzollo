package it.polimi.se2018.model;

/**
 * Interface for pattern loaders.
 * @author dvdmff
 */
public interface PatternLoader {
    /**
     * Loads {@code n} unique pattern among all te possible ones.
     * @param n The number of unique patterns to load.
     * @return An array of size at most {@code n} containing the loaded
     * patterns.
     */
    Pattern[] load(int n);
}
