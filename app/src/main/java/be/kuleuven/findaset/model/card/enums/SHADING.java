package be.kuleuven.findaset.model.card.enums;

public enum SHADING {
    open(1), striped(2), solid(3);

    private final int index;

    SHADING(final int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
}
