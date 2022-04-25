package be.kuleuven.findaset.model.card;

import be.kuleuven.findaset.model.card.enums.ShapeCount;
import be.kuleuven.findaset.model.card.enums.Color;
import be.kuleuven.findaset.model.card.enums.Shading;
import be.kuleuven.findaset.model.card.enums.Type;
import be.kuleuven.findaset.test.TestableCard;

public abstract class AbstractCard implements TestableCard{
    public abstract void toggle();
    public abstract boolean isSelected();
    public abstract Enum<Color> getColor();
    public abstract Enum<Shading> getShading();
    public abstract Enum<Type> getType();
    public abstract Enum<ShapeCount> getCount();
}
