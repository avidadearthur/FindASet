package be.kuleuven.findaset.model.card;

import be.kuleuven.findaset.model.card.enums.SHAPECOUNT;
import be.kuleuven.findaset.model.card.enums.COLOR;
import be.kuleuven.findaset.model.card.enums.SHADING;
import be.kuleuven.findaset.model.card.enums.TYPE;

public class Card extends AbstractCard{
    private Enum<SHAPECOUNT> shapeCountEnum;
    private Enum<SHADING> shadingEnum;
    private Enum<COLOR> colorEnum;
    private Enum<TYPE> typeEnum;

    public Card(Enum<SHAPECOUNT> shapeCountEnum, Enum<SHADING> shadingEnum, Enum<COLOR> colorEnum, Enum<TYPE> typeEnum) {
        this.shapeCountEnum = shapeCountEnum;
        this.shadingEnum = shadingEnum;
        this.colorEnum = colorEnum;
        this.typeEnum = typeEnum;
    }

    @Override
    public void toggle() {
    }

    @Override
    public boolean isSelected() {
        return false;
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
}
