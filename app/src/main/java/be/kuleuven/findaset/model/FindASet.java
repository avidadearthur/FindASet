package be.kuleuven.findaset.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import be.kuleuven.findaset.model.card.AbstractCard;
import be.kuleuven.findaset.model.card.Card;
import be.kuleuven.findaset.model.card.enums.EnumHandler;

public class FindASet extends AbstractFindASet{
    private AbstractCard[] cards;
    private int[] cardFeatures;
    private int[] justForTest;

    public FindASet() {
        this.cards = new Card[12];
        this.cardFeatures = new int[12];
        this.justForTest = new int[3];
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
     *  Based on the classification:
     *  one(1),two(2),three(3)
     *  red(1), green(2), purple(3)
     *  solid(1), striped(2), open(3)
     *  diamond(1), squiggle(2), oval(3)
     *
     *  So the example above would pick a set of
     *  one purple striped solid
     *  one red striped oval
     *  one green striped squiggle
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
            if(sum==4){isEqual[i] = 0;}                                 //error: i = 0;
        }

        for (int col = 0; col<4; col++){
            if(isEqual[col] == 1) {
                // choose feature that will be equal
                int value = rd.nextInt(3) + 1;
                for(int row = 0; row<3; row++){                         //error: row<2;
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
                for (int row = 0; row < 3; row++) {                     //error: row<2;
                    featureMatrix[row][col] = diffFeatures.get(row);
                }
            }
        }

        return featureMatrix;
    }

    // I split the methods here mainly for testing purposes
    /**
     * Generates a set of abstract cards based on a feature matrix
     * passed as an argument.
     *
     * @param featureMatrix - two dimensional array of ints that
     *                      represent the enum options.
     *
     * @return AbstractCard array of size 3
     */
    @Override
    public AbstractCard[] generateSet(int[][] featureMatrix){
        AbstractCard[] set = new AbstractCard[3];

        EnumHandler handler = new EnumHandler();
        int i = 0;
        for (int[] card: featureMatrix) {
            AbstractCard newCard = new Card(
                    handler.shapeCount(card[0]),
                    handler.shading(card[1]),
                    handler.color(card[2]),
                    handler.type(card[3]));
            set[i] = newCard;
            i++;
        }

        return set;
    }



    /**
     * Generates an feature ID for each set card according to matrix.
     * This array will be easier to check if there are the same cards,
     * as well as the check of if there is the set.
     *
     * Example:
     *             nr.     color    shading   type
     * card n1.    1       3        2         1
     *
     * Feature ID of card n1. is 1321
     *
     * @param featureMatrix - two dimensional array of ints that
     *                      represent the enum options.
     *
     * @return int array of size 3
     */
    @Override
    public int[] generateSetFeature(int[][] featureMatrix){
        int[] setFeatures = new int[3];

        int i = 0;
        for (int[] card: featureMatrix) {
            int newCardFeature = card[0]*1000 + card[1]*100 + card[2]*10 + card[3];
            setFeatures[i] = newCardFeature;
            i++;
        }

        return setFeatures;
    }

    /**
     * Populates AbstractCard[] cards.
     *
     * First randomly choose 3 indexes and then copy the cards
     * returned by generateSet().
     *
     * Then generate one 4-dit feature ID for each empty cards.
     * Usage of "int[] cardFeatures" can avoid multiple
     * occurrences of one card.
     *
     * Finally call method in MainActivity to display all cards.
     *
     * @param table - empty 12 element array of cards.
     */
    @Override
    public void setTable(AbstractCard[] table) {
        int[][] setFeatureMatrix = getFeatureMatrix();
        AbstractCard[] set = generateSet(setFeatureMatrix);
        int[] setFeatures = generateSetFeature(setFeatureMatrix);

        EnumHandler handler = new EnumHandler();
        Random rd = new Random();
        int i = 0;
        while (i < 3){
            int next = rd.nextInt(cards.length);
            if(cardFeatures[next] == 0){
                cards[next] = set[i];
                cardFeatures[next] = setFeatures[i];
                justForTest[i] = next; //JUST for TEST
                i++;
            }
        }
        for (int j = 0; j < cards.length; j++) {
            if(cardFeatures[j] == 0){
                boolean contain = true;
                while (contain){
                    int ID0 = rd.nextInt(3);
                    int ID1 = rd.nextInt(3);
                    int ID2 = rd.nextInt(3);
                    int ID3 = rd.nextInt(3);
                    int ID = ID0*1000 + ID1*100 + ID2*10 + ID3;
                    contain = arrayContainsValue(cardFeatures, ID);
                    if(!contain){
                        cards[j] = new Card(
                                handler.shapeCount(ID0),
                                handler.shading(ID1),
                                handler.color(ID2),
                                handler.type(ID3));
                        cardFeatures[j] = ID;
                    }
                }
            }
        }
        mainActivity.notifyNewGame();
    }

    private boolean arrayContainsValue(int[] array, int targetValue) {
        for (int next: array) {
            if (next == targetValue)
                return true;
        }
        return false;
    }

    @Override
    public void updateTable(int[] cardIndexes) {

    }

    /**
     * Generate two new arrays to empty existed arrays.
     */
    @Override
    public void emptyTable() {
        cards = new AbstractCard[12];
        cardFeatures = new int[12];
        justForTest = new int[3]; //JUST for TEST
    }

    @Override
    public AbstractCard getCard(int i) {
        return cards[i];
    }

    @Override
    public void select(int i) {

    }

    @Override
    public void unselect(int i) {

    }

    //JUST for TEST
    public int[] getJustForTest() {
        return justForTest;
    }
}
