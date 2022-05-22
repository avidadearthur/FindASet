package be.kuleuven.findaset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.testng.annotations.Test;

import java.util.Arrays;

import be.kuleuven.findaset.model.FindASet;

public class GameModelTester {
    private TestableFindASet gameBoard;

    @Before
    public void init() {
        //uncomment the line below once your game model code is ready for testing
        gameBoard = new FindASet();
    }

    /**
     * Creates two new CardElement objects based on the int based
     * logic and test both constructors.
     */
    /*
    @org.junit.Test
    public void testGenerateCardElement() {
        // test to create a green striped diamond
        CardElement element1 = new CardElement(123);
        CardElement element2 = new CardElement(1, 2, 3);

        assertSame(element1.getColor(), element2.getColor());
        assertSame(element1.getShading(), element2.getShading());
        assertSame(element1.getType(), element2.getType());
        assertSame(element1.getId(), element2.getId());
        assertEquals(element1.getFeatures(), element2.getFeatures());
    }


    @org.junit.Test
    public void testGenerateAlternativeCard() {
        // id = 123, corresponds to a green striped diamond
        AlternativeCard card1 = new AlternativeCard(3123);
        AlternativeCard card2 = new AlternativeCard(123, 3);

        assertSame(card1.getCardId(), card2.getCardId());
        assertSame(card1.getElementId(), card2.getElementId());
        assertSame(card1.getSize(), card2.getSize());
        assertEquals(card1.getCardFeatures(), card2.getCardFeatures());
    }


    @org.junit.Test
    public void testToggleAlternativeCard() {
        // id = 123, corresponds to a green striped diamond
        AlternativeCard card1 = new AlternativeCard(3123);
        assertFalse(card1.isSelected());
        card1.toggle();
        assertTrue(card1.isSelected());
    }

     */

    /*
    @org.junit.Test
    @Test
    public void testGeneratingCard() {
        EnumHandler handler = new EnumHandler();
        AbstractCard card1 = new Card(SHAPECOUNT.one, SHADING.open, COLOR.red, TYPE.diamond);
        AbstractCard card2 = new Card(handler.shapeCount(1), handler.shading(3),
                handler.color(1), handler.type(1));
        assertSame(card1.getCount(), card2.getCount());
        assertSame(card1.getColor(), card2.getColor());
        assertSame(card1.getShading(), card2.getShading());
        assertSame(card1.getType(), card2.getType());
    }
    */

    /**
     * Checks if generateSet method creates valid sets according to the rule:
     * <p>
     * The number of features that are all the same and the number of features
     * that are all different may break down as
     * 0 the same + 4 different;
     * 1 the same + 3 different;
     * 2 the same + 2 different;
     * 3 the same + 1 different.
     */
    @org.junit.Test
    @Test
    public void testGeneratingSet() {
        int[][] setCandidate = gameBoard.getFeatureMatrix();
        // The following matrix is a possible Feature Matrix
        /*
         *             nr.     type    color   shading
         *  card n1.    1       3        2         1
         *
         *  card n2.    3       1        2         3
         *
         *  card n3.    1       2        2         2
         *  --------------------------------------------
         *  bool        1       0        1         0
         */

        // isEqual is an array that keeps track of whether all cards share the
        // same feature(1) or if at the least one is different from the others(0).
        int[] isEqual = {0, 0, 0, 0};
        for (int col = 0; col < 4; col++) {
            for (int row = 0; row <= 1; row++) {
                if (setCandidate[row][col] != setCandidate[row + 1][col]) {
                    continue;
                }
                isEqual[col] = 1;
            }
        }

        // Consider the following combinations where 1 represents a feature column
        // that is shared by all three cards
        /*  {   {0,0,0,0},{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}, --> sums to 1
                {1,0,0,1},{0,1,0,1},{0,0,1,1}, --> sums to 2
                {1,0,1,1},{0,1,1,1}} --> sums to 3
        */
        assertTrue(Arrays.stream(isEqual).sum() <= 3 | Arrays.stream(isEqual).sum() == 6);
    }

    /**
     * Check that when table is updated at least one set is present.
     */
    @org.junit.Test
    @Test
    public void testUpdateTable() {
    }
}
