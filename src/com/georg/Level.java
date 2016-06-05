package com.georg;

import com.georg.Generator.SudokuGen;

/**
 * Created by Georg on 30/05/16.
 * <p>
 * Enum that holds important information
 * to each difficulty level.
 * Includes:
 * <ul>
 * <li>{@link #ExtremelyEasy Extremely Easy}</li>
 * <li>{@link #Easy}</li>
 * <li>{@link #Medium}</li>
 * <li>{@link #Hard}</li>
 * <li>{@link #Evil Devil's playground}</li>
 * </ul>
 */
public enum Level {
    /**
     * Extremly easy.
     * <table>
     * <tr>
     * <td>Total Givens</td>
     * <td>+50</td>
     * </tr>
     * <tr>
     * <td>Lower bounds given<br>for Row and Column</td>
     * <td>5</td>
     * </tr>
     * <tr>
     * <td>Algorithm to use</td>
     * <td>{@link SudokuGen#seq4RandomizingGlobally()} () Randomizing globally (4)}</td>
     * </tr>
     * </table>
     */
    ExtremelyEasy("Extremly Easy", 50, 81, 5),
    /**
     * Easy.
     * <table>
     * <tr>
     * <td>Total Givens</td>
     * <td>36-49</td>
     * </tr>
     * <tr>
     * <td>Lower bounds given<br>for Row and Column</td>
     * <td>4</td>
     * </tr>
     * <tr>
     * <td>Algorithm to use</td>
     * <td>{@link SudokuGen#seq4RandomizingGlobally() Randomizing globally (4)}</td>
     * </tr>
     * </table>
     */
    Easy("Easy", 36, 39, 4),
    /**
     * Medium.
     * <table>
     * <tr>
     * <td>Total Givens</td>
     * <td>32-35</td>
     * </tr>
     * <tr>
     * <td>Lower bounds given<br>for Row and Column</td>
     * <td>3</td>
     * </tr>
     * <tr>
     * <td>Algorithm to use</td>
     * <td>{@link SudokuGen#seq3JumpingOneCell(int) Jumping one Cell (3)}</td>
     * </tr>
     * </table>
     */
    Medium("Medium", 32, 35, 3),
    /**
     * Hard.
     * <table>
     * <tr>
     * <td>Total Givens</td>
     * <td>28-31</td>
     * </tr>
     * <tr>
     * <td>Lower bounds given<br>for Row and Column</td>
     * <td>2</td>
     * </tr>
     * <tr>
     * <td>Algorithm to use</td>
     * <td>{@link SudokuGen#seq2WanderingAlongS(int) Wandering along "S"}</td>
     * </tr>
     * </table>
     */
    Hard("Hard", 28, 31, 2),
    /**
     * Devil's playground, also referenced
     * as Evil
     * <table>
     * <tr>
     * <td>Total Givens</td>
     * <td>22-27</td>
     * </tr>
     * <tr>
     * <td>Lower bounds given<br>for Row and Column</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>Algorithm to use</td>
     * <td>{@link SudokuGen#seq1Left2RightAndTop2Bottom(int) Left to Right then Top to Bottom}</td>
     * </tr>
     * </table>
     */
    Evil("Devil's playground", 22, 27, 0);

    // The name of the object
    private final String name;
    // The minimum number of total givens
    private final int minTotalGiven;
    // The maximum number of total givens
    private final int maxTotalGiven;
    // The minimum amount of givens for all rows and cols.
    private final int minGivenRC;

    /**
     * Initializer for a Level enum
     * object.
     *
     * @param name          The name
     * @param minTotalGiven The min number of total givens. (included)
     * @param maxTotalGiven The max number of total givens. (included)
     * @param minGivenRC    The min number of givens per Row and/or Column
     */
    Level(String name, int minTotalGiven, int maxTotalGiven, int minGivenRC) {
        this.name = name;
        this.minTotalGiven = minTotalGiven;
        this.maxTotalGiven = maxTotalGiven;
        this.minGivenRC = minGivenRC;
    }

    /**
     * Returns the correctly user
     * friendly name of the enum.
     *
     * @return Real name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * @return The min number of total given
     * on the field.
     */
    public int getMinTotalGiven() {
        return minTotalGiven;
    }

    /**
     * @return The max number of total given
     * in a field. -1 if no limit.
     */
    public int getMaxTotalGiven() {
        return maxTotalGiven;
    }


    /**
     * @return The minimum number of total givens
     * per Row and/or Column.
     */
    public int getMinGivenRC() {
        return minGivenRC;
    }

}