package be.kuleuven.findaset.model.card.enums;

public enum TYPE {
    oval(1), diamond(2), squiggle(3);

    private final int index;

    TYPE(final int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
}
