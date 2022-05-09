package be.kuleuven.findaset.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import be.kuleuven.findaset.activities.MainActivity;
import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.model.card.AlternativeCard;
import be.kuleuven.findaset.model.card.Card;
import be.kuleuven.findaset.model.card.enums.EnumHandler;

public class FindASet extends AbstractFindASet{
    private ArrayList<AlternativeCard> cardsTable;
    private ArrayList<Integer> cardsIdTable;
    private int[] justForTest;
    private ArrayList<Integer> selectedCardsIndex;
    private ArrayList<Boolean> isCardSelected;
    private ArrayList<Integer> foundedSetCardsIds;

    public FindASet() {
    }

    /**
     *
     */
    public void startNewGame(){
        // init class fields
        this.justForTest = new int[3];
        this.cardsTable = new ArrayList<>(12);
        this.cardsIdTable = new ArrayList<>(12);
        this.selectedCardsIndex = new ArrayList<>(3);
        this.foundedSetCardsIds= new ArrayList<>();
        this.isCardSelected = new ArrayList<>();
        for (int i = 0; i < 12; i++) { this.isCardSelected.add(false); }
        alternativeSetCardsTable(cardsTable,cardsIdTable);
        mainActivity.notifyNewGame();
    }

    /**
     * Generates matrix that maps to a correct set of three cards
     * when the game is initialized.
     *
     * Game rules:
     * 0 the same + 4 different; or 1 the same + 3 different;
     * or 2 the same + 2 different; or 3 the same + 1 different.
     *
     * The featureMatrix randomly selects the features that
     * form a set among three cards.
     * featureMatrix example:
     *
     *              nr.     color    shading   type
     *
     *  card n1.    1       3        2         1
     *
     *  card n2.    1       1        2         3
     *
     *  card n3.    1       2        2         2
     *  --------------------------------------------
     *  bool        1       0        1         0
     *
     * Based on the classification:
     * one(1),two(2),three(3)
     * green(1), red(2), purple(3)
     * open(1), striped(2), solid(3)
     * oval(1), diamond(2), squiggle(3)
     *
     *  So the example above would pick a set of
     *  one purple striped oval
     *  one green striped squiggle
     *  one red striped diamond
     *
     * @return AbstractCard array of size 3
     */
    @Override
    public int[][] getFeatureMatrix(){
        Random rd = new Random();
        int[][] featureMatrix = new int[3][4];
        int[] isEqual = new int[4];

        // generate bool array
        int sum = 0;
        for (int i = 0; i <4; i++) {
            isEqual[i] = rd.nextInt(2);
            sum += isEqual[i];
            if(sum==4){isEqual[i] = 0;}
        }

        for (int col = 0; col<4; col++){
            if(isEqual[col] == 1) {
                // choose feature that will be equal
                int value = rd.nextInt(3) + 1;
                for(int row = 0; row<3; row++){
                    featureMatrix[row][col] = value;
                }
            }
            else {
                ArrayList<Integer> diffFeatures = new ArrayList<>(3);
                int i = 0;
                while (i < 3){
                    int next = rd.nextInt(3) + 1;
                    if (!diffFeatures.contains(next)){
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
     *
     * Returns a set of three AlternativeCard objects by:
     *      1.Invoking a featureMatrix
     *      2.Converting the featureMatrix card arrays into AlternativeCard objects
     *      3.Finally adding them to ArrayList<AlternativeCard> set
     *
     * @return set - ArrayList<AlternativeCard> of size 3
     */
    private ArrayList<AlternativeCard> alternativeGenerateSet(){
        boolean setFoundedAlready = false;
        ArrayList<AlternativeCard> set = new ArrayList<>();

        do {
            int[][] setFeatureMatrix = getFeatureMatrix();

            for (int[] features : setFeatureMatrix) {
                int cardId = features[0] * 1000 + features[1] * 100 + features[2] * 10 + features[3];
                if (foundedSetCardsIds.contains(cardId)) {
                    setFoundedAlready = true;
                    set.clear();
                    break;
                }
                else {
                    AlternativeCard newCard = new AlternativeCard(cardId);
                    set.add(newCard);
                }
            }
        }
        while (setFoundedAlready);

        return set;
    }

    /**
     * Alternative setting table method that uses the AlternativeCard objects
     *
     * Populates ArrayList<AlternativeCard> cardsTable by:
     *      0. Populate table with dummy cards with ID 9999 (inside emptyTable())
     *      1. Choosing 3 random indexes to place the three cards previously generated
     *         from alternativeGenerateSet().
     *      2. Populates the array with unique randomly generated cards to be placed in the empty
     *         positions.
     */
    public void alternativeSetCardsTable(ArrayList<AlternativeCard> cardsTable,
                                         ArrayList<Integer> cardsIdTable)
    {
        ArrayList<AlternativeCard> set = alternativeGenerateSet();
        // Step 0
        for(int i = 0; i < 12; i++){
            AlternativeCard newCard = new AlternativeCard(9999);
            cardsTable.add(newCard);
            cardsIdTable.add(9999);
        }
        // Step 1
        Random rd = new Random();
        for(int i = 0; i < set.size(); i++){
            int randomIndex;
            do {
                randomIndex = rd.nextInt(11);
            }
            while (cardsTable.get(randomIndex).getCardId() != 9999);
            cardsTable.set(randomIndex,set.get(i));
            cardsIdTable.set(randomIndex,set.get(i).getCardId());
            justForTest[i] = randomIndex;
        }
        // Step 2
        while(cardsIdTable.contains(9999)){
            int nr = rd.nextInt(3) + 1;
            int color = rd.nextInt(3) + 1;
            int shading = rd.nextInt(3) + 1;
            int type = rd.nextInt(3) + 1;

            int newCardId = nr*1000 + color*100 + shading*10 + type;
            AlternativeCard newCard = new AlternativeCard(newCardId);

            if(!cardsIdTable.contains(newCardId) && !foundedSetCardsIds.contains(newCardId)){
                // Very inefficient algo to add cards in the available spots
                int randomIndex;
                do{
                    randomIndex = rd.nextInt(12);
                }
                while (cardsIdTable.get(randomIndex) != 9999);
                cardsTable.set(randomIndex,newCard);
                cardsIdTable.set(randomIndex,newCardId);
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
            AlternativeCard nextCard = AlternativeGetCard(testedCardsIndex.get(i));
            featureMatrix[i][0] = nextCard.getSize();
            featureMatrix[i][1] = nextCard.getCardFeatures().get(0);
            featureMatrix[i][2] = nextCard.getCardFeatures().get(1);
            featureMatrix[i][3] = nextCard.getCardFeatures().get(2);
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
     *      1. Update foundedSetCardsIds and Toggle the set cards
     *      2.
     *      3.
     *
     * ArrayList<Integer> of indexes (0 - 11) of selected cards
     */
    @Override
    public void alternativeUpdateTable(ArrayList<Integer> selectedCardsIndex) {
        Random rd = new Random();
        // 1.
        for (int i = 0; i < 3; i++) {
            int selectedIndex = selectedCardsIndex.get(i);
            //Error: toggle(selectedIndex);
            unselect(selectedIndex);

            foundedSetCardsIds.add(cardsIdTable.get(selectedIndex));
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
            AlternativeCard newCard = new AlternativeCard(newCardId);
            cardsTable.set(selectedIndex, newCard);
            cardsIdTable.set(selectedIndex, newCardId);
            // 4.
            mainActivity.notifyCard(selectedIndex);
        }
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
                for (int k = j + 1; k < cardsIdTableSize; k++){
                    setCandidates.set(0,i);
                    setCandidates.set(1,j);
                    setCandidates.set(2,k);
                    if(checkSet(setCandidates)){
                        Log.d("SetCandidate", "alternativeUpdateTable: " + setCandidates);
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
        String testMessage = "Set in this screen: \n";
        for(int i : testSetMessage) {
            int j = i+1;
            testMessage = testMessage + j + " ";
        }
        mainActivity.setTestTxt(testMessage);
        // If no sets found recreate table
        if (!setExisted) {
            emptyTable();
            alternativeSetCardsTable(cardsTable,cardsIdTable);
        }
    }

    /**
     * Generate two new arrays to empty existed arrays.
     *
     * Also empty foundedSetCardsFeatures.
     */
    @Override
    public void emptyTable() {
        cardsTable = new ArrayList<>(12);
        cardsIdTable = new ArrayList<>(12);
        justForTest = new int[3]; //JUST for TEST
    }

    public AlternativeCard AlternativeGetCard(int i) {
        return cardsTable.get(i);
    }

    @Override
    public void toggle(int index) {
        boolean isSelected = isCardSelected.get(index);
        if (!isSelected) {
            select(index);
            selectedCardsIndex.add(index);
            if(selectedCardsIndex.size()==4) {
                unselect(selectedCardsIndex.get(0));
                selectedCardsIndex.remove(selectedCardsIndex.get(0));
            }
            if(selectedCardsIndex.size()==3) {
                if(checkSet(selectedCardsIndex)) {
                    mainActivity.setTestTxt("set Found");
                    alternativeUpdateTable(selectedCardsIndex);
                    selectedCardsIndex.clear();
                }
            }
        }
        else {
            unselect(index);
            selectedCardsIndex.remove(selectedCardsIndex.indexOf(index));
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
