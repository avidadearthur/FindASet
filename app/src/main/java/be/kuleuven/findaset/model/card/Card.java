package be.kuleuven.findaset.model.card;

import be.kuleuven.findaset.model.card.enums.ShapeCount;
import be.kuleuven.findaset.model.shape.Shape;
import be.kuleuven.findaset.model.shape.ShapeSet;
import be.kuleuven.findaset.model.shape.enums.Color;
import be.kuleuven.findaset.model.shape.enums.Shading;
import be.kuleuven.findaset.model.shape.enums.Type;

public class Card extends AbstractCard{
    public ShapeSet<Shape> shapes;

    @Override
    public void toggle() {

    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public Enum<Color> getColor() {
        return null;
    }

    @Override
    public Enum<Shading> getShading() {
        return null;
    }

    @Override
    public Enum<Type> getType() {
        return null;
    }

    @Override
    public Enum<ShapeCount> getCount() {
        return null;
    }
}
