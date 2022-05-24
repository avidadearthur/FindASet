package be.kuleuven.findaset.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import be.kuleuven.findaset.activities.MainActivity;

/**
 * Parent class of the set Find All , Find learning and Find Ten game extensions.
 * It contains the main logic common to all games.
 *
 * For description of the methods refer to InterfaceFindASet
 *
 * Description of the fields:
 *
 *      cardsIdTable - Array of 12 4-digit numbers that represent the
 *                     features contained on each card currently displayed
 *
 *      selectedCardsIndex - Array that keeps track of the index of the cards
 *                          that are selected.
 *
 *      isCardSelected -  Array that keeps track of whether cards are selected
 *                        or not.
 *
 *      foundedSetCardsIds - Array that stores which card Ids have already been
 *                         found and should not be put on the board again.
 *
 *      remainingCardsIds - Array of card Ids that can still be added to the table
 *
 *      firstSetOnPage - Stores last set generated (Indexes of the cards) on
 *                      the table to be used when the user wants a hint.
 *
 *      hints - Count of how many hints have been given
 *
 *      win - Self-explanatory
 *
 */
public abstract class AbstractFindASet implements InterfaceFindASet {
    protected ArrayList<Integer> cardsIdTable;
    protected ArrayList<Integer> selectedCardsIndex;
    protected ArrayList<Boolean> isCardSelected;
    protected ArrayList<Integer> foundedSetCardsIds;
    protected ArrayList<Integer> remainingCardsIds;
    protected ArrayList<Integer> firstSetOnPage;
    protected int hints;
    protected boolean win;

    protected MainActivity mainActivity;

