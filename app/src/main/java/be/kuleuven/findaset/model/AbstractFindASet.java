package be.kuleuven.findaset.model;

import be.kuleuven.findaset.model.card.AbstractCard;

public abstract class AbstractFindASet implements TestableFindASet {


    public abstract int[][] getFeatureMatrix();

    public abstract AbstractCard[] generateSet(int[][] featureMatrix);

    public abstract void setTable(AbstractCard[] table);

    public abstract void updateTable(int[] cardIndexes);

    public abstract AbstractCard getCard(int i);

    public abstract void select(int i);

    public abstract void unselect(int i);
}
