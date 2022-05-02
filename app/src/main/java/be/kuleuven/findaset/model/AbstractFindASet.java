package be.kuleuven.findaset.model;

import java.util.ArrayList;

import be.kuleuven.findaset.activities.MainActivity;
import be.kuleuven.findaset.model.card.AbstractCard;

public abstract class AbstractFindASet implements TestableFindASet {
    protected MainActivity mainActivity;

    public final void setUI(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public abstract int[][] getFeatureMatrix();

    public abstract AbstractCard[] generateSet(int[][] featureMatrix);

    public abstract int[] generateSetFeature(int[][] featureMatrix);

    public abstract void setTable(AbstractCard[] table);

    public abstract void updateTable(ArrayList<Integer> cardIndexes);

    public abstract void emptyTable();

    public abstract AbstractCard getCard(int i);

    public abstract void toggle(int i);
}
