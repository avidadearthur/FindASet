package be.kuleuven.findaset.model.card;

import be.kuleuven.findaset.model.card.enums.ShapeCount;
import be.kuleuven.findaset.model.card.enums.Color;
import be.kuleuven.findaset.model.card.enums.Shading;
import be.kuleuven.findaset.model.card.enums.Type;

public class Card extends AbstractCard{

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
