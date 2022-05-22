package be.kuleuven.findaset.model;

import java.util.ArrayList;
import java.util.Random;

import be.kuleuven.findaset.activities.LearningActivity;
import be.kuleuven.findaset.activities.MainActivity;

public abstract class AbstractFindASet implements InterfaceFindASet {
    private ArrayList<Integer> cardsIdTable;
    protected int[] justForTest;
    private ArrayList<Integer> selectedCardsIndex;
    private ArrayList<Boolean> isCardSelected;
    protected ArrayList<Integer> foundedSetCardsIds;
    private ArrayList<Integer> remainingCardsIds;
    protected boolean win;

    protected MainActivity mainActivity;
    protected LearningActivity learningActivity;

    @Override
    public final void setUI(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public final void setUILearning(LearningActivity learningActivity){
        this.learningActivity = learningActivity;
    }

    public AbstractFindASet() {
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

    @Override
    public void startNewGame() {
        //TODO replace by file reading
        generateAllCardsIdsList();

        // init class fields
        this.win = false;
        this.justForTest = new int[3];
        this.selectedCardsIndex = new ArrayList<>(3);
        this.foundedSetCardsIds = new ArrayList<>();

        this.isCardSelected = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            this.isCardSelected.add(false);
        }

        initializeTable(12);
        setCardsTable(12);

        mainActivity.notifyNewGame(12);
    }

    @Override
    public void initializeTable(int sizeOfCards) {
        this.cardsIdTable = new ArrayList<>(12);
        for (int i = 0; i < sizeOfCards; i++) {
            cardsIdTable.add(9999);
        }
        this.justForTest = new int[3]; //JUST for TEST
    }

    //////////////////FEATURE MATRIX GENERATION/////////////////////////////////////////
    @Override
    public int[][] getFeatureMatrix() {
        Random rd = new Random();
        int[][] featureMatrix = new int[3][4];
        int[] isEqual = new int[4];

        // chooses which column will have equal features
        int sum = 0;
        isEqual = chooseEqualFeatures(rd, isEqual, sum);
        // add numbers based on the Features that should be equal
        addFeatureNumbers(rd, featureMatrix, isEqual);

        return featureMatrix;
    }

    private int[] chooseEqualFeatures(Random rd, int[] isEqual, int sum) {
        for (int i = 0; i < 4; i++) {
            isEqual[i] = rd.nextInt(2);
            sum += isEqual[i];
            if (sum == 4) {
                isEqual[i] = 0;
            }
        }
        return isEqual;
    }

    private void addFeatureNumbers(Random rd, int[][] featureMatrix, int[] isEqual) {
        for (int col = 0; col < 4; col++) {
            // choose feature that will be equal
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
    /////////////////////END OF FEATURE MATRIX GENERATION///////////////////////////////////////

    //////////////////SET GENERATION///////////////////////////////////////////////////////////
    private ArrayList<Integer> generateSet() {
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
    /////////////////////END OF SET GENERATION///////////////////////////////////////

    //////////////////SET CARDS TABLE (Find All)////////////////////////////////////
    @Override
    public void setCardsTable(int sizeOfCards) {
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
            justForTest[i] = randomIndex;
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
    //////////////////END OF SET CARDS TABLE////////////////////////////////////////////////



    //////////////////CHECK FOR SET////////////////////////////////////////////////
    @Override
    public boolean checkSet(ArrayList<Integer> testedCardsIndex) {
        int[][] featureMatrix = new int[3][4];
        // Create feature matrix of the three selected cards
        getTestFeatureMatrix(testedCardsIndex, featureMatrix);

        boolean isSet = true;
        isSet = checkFeatureMatrix(featureMatrix, isSet);
        return isSet;
    }

    private boolean checkFeatureMatrix(int[][] featureMatrix, boolean isSet) {
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
    //////////////////END OF CHECK FOR SET////////////////////////////////////////////////

    //////////////////Update Table////////////////////////////////////////////////
    @Override
    public void updateTable(ArrayList<Integer> selectedCardsIndex) {
        Random rd = new Random();

        for (int i = 0; i < 3; i++) {
            //1.
            int selectedIndex = selectedCardsIndex.get(i);

            unselect(selectedIndex);
            foundedSetCardsIds.add(cardsIdTable.get(selectedIndex));

            replace3Cards(rd, selectedIndex);
        }

        checkWin();

        if (win) {
            for (int l = 0; l < cardsIdTable.size(); l++) {
                mainActivity.notifyUnavailable(l);
            }
            mainActivity.notifyWin();
        }
    }

    private void replace3Cards(Random rd, int selectedIndex) {
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
    //////////////////End of Update Table////////////////////////////////////////////////

    protected boolean checkAllSetOnPage() {
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
                    mainActivity.setTestTxt("set Found");
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
    @Override
    public int[] getJustForTest() {
        return justForTest;
    }
}
