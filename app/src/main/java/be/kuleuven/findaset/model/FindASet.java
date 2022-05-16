package be.kuleuven.findaset.model;

import java.util.ArrayList;
import java.util.Random;

public class FindASet extends AbstractFindASet {
    private ArrayList<Integer> cardsIdTable;
    private int[] justForTest;
    private ArrayList<Integer> selectedCardsIndex;
    private ArrayList<Boolean> isCardSelected;
    private ArrayList<Integer> foundedSetCardsIds;
    private ArrayList<Integer> remainingCardsIds;
    private boolean win;

    public FindASet() {
    }

    private void generateAllCardsIdsList() {
        remainingCardsIds = new ArrayList<>(81);
        for (int size = 1; size < 4; size++) {
            for (int color = 1; color < 4; color++) {
                for (int shading = 1; shading < 4; shading++) {
                    for (int type = 1; type < 4; type++) {
                        remainingCardsIds.add(size * 1000 + color * 100 + shading * 10 + type);
                    }
                }
            }
        }
    }

    /**
     *
     */
    public void startNewGame() {
        generateAllCardsIdsList();
        // init class fields
        this.justForTest = new int[3];
        this.selectedCardsIndex = new ArrayList<>(3);
        this.foundedSetCardsIds = new ArrayList<>();
        this.isCardSelected = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            this.isCardSelected.add(false);
        }
        initializeTable(12);
        alternativeSetCardsTable(12);
        mainActivity.notifyNewGame(12);
        win = false;
    }


    public void initializeTable(int sizeOfCards) {
        this.cardsIdTable = new ArrayList<>(12);
        for (int i = 0; i < sizeOfCards; i++) {
            cardsIdTable.add(9999);
        }
        this.justForTest = new int[3]; //JUST for TEST
    }

    /**
     * Generates matrix that maps to a correct set of three cards
     * when the game is initialized.
     * <p>
     * Game rules:
     * 0 the same + 4 different; or 1 the same + 3 different;
     * or 2 the same + 2 different; or 3 the same + 1 different.
     * <p>
     * The featureMatrix randomly selects the features that
     * form a set among three cards.
     * featureMatrix example:
     * <p>
     * nr.     color    shading   type
     * <p>
     * card n1.    1       3        2         1
     * <p>
     * card n2.    1       1        2         3
     * <p>
     * card n3.    1       2        2         2
     * --------------------------------------------
     * bool        1       0        1         0
     * <p>
     * Based on the classification:
     * one(1),two(2),three(3)
     * green(1), red(2), purple(3)
     * open(1), striped(2), solid(3)
     * oval(1), diamond(2), squiggle(3)
     * <p>
     * So the example above would pick a set of
     * one purple striped oval
     * one green striped squiggle
     * one red striped diamond
     *
     * @return AbstractCard array of size 3
     */
    @Override
    public int[][] getFeatureMatrix() {
        Random rd = new Random();
        int[][] featureMatrix = new int[3][4];
        int[] isEqual = new int[4];

        // generate bool array
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            isEqual[i] = rd.nextInt(2);
            sum += isEqual[i];
            if (sum == 4) {
                isEqual[i] = 0;
            }
        }

        for (int col = 0; col < 4; col++) {
            if (isEqual[col] == 1) {
                // choose feature that will be equal
                int value = rd.nextInt(3) + 1;
                for (int row = 0; row < 3; row++) {
                    featureMatrix[row][col] = value;
                }
            } else {
                ArrayList<Integer> diffFeatures = new ArrayList<>(3);
                int i = 0;
                while (i < 3) {
                    int next = rd.nextInt(3) + 1;
                    if (!diffFeatures.contains(next)) {
                        diffFeatures.add(next);
                        i++;
                    }
                }
                for (int row = 0; row < 3; row++) {
                    featureMatrix[row][col] = diffFeatures.get(row);
                }
            }
        }

        return featureMatrix;
    }

    /**
     * Alternative implementation of generate set that is compatible with alternativeSetCardsTable.
     * <p>
     * Returns a set of three AlternativeCard objects by:
     * 1.Invoking a featureMatrix
     * 2.Converting the featureMatrix card arrays into AlternativeCard objects
     * 3.Finally adding them to ArrayList<AlternativeCard> set
     *
     * @return set - ArrayList<AlternativeCard> of size 3
     */
    private ArrayList<Integer> alternativeGenerateSet() {
        boolean setFoundedAlready;
        ArrayList<Integer> set = new ArrayList<>();

        do {
            set.clear();
            setFoundedAlready = false;
            int[][] setFeatureMatrix = getFeatureMatrix();
            for (int[] features : setFeatureMatrix) {
                int cardId = features[0] * 1000 + features[1] * 100 + features[2] * 10 + features[3];
                if (foundedSetCardsIds.contains(cardId)) {
                    setFoundedAlready = true;
                    break;
                } else {
                    set.add(cardId);
                }
            }
        }
        while (setFoundedAlready);

        return set;
    }

    /**
     * Alternative setting table method that uses the AlternativeCard objects
     * <p>
     * Populates ArrayList<AlternativeCard> cardsTable by:
     * 0. Populate table with dummy cards with ID 9999 (inside initilizeTable())
     * 1. Choosing 3 random indexes to place the three cards previously generated
     * from alternativeGenerateSet().
     * 2. Populates the array with unique randomly generated cards to be placed in the empty
     * positions.
     */
    public void alternativeSetCardsTable(int sizeOfCards) {
        Random rd = new Random();
        // Step 0
        if (foundedSetCardsIds.size() <= 69) {
            ArrayList<Integer> set = alternativeGenerateSet();
            // Step 1
            for (int i = 0; i < set.size(); i++) {
                int randomIndex;
                do {
                    randomIndex = rd.nextInt(12);
                }
                while (cardsIdTable.get(randomIndex) != 9999);
                cardsIdTable.set(randomIndex, set.get(i));
                justForTest[i] = randomIndex;
            }
            // Step 2
            /*
            while (cardsIdTable.contains(9999)) {
                int nr = rd.nextInt(3) + 1;
                int color = rd.nextInt(3) + 1;
                int shading = rd.nextInt(3) + 1;
                int type = rd.nextInt(3) + 1;
                int newCardId = nr * 1000 + color * 100 + shading * 10 + type;
                if (!cardsIdTable.contains(newCardId) && !foundedSetCardsIds.contains(newCardId)) {
                    // Very inefficient algo to add cards in the available spots
                    int randomIndex;
                    do {
                        randomIndex = rd.nextInt(12);
                    }
                    while (cardsIdTable.get(randomIndex) != 9999);
                    cardsIdTable.set(randomIndex, newCardId);
                }
            }

             */


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
        else {
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
    }

    /**
     * Store features of all cards into 4*3 featureMatrix.
     * For every column, check if is all same numbers or the sum equals to 6.
     *
     * @return If there is set, true will be returned.
     */
    public boolean checkSet(ArrayList<Integer> testedCardsIndex) {
        int[][] featureMatrix = new int[3][4];
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

        boolean isSet = true;
        for (int col = 0; col < 4; col++) {
            if (featureMatrix[0][col] == featureMatrix[1][col]) {
                if (featureMatrix[1][col] != featureMatrix[2][col]) {
                    isSet = false;
                    break;
                }
            } else if (featureMatrix[0][col] + featureMatrix[1][col] + featureMatrix[2][col] != 6) {
                isSet = false;
                break;
            }
        }
        return isSet;
    }

    /**
     * Updates table when set is found by:
     * 1. Update foundedSetCardsIds and Toggle the set cards
     * 2.
     * 3.
     * <p>
     * ArrayList<Integer> of indexes (0 - 11) of selected cards
     */
    @Override
    public void alternativeUpdateTable(ArrayList<Integer> selectedCardsIndex) {
        Random rd = new Random();
        for (int i = 0; i < 3; i++) {
            //1.
            int selectedIndex = selectedCardsIndex.get(i);
            unselect(selectedIndex);
            foundedSetCardsIds.add(cardsIdTable.get(selectedIndex));

            if (foundedSetCardsIds.size() <= 69) {
                int newCardId;
                //2.
                do {
                    int nr = rd.nextInt(3) + 1;
                    int color = rd.nextInt(3) + 1;
                    int shading = rd.nextInt(3) + 1;
                    int type = rd.nextInt(3) + 1;

                    newCardId = nr * 1000 + color * 100 + shading * 10 + type;
                }
                while (cardsIdTable.contains(newCardId) || foundedSetCardsIds.contains(newCardId));
                //3.
                cardsIdTable.set(selectedIndex, newCardId);
                // 4.
                mainActivity.notifyCard(selectedIndex);
            }
        }
        if (foundedSetCardsIds.size() <= 69) {
            ifNoSet(checkAllSetOnPage());
        } else if (foundedSetCardsIds.size() == 72) {
            for (int j = 9; j < 12; j++) {
                mainActivity.notifyUnavailable(j);
            }
            initializeTable(9);
            justForTest = new int[3];
            alternativeSetCardsTable(9);
            mainActivity.notifyNewGame(9);
            if (!checkAllSetOnPage()) {
                win = true;
            }
        } else if (foundedSetCardsIds.size() == 75) {
            for (int k = 6; k < 9; k++) {
                mainActivity.notifyUnavailable(k);
            }
            initializeTable(6);
            justForTest = new int[3];
            alternativeSetCardsTable(6);
            mainActivity.notifyNewGame(6);
            if (!checkAllSetOnPage()) {
                win = true;
            }
        }
        if (win) {
            for (int l = 0; l < cardsIdTable.size(); l++) {
                mainActivity.notifyUnavailable(l);
            }
            mainActivity.setTestTxt("YOU WIN!!!");
        }
    }

    private boolean checkAllSetOnPage() {
        //Loop over all the possible card combinations
        ArrayList<Integer> setCandidates = new ArrayList<>(3);
        setCandidates.add(99);
        setCandidates.add(99);
        setCandidates.add(99);
        ArrayList<Integer> testSetMessage = new ArrayList<>(); // JUST for test
        int cardsIdTableSize = cardsIdTable.size();
        boolean setExisted = false;
        for (int i = 0; i < cardsIdTableSize - 2; i++) {
            for (int j = i + 1; j < cardsIdTableSize - 1; j++)
                for (int k = j + 1; k < cardsIdTableSize; k++) {
                    setCandidates.set(0, i);
                    setCandidates.set(1, j);
                    setCandidates.set(2, k);
                    if (checkSet(setCandidates)) {
                        //JUST for test
                        testSetMessage.add(setCandidates.get(0));
                        testSetMessage.add(setCandidates.get(1));
                        testSetMessage.add(setCandidates.get(2));
                        setExisted = true;
                        //break;
                    }
                }
        }
        //JUST for test
        StringBuilder testMessage = new StringBuilder("Set in this screen: \n");
        for (int i : testSetMessage) {
            int j = i + 1;
            testMessage.append(j).append(" ");
        }
        testMessage.append("\n Founded").append(foundedSetCardsIds.size());
        mainActivity.setTestTxt(testMessage.toString());
        // Return if there are sets existed in the screen
        return setExisted;
    }

    private void ifNoSet(boolean setExisted) {
        if (!setExisted) {
            initializeTable(12);
            alternativeSetCardsTable(12);
            mainActivity.notifyNewGame(12);
        }
    }

    /**
     * Generate two new arrays to empty existed arrays.
     * <p>
     * Also empty foundedSetCardsFeatures.
     */

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
                    mainActivity.setTestTxt("set Found");
                    alternativeUpdateTable(selectedCardsIndex);
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

    /**
     * Used for testing updateTable method
     */
    @Override
    public ArrayList<Integer> getCardsIdTable() {
        return cardsIdTable;
    }

    @Override
    public ArrayList<Boolean> getIsCardSelected() {
        return isCardSelected;
    }

    @Override
    public ArrayList<Integer> getSelectedCardsIndex() {
        return selectedCardsIndex;
    }

    //JUST for TEST
    public int[] getJustForTest() {
        return justForTest;
    }
}