    @Override
    public final void setUI(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public AbstractFindASet() {
    }

    protected void generateAllCardsIdsList() {
        try {
            remainingCardsIds = mainActivity.notifyAllCardsIds();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startNewGame() {
        generateAllCardsIdsList();

        this.win = false;
        this.hints = 0;
        this.selectedCardsIndex = new ArrayList<>(3);
        this.foundedSetCardsIds = new ArrayList<>();

        this.isCardSelected = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            this.isCardSelected.add(false);
        }
        this.firstSetOnPage = new ArrayList<>(3);
        for (int i = 0; i < 12; i++) {
            this.firstSetOnPage.add(9999);
        }

        initializeTable(12);
        setCardsTable(12);

        mainActivity.notifyNewGame(12);
    }

    @Override
    public void initializeTable(int numOfCardsOnTable) {
        this.cardsIdTable = new ArrayList<>(12);
        for (int i = 0; i < numOfCardsOnTable; i++) {
            cardsIdTable.add(9999);
        }
    }

    //START OF FEATURE MATRIX GENERATION
    @Override
    public int[][] getFeatureMatrix() {
        Random rd = new Random();
        int[][] featureMatrix = new int[3][4];
        int[] isEqual = new int[4];
        int sum = 0;

        chooseEqualFeaturesCol(rd, isEqual, sum);
        addFeatureNumbers(rd, featureMatrix, isEqual);

        return featureMatrix;
    }

    private void chooseEqualFeaturesCol(Random rd, int[] isEqual, int sum) {
        for (int i = 0; i < 4; i++) {

            isEqual[i] = rd.nextInt(2);
            sum += isEqual[i];
            if (sum == 4) {
                isEqual[i] = 0;
            }
        }
    }

    private void addFeatureNumbers(Random rd, int[][] featureMatrix, int[] isEqual) {
        for (int col = 0; col < 4; col++) {

            if (isEqual[col] == 1) getEqualFeatureNumber(rd, featureMatrix, col);
            else {
                getDifferentFeatureNumber(rd, featureMatrix, col);
            }
        }
    }

    private void getEqualFeatureNumber(Random rd, int[][] featureMatrix, int col) {
        int value = rd.nextInt(3) + 1;
        for (int row = 0; row < 3; row++) {
            featureMatrix[row][col] = value;
        }
    }

    private void getDifferentFeatureNumber(Random rd, int[][] featureMatrix, int col) {
        ArrayList<Integer> differentFeaturesNumbers = new ArrayList<>(3);
        int i = 0;
        while (i < 3) {
            int next = rd.nextInt(3) + 1;
            if (!differentFeaturesNumbers.contains(next)) {
                differentFeaturesNumbers.add(next);
                i++;
            }
        }
        for (int row = 0; row < 3; row++) {
            featureMatrix[row][col] = differentFeaturesNumbers.get(row);
        }
    }
    //END OF FEATURE MATRIX GENERATION

    //START OF SET GENERATION
    @Override
    public ArrayList<Integer> generateSet() {
        boolean isFound;
        ArrayList<Integer> set = new ArrayList<>();

        do {
            set.clear();
            isFound = false;
            int[][] setFeatureMatrix = getFeatureMatrix();
            for (int[] features : setFeatureMatrix) {
                int cardId = features[0] * 1000 + features[1] * 100 + features[2] * 10 + features[3];
                if (foundedSetCardsIds.contains(cardId)) {
                    isFound = true;
                    break;
                } else {
                    set.add(cardId);
                }
            }
        }
        while (isFound);

        return set;
    }
    //END OF FEATURE SET GENERATION

    //START SET CARDS TABLE (Find All)
    @Override
    public void setCardsTable(int numOfCardsOnTable) {
        Random rd = new Random();

        if (foundedSetCardsIds.size() <= 69) {
            ArrayList<Integer> set = generateSet();
            placeSet(rd, set);
            place9Cards(rd);
        }
        // if you find over 69, there are 12 cards remaining so the game adds
        // the 12 remaining cards
        else {
            place12Cards(rd);
        }
    }

    private void placeSet(Random rd, ArrayList<Integer> set) {
        for (int i = 0; i < set.size(); i++) {
            int randomIndex;
            do {
                randomIndex = rd.nextInt(12);
            }
            // 9999 = no card
            while (cardsIdTable.get(randomIndex) != 9999);
            cardsIdTable.set(randomIndex, set.get(i));
            firstSetOnPage.set(i,randomIndex);
        }
    }

    private void place9Cards(Random rd) {
        remainingCardsIds.removeAll(foundedSetCardsIds);
        while (cardsIdTable.contains(9999)) {
            int randomChosenCardIndex;
            int nextChosenId;
            do {
                randomChosenCardIndex = rd.nextInt(remainingCardsIds.size());
                nextChosenId = remainingCardsIds.get(randomChosenCardIndex);
            }
            while (cardsIdTable.contains(nextChosenId));
            int position = cardsIdTable.indexOf(9999);
            cardsIdTable.set(position, nextChosenId);
        }
    }

    private void place12Cards(Random rd) {
        remainingCardsIds.removeAll(foundedSetCardsIds);
        for (int i = 0; i < remainingCardsIds.size(); i++) {
            int randomIndex;
            do {
                randomIndex = rd.nextInt(remainingCardsIds.size());
            }
            while (cardsIdTable.get(randomIndex) != 9999);
            cardsIdTable.set(randomIndex, remainingCardsIds.get(i));
        }
    }
    //END SET CARDS TABLE (Find All)

    //START OF CHECK FOR SET
    @Override
    public boolean checkSet(ArrayList<Integer> testedCardsIndex) {
        int[][] featureMatrix = new int[3][4];
        boolean isSet = true;

        getTestFeatureMatrix(testedCardsIndex, featureMatrix);

        isSet = checkFeatureMatrix(featureMatrix, isSet);
        return isSet;
    }

    private void getTestFeatureMatrix(ArrayList<Integer> testedCardsIndex, int[][] featureMatrix) {
        for (int i = 0; i < 3; i++) {
            int nextCardId = cardsIdTable.get(testedCardsIndex.get(i));
            int size = nextCardId/1000;
            int color = (nextCardId%1000)/100;
            int shading = (nextCardId%100)/10;
            int type = nextCardId%10;
            featureMatrix[i][0] = size;
            featureMatrix[i][1] = color;
            featureMatrix[i][2] = shading;
            featureMatrix[i][3] = type;
        }
    }

    @Override
    public boolean checkFeatureMatrix(int[][] featureMatrix, boolean isSet) {
        for (int col = 0; col < 4; col++) {
            // Compares the features of the first of the first two cards
            // if the first two are the same -> check the third one
            if (featureMatrix[0][col] == featureMatrix[1][col]) {
                // if the third is different = no set
                if (featureMatrix[1][col] != featureMatrix[2][col]) {
                    isSet = false;
                    break;
                }
            }
            // if the first two are different check if the third one is also different
            // if there's a set it will be the combination of features 1 2 and 3 in different cards
            // the sum in a set should always be 6
            else if (featureMatrix[0][col] + featureMatrix[1][col] + featureMatrix[2][col] != 6) {
                isSet = false;
                break;
            }
        }
        return isSet;
    }
    //END OF CHECK FOR SET

    //START OF UPDATE TABLE
    @Override
    public void updateTable(ArrayList<Integer> selectedCardsIndex) {
        Random rd = new Random();

        for (int i = 0; i < 3; i++) {
            //1.
            int selectedIndex = selectedCardsIndex.get(i);
            unselect(selectedIndex);
            foundedSetCardsIds.add(cardsIdTable.get(selectedIndex));
            mainActivity.notifyFoundedCardsChange(foundedSetCardsIds.size());
            replace3Cards(rd, selectedIndex);
        }

        checkWin();

        if (win) {
            for (int l = 0; l < cardsIdTable.size(); l++) {
                mainActivity.notifyDisabled(l);
            }
            try {
                mainActivity.notifyWin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void replace3Cards(Random rd, int selectedIndex) {
        if (foundedSetCardsIds.size() <= 69) {
            int newCardId;

            do {
                int nr = rd.nextInt(3) + 1;
                int color = rd.nextInt(3) + 1;
                int shading = rd.nextInt(3) + 1;
                int type = rd.nextInt(3) + 1;

                newCardId = nr * 1000 + color * 100 + shading * 10 + type;
            }
            while (cardsIdTable.contains(newCardId) || foundedSetCardsIds.contains(newCardId));

            cardsIdTable.set(selectedIndex, newCardId);
            mainActivity.notifyCard(selectedIndex);
        }
    }
    //END OF UPDATE TABLE

    @Override
    public boolean checkAllSetOnPage() {
        //Loop over all the possible card combinations
        ArrayList<Integer> setCandidates = new ArrayList<>(3);
        setCandidates.add(99);
        setCandidates.add(99);
        setCandidates.add(99);
        int cardsIdTableSize = cardsIdTable.size();
        boolean setExisted = false;
        for (int i = 0; i < cardsIdTableSize - 2; i++) {
            for (int j = i + 1; j < cardsIdTableSize - 1; j++)
                for (int k = j + 1; k < cardsIdTableSize; k++) {
                    setCandidates.set(0, i);
                    setCandidates.set(1, j);
                    setCandidates.set(2, k);
                    if (checkSet(setCandidates)) {
                        firstSetOnPage.set(0,setCandidates.get(0));
                        firstSetOnPage.set(1,setCandidates.get(1));
                        firstSetOnPage.set(2,setCandidates.get(2));
                        setExisted = true;
                        //break;
                    }
                }
        }
        return setExisted;
    }

    protected void updateWholeTable(boolean setExisted) {
        if (!setExisted) {
            initializeTable(12);
            setCardsTable(12);
            mainActivity.notifyNewGame(12);
        }
    }

    @Override
    public void toggle(int index) {
        boolean isSelected = isCardSelected.get(index);
        if (!isSelected) {
            select(index);
            selectedCardsIndex.add(index);
            if (selectedCardsIndex.size() == 4) {
                unselect(selectedCardsIndex.get(0));
                selectedCardsIndex.remove(selectedCardsIndex.get(0));
            }
            if (selectedCardsIndex.size() == 3) {
                if (checkSet(selectedCardsIndex)) {
                    mainActivity.notifyDisableHint();
                    updateTable(selectedCardsIndex);
                    selectedCardsIndex.clear();
                }
            }
        } else {
            unselect(index);
            selectedCardsIndex.remove((Integer) index);
        }
    }

    @Override
    public void select(int index) {
        isCardSelected.set(index, true);
        mainActivity.notifySelect(index);
    }

    @Override
    public void unselect(int index) {
        isCardSelected.set(index, false);
        mainActivity.notifyUnselect(index);
    }

    @Override
    public void showSet() {
        hints += 1;
    }

    @Override
    public ArrayList<Integer> getCardsIdTable() {
        return cardsIdTable;
    }

    @Override
    public Boolean getIsCardSelected(int index) {
        return isCardSelected.get(index);
    }

    @Override
    public ArrayList<Integer> getSelectedCardsIndex() {
        return selectedCardsIndex;
    }

    @Override
    public ArrayList<Integer> getFirstSetOnPage() {
        return firstSetOnPage;
    }

    @Override
    public int getHints() {
        return hints;
    }
}
