package be.kuleuven.findaset.model;

import be.kuleuven.findaset.activities.MainActivity;
import be.kuleuven.findaset.model.card.AbstractCard;

public interface TestableFindASet {
    void setTable(AbstractCard[] table);
    void updateTable(int [] cardIndexes);
    void emptyTable();
    AbstractCard getCard(int i);
    void select(int i);
    void unselect(int i);
    AbstractCard[] generateSet(int[][] featureMatrix);
    int[] generateSetFeature(int[][] featureMatrix);
    int[][] getFeatureMatrix();
    void setUI(MainActivity mainActivity);
    int[] getJustForTest();
}
