package be.kuleuven.findaset.model.card;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 */
public class AlternativeCard {
    private int elementId;
    private final int cardId;
    private int size;
    private ArrayList<Integer> cardFeatures;
    private boolean isSelected;


    /**
     * Construct by Element ID and Number of elements
     * Creates card object that has its features based on the feature codes:
     *
     * color    - green(1), red(2), purple(3)
     * shading  - open(1), striped(2), solid(3)
     * shape    - oval(1), diamond(2), squiggle(3)
     *
     * i.e. id = 123, corresponds to a green striped diamond.
     *
     * @param elementId - 3 digit integer to define the features of a card element.
     * @param size - 1 digit number of elements in each card.
     */
    public AlternativeCard(int elementId, int size) {
        this.isSelected = false;
        this.cardId = (int) (size * 10E3 + elementId);
        this.cardFeatures = new ArrayList<>();
        intToArray(elementId);
    }

    /**
     * Construct by Card feature ID
     * Creates card object that has its features based on the following codes:
     *
     * size     - one(1), two(2), three(3)
     * color    - green(1), red(2), purple(3)
     * shading  - open(1), striped(2), solid(3)
     * shape    - oval(1), diamond(2), squiggle(3)
     *
     * i.e. id = 3123, corresponds to a card with three green striped diamonds
     *
     * @param cardId - 4 digit integer to define the features of a card.
     */
    public AlternativeCard(int cardId) {
        this.isSelected = false;
        this.cardId = cardId;
        this.size = cardId / 1000;
        this.elementId = cardId % 1000;
        this.cardFeatures = new ArrayList<>();
        intToArray(this.elementId);
    }
    /**
     * Construct by Array of card Features
     * Creates card object that has its features based on the following codes:
     *
     * cardFeatures.get(0) size     - one(1),   two(2)     or three(3)
     * cardFeatures.get(1) color    - green(1), red(2)     or purple(3)
     * cardFeatures.get(2) shading  - open(1),  striped(2) or solid(3)
     * cardFeatures.get(3) shape    - oval(1),  diamond(2) or squiggle(3)
     *
     * i.e. id = 3123, corresponds to a card with three green striped diamonds
     *      cardFeatures = ArrayList<Integer> 3,1,2,3
     *
     * @param cardFeatures - 4 digit ArrayList<Integer> with features of a card.
     */
    public AlternativeCard(ArrayList<Integer> cardFeatures) {
        this.isSelected = false;
        this.cardFeatures = cardFeatures;
        this.cardId = (int) (cardFeatures.get(0) * 10E3 + cardFeatures.get(1) * 10E2 +
                                        cardFeatures.get(2) * 10E1 + cardFeatures.get(3));
        this.size = cardId / 1000;
        this.elementId = cardId % 1000;
    }

    /**
     *
     */
    public void toggle() {
        isSelected = !isSelected;
    }

    /**
     * @return cardId - 4 digit integer to define the features of a card.
     */
    public int getCardId() {
        return cardId;
    }

    /**
     * @return size - 1 digit number of elements in card.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return elementId - 3 digit integer to define the features of a card element.
     */
    public int getElementId() {
        return elementId;
    }

    /**
     * @return cardFeatures - 4 digit ArrayList<Integer> with features of a card.
     */
    public ArrayList<Integer> getCardFeatures() {
        return cardFeatures;
    }

    /**
     *
     */
    public boolean isSelected() {
        return isSelected;
    }

    // helper method from:
    // https://stackoverflow.com/questions/8033550/convert-an-integer-to-an-array-of-digits
    public void intToArray(int num){
        if( num != 0){
            int featureId = num %10;
            num /= 10;
            intToArray(num);
            cardFeatures.add(featureId);
        }
    }

}
