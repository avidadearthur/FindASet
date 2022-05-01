package be.kuleuven.findaset;

import org.junit.Assert;
import org.junit.Before;
import org.testng.annotations.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import be.kuleuven.findaset.model.FindASet;
import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.model.card.Card;
import be.kuleuven.findaset.model.card.enums.COLOR;
import be.kuleuven.findaset.model.card.enums.EnumHandler;
import be.kuleuven.findaset.model.card.enums.SHADING;
import be.kuleuven.findaset.model.card.enums.SHAPECOUNT;
import be.kuleuven.findaset.model.card.enums.TYPE;
import be.kuleuven.findaset.model.TestableFindASet;

public class GameModelTester {
    private TestableFindASet gameBoard;

    @Before
    public void init() {
        //uncomment the line below once your game model code is ready for testing
        gameBoard = new FindASet();
    }

    /**
     * Checks if card generation by enum and by index both work.
     */
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

    /**
     * Checks if generateSet method creates valid sets according to the rule:
     *
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
        int[] isEqual = {0,0,0,0};
        for(int col = 0; col < 4; col++){
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
        assertTrue(Arrays.stream(isEqual).sum() <= 3);
    }

}
