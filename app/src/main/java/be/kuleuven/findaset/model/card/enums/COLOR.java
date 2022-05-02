package be.kuleuven.findaset.model.card.enums;

public enum COLOR {
    green(1), red(2), purple(3);

    private final int index;

    COLOR(final int index){
        this.index = index;
    }

    public int getIndex(){
        return index;
    }

}
