package be.kuleuven.findaset.model;

import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.test.TestableFindASet;

public abstract class AbstractFindASet implements TestableFindASet {
    @Override
    public void setTable(AbstractCard[] table) {

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
