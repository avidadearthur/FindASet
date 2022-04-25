package be.kuleuven.findaset.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import be.kuleuven.findaset.model.card.AbstractCard;

public class FindASet extends AbstractFindASet{
    private AbstractCard[] cards;

    public FindASet() {
        this.cards = new AbstractCard[12];
        setTable(cards);
    }

    /**
     * Generates a correct set of three cards when the game is initialized.
     *
     * Game rules:
     *
     * 0 the same + 4 different; or 1 the same + 3 different;
     * or 2 the same + 2 different; or 3 the same + 1 different.
     *
     * The featureMatrix randomly selects the features that
     * form a set among three cards.
     *
     * featureMatrix example:
     *
     *              nr.     type    color   shading
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
     *  diamond(1), squiggle(2), oval(3)
     *  red(1), green(2), purple(3)
     *  solid(1), striped(2), open(3)
     *
     *  So the example above would pick a set of
     *  (...)
     *
     * @return AbstractCard array of size 3
     */
    @Override
    public AbstractCard[] generateSet(){

        AbstractCard[] set = new AbstractCard[3];

        Random rd = new Random();
        int[][] featureMatrix = new int[3][4];
        int[] isEqual = new int[4];

        // generate bool array
        int sum = 0;
        for (int i = 0; i <4; i++) {
            isEqual[i] = rd.nextInt(2);
            sum += isEqual[i];
            if(sum==4){i = 0;}
        }

        for (int col = 0; col<4; col++){
            if(isEqual[col] == 1) {
                // choose feature that will be equal
                int value = rd.nextInt(3) + 1;
                for(int row = 0; row<2; row++){
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
                for (int row = 0; row < 2; row++) {
                    featureMatrix[row][col] = diffFeatures.get(row);
                }
            }
        }

        return set;
    }

    /**
     * Populates AbstractCard[] cards.
     *
     * @param table - empty 12 element array of cards.
     */
    @Override
    public void setTable(AbstractCard[] table) {
        AbstractCard[] set = generateSet();
    }

    @Override
    public void updateTable(int[] cardIndexes) {

    }

    @Override
    public AbstractCard getCard(int i) {
        return null;
    }

    @Override
    public void select(int i) {

    }

    @Override
    public void unselect(int i) {

    }
}
