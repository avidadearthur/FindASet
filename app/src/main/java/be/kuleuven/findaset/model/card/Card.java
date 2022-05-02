package be.kuleuven.findaset.model.card;

import be.kuleuven.findaset.model.card.enums.EnumHandler;
import be.kuleuven.findaset.model.card.enums.SHAPECOUNT;
import be.kuleuven.findaset.model.card.enums.COLOR;
import be.kuleuven.findaset.model.card.enums.SHADING;
import be.kuleuven.findaset.model.card.enums.TYPE;

public class Card extends AbstractCard{
    private Enum<SHAPECOUNT> shapeCountEnum;
    private Enum<SHADING> shadingEnum;
    private Enum<COLOR> colorEnum;
    private Enum<TYPE> typeEnum;
    private int shapeCountInt;
    private int shadingInt;
    private int colorInt;
    private int typeInt;
    private boolean isSelected;

    public Card(Enum<SHAPECOUNT> shapeCountEnum, Enum<SHADING> shadingEnum, Enum<COLOR> colorEnum, Enum<TYPE> typeEnum) {
        this.shapeCountEnum = shapeCountEnum;
        this.shadingEnum = shadingEnum;
        this.colorEnum = colorEnum;
        this.typeEnum = typeEnum;
        this.shapeCountInt = ((SHAPECOUNT) shapeCountEnum).getIndex();
        this.shadingInt = ((SHADING) shadingEnum).getIndex();
        this.colorInt = ((COLOR) colorEnum).getIndex();
        this.typeInt = ((TYPE) typeEnum).getIndex();

        isSelected = false;
    }

    @Override
    public void toggle() {
        isSelected = !isSelected;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public Enum<COLOR> getColor() {
        return colorEnum;
    }

    @Override
    public Enum<SHADING> getShading() {
        return shadingEnum;
    }

    @Override
    public Enum<TYPE> getType() {
        return typeEnum;
    }

    @Override
    public Enum<SHAPECOUNT> getCount() {
        return shapeCountEnum;
    }

    @Override
    public String toString() {
        SHAPECOUNT count = (SHAPECOUNT) shapeCountEnum;
        SHADING shading = (SHADING) shadingEnum;
        COLOR color = (COLOR) colorEnum;
        TYPE type = (TYPE) typeEnum;
        int ID = count.getIndex()*1000+shading.getIndex()*100+color.getIndex()*10+type.getIndex();
        return  "count=" + shapeCountEnum + "\n" +
                "shading=" + shadingEnum + "\n" +
                "color=" + colorEnum + "\n" +
                "type=" + typeEnum + "\n" + ID;
    }

    @Override
    public int getShapeCountInt() {
        return shapeCountInt;
    }

    @Override
    public int getShadingInt() {
        return shadingInt;
    }

    @Override
    public int getColorInt() {
        return colorInt;
    }

    @Override
    public int getTypeInt() {
        return typeInt;
    }
}
