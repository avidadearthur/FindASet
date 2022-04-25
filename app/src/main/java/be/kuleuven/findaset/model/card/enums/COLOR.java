package be.kuleuven.findaset.model.card.enums;

public enum COLOR {
    red(1), green(2), purple(3);

    private final int index;

    COLOR(final int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

}
