package be.kuleuven.findaset.model;

import java.util.ArrayList;

import be.kuleuven.findaset.activities.MainActivity;
import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.model.card.AlternativeCard;

public interface TestableFindASet {
    void setTable(AbstractCard[] table);
    void updateTable(ArrayList<Integer> cardIndexes);
    void emptyTable();
    AbstractCard getCard(int i);
    void toggle(int i);
    AbstractCard[] generateSet(int[][] featureMatrix);
    int[] generateSetFeature(int[][] featureMatrix);
    int[][] getFeatureMatrix();
    void setUI(MainActivity mainActivity);
    int[] getJustForTest();

    void startNewGame();
    AlternativeCard AlternativeGetCard(int index);
}
