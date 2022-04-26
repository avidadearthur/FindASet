package be.kuleuven.findaset;

import org.junit.Before;
import org.testng.annotations.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import be.kuleuven.findaset.model.FindASet;
import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.model.card.Card;
import be.kuleuven.findaset.model.card.enums.COLOR;
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

    @org.junit.Test
    @Test
    public void testGeneratingCard() {
        AbstractCard card = new Card(SHAPECOUNT.one, SHADING.open, COLOR.red, TYPE.diamond);
        assertTrue(card.getCount() == SHAPECOUNT.one);
    }

    @org.junit.Test
    @Test
    public void testGeneratingSet() {
        AbstractCard[] set = gameBoard.generateSet();
    }
}
