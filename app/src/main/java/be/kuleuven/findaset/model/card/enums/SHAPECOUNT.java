package be.kuleuven.findaset.model.card.enums;

public enum SHAPECOUNT {
    one(1),two(2),three(3);

    private final int index;

    SHAPECOUNT(final int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
}
