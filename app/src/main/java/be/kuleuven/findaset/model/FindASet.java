package be.kuleuven.findaset.model;

import java.lang.reflect.Array;

import be.kuleuven.findaset.model.card.AbstractCard;

public class FindASet extends AbstractFindASet{
    private AbstractCard[] cards;

    public FindASet() {
        this.cards = new AbstractCard[12];
        setTable(cards);
    }

    /**
     * Generates a correct set of three cards
     * when the game is initialized.
     *
     * @return AbstractCard array of size 3
     */
    @Override
    public AbstractCard[] generateSet(){
        AbstractCard[] set = new AbstractCard[3];
        return set;
    }

    /**
     * Populates AbstractCard[] cards.
     *
     * @param table - empty 12 element array of cards.
     */
    @Override
    public void setTable(AbstractCard[] table) {
        AbstractCard[] set = generateSet();

    }

    @Override
    public void updateTable(int[] cardIndexes) {

    }

    @Override
    public AbstractCard getCard(int i) {
        return null;
    }

    @Override
    public void select(int i) {

    }

    @Override
    public void unselect(int i) {

    }
}
