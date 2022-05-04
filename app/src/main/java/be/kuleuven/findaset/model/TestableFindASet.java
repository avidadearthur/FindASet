package be.kuleuven.findaset.model;

import java.util.ArrayList;
import java.util.Collection;

import be.kuleuven.findaset.activities.MainActivity;
import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.model.card.AlternativeCard;

public interface TestableFindASet {

    void emptyTable();
    void toggle(int index);

    int[][] getFeatureMatrix();
    void setUI(MainActivity mainActivity);
    int[] getJustForTest();

    void startNewGame();
    AlternativeCard AlternativeGetCard(int index);
    void alternativeUpdateTable(ArrayList<Integer> selectedCardsIndex);

    boolean checkSet(ArrayList<Integer> selectedCardsIndex);

    ArrayList<Integer> getSelectedCardsIndex();

    ArrayList<Boolean> getIsCardSelected();

    void select(int index);
    void unselect(int index);
}
