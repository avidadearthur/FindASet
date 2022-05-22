package be.kuleuven.findaset.model;

import java.util.ArrayList;
import java.util.Collection;

import be.kuleuven.findaset.activities.LearningActivity;
import be.kuleuven.findaset.activities.MainActivity;

public interface TestableFindASet {
    void setUI(MainActivity mainActivity);
    void setUILearning(LearningActivity learningActivity);

    void startNewGame();
    void initializeTable(int sizeOfCards);

    void toggle(int index);
    void select(int index);
    void unselect(int index);

    int[][] getFeatureMatrix();
    int[] getJustForTest();
    void alternativeUpdateTable(ArrayList<Integer> selectedCardsIndex);

    boolean checkSet(ArrayList<Integer> selectedCardsIndex);

    ArrayList<Integer> getSelectedCardsIndex();
    ArrayList<Integer> getCardsIdTable();
    ArrayList<Boolean> getIsCardSelected();

}
