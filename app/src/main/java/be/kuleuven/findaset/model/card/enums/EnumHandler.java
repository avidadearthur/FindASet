package be.kuleuven.findaset.model.card.enums;

public class EnumHandler {
    /**
     * Handles the initialization of the enums.
     * I did not manage to initiate this enum by the index
     * 'SHAPECOUNT count = new SHAPECOUNT(1);' gives an error when called from FindASet.java
     */
    public EnumHandler() {
    }
    public SHAPECOUNT shapeCount(int index){
        if(index == 1){
            return SHAPECOUNT.one;
        }
        else if(index == 2){
            return SHAPECOUNT.two;
        }
        return SHAPECOUNT.three;
    }
    public SHADING shading(int index){
        if(index == 1){
            return SHADING.solid;
        }
        else if(index == 2){
            return SHADING.striped;
        }
        return SHADING.open;
    }
    public COLOR color(int index){
        if(index == 1){
            return COLOR.red;
        }
        else if(index == 2){
            return COLOR.green;
        }
        return COLOR.purple;
    }
    public TYPE type(int index){
        if(index == 1){
            return TYPE.diamond;
        }
        else if(index == 2){
            return TYPE.squiggle;
        }
        return TYPE.oval;
    }

}
