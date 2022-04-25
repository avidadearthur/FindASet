package be.kuleuven.findaset.model.card.enums;

public enum SHADING {
    solid(1), striped(2), open(3);

    private final int index;

    SHADING(final int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
}
