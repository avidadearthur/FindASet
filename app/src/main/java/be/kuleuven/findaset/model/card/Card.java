package be.kuleuven.findaset.model.card;

import be.kuleuven.findaset.model.card.enums.ShapeCount;
import be.kuleuven.findaset.model.card.enums.Color;
import be.kuleuven.findaset.model.card.enums.Shading;
import be.kuleuven.findaset.model.card.enums.Type;

public class Card extends AbstractCard{
    private Enum<ShapeCount> shapeCountEnum;
    private Enum<Shading> shadingEnum;
    private Enum<Color> colorEnum;
    private Enum<Type> typeEnum;

    public Card(Enum<ShapeCount> shapeCountEnum, Enum<Shading> shadingEnum, Enum<Color> colorEnum, Enum<Type> typeEnum) {
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
    public Enum<Color> getColor() {
        return colorEnum;
    }

    @Override
    public Enum<Shading> getShading() {
        return shadingEnum;
    }

    @Override
    public Enum<Type> getType() {
        return typeEnum;
    }

    @Override
    public Enum<ShapeCount> getCount() {
        return shapeCountEnum;
    }
}
