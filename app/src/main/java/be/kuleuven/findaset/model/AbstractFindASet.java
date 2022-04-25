package be.kuleuven.findaset.model;

import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.test.TestableFindASet;

public abstract class AbstractFindASet implements TestableFindASet {

    public abstract AbstractCard[] generateSet();

    public abstract void setTable(AbstractCard[] table);

    public abstract void updateTable(int[] cardIndexes);

    public abstract AbstractCard getCard(int i);

    public abstract void select(int i);


    public abstract void unselect(int i);
}
