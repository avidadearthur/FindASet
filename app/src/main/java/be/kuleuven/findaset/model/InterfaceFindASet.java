package be.kuleuven.findaset.model;

import java.util.ArrayList;

import be.kuleuven.findaset.activities.MainActivity;

public interface InterfaceFindASet {
    void setUI(MainActivity mainActivity);

    void startNewGame();

    void initializeTable(int sizeOfCards);

    //////////////////FEATURE MATRIX GENERATION/////////////////////////////////////////
    int[][] getFeatureMatrix();

    //////////////////SET CARDS TABLE (Find All)////////////////////////////////////
    void setCardsTable(int sizeOfCards);

    //////////////////CHECK FOR SET////////////////////////////////////////////////
    boolean checkSet(ArrayList<Integer> testedCardsIndex);
    boolean checkFeatureMatrix(int[][] featureMatrix, boolean isSet);

    //////////////////Update Table////////////////////////////////////////////////
    void updateTable(ArrayList<Integer> selectedCardsIndex);

    boolean checkAllSetOnPage();

    void checkWin();

    void toggle(int index);

    void select(int index);

    void unselect(int index);

    void showSet();

    ArrayList<Integer> getCardsIdTable();

    Boolean getIsCardSelected(int index);

    ArrayList<Integer> getSelectedCardsIndex();

    ArrayList<Integer> getFirstSetOnPage();

    int getHints();
}
