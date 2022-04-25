package be.kuleuven.findaset.model.card;

import be.kuleuven.findaset.model.card.enums.ShapeCount;
import be.kuleuven.findaset.model.shape.enums.Color;
import be.kuleuven.findaset.model.shape.enums.Shading;
import be.kuleuven.findaset.model.shape.enums.Type;
import be.kuleuven.findaset.test.TestableCard;
import be.kuleuven.findaset.test.TestableShape;

public abstract class AbstractCard implements TestableCard, TestableShape {
    public abstract void toggle();
    public abstract boolean isSelected();
    public abstract Enum<Color> getColor();
    public abstract Enum<Shading> getShading();
    public abstract Enum<Type> getType();
    public abstract Enum<ShapeCount> getCount();
}
