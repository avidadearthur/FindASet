package be.kuleuven.findaset.model.card.enums;

public enum TYPE {
    diamond(1), squiggle(2), oval(3);

    private final int index;

    TYPE(final int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }
}
