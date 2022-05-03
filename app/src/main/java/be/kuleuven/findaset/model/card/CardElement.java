package be.kuleuven.findaset.model.card;


import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * This class is not necessary and might be removed from the final
 * project later on.
 *
 * Elements objects are the unit drawings that are kept in
 * card objects. The features of an element are associated
 * to integers as explained below.
 *
 */
public class CardElement {
    private int color;
    private int shading;
    private int type;
    private final ArrayList<Integer> features;
    private final int id;

    /**
     * Creates an element object that will be used to compose a card
     *
     * @param color - green(1), red(2), purple(3)
     * @param shading - open(1), striped(2), solid(3)
     * @param type - oval(1), diamond(2), squiggle(3)
     */
    public CardElement(int color, int shading, int type) {
        this.color = color;
        this.shading = shading;
        this.type = type;

        this.features = new ArrayList<>();
        Collections.addAll(features, color, shading, type);
        this.id = color*100 + shading*10 + type;
    }

    /**
     * Creates an element object that will be used to compose a card
     *
     * Mind that ids are formed according to:
     *
     * green(1), red(2), purple(3)
     * open(1), striped(2), solid(3)
     * oval(1), diamond(2), squiggle(3)
     *
     * i.e. id = 123, corresponds to a green striped diamond
     *
     * @param id - 3 digit integer to define on of element
     *           the elements a card will hold
     */
    public CardElement(int id) {
        this.id = id;
        this.features = new ArrayList<>();
        intToArray(id);
        this.color = features.get(0);
        this.shading = features.get(1);
        this.type = features.get(2);
    }

    // helper method from:
    // https://stackoverflow.com/questions/8033550/convert-an-integer-to-an-array-of-digits
    public void intToArray(int num){
        if( num != 0){
            int featureId = num %10;
            num /= 10;
            intToArray(num);
            features.add(featureId);
        }
    }

    public int getColor() {
        return color;
    }

    public int getShading() {
        return shading;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getFeatures() {
        return features;
    }
}
