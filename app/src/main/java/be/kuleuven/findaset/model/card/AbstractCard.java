package be.kuleuven.findaset.model.card;

import be.kuleuven.findaset.model.card.enums.SHAPECOUNT;
import be.kuleuven.findaset.model.card.enums.COLOR;
import be.kuleuven.findaset.model.card.enums.SHADING;
import be.kuleuven.findaset.model.card.enums.TYPE;
import be.kuleuven.findaset.test.TestableCard;

public abstract class AbstractCard implements TestableCard {
    public abstract void toggle();
    public abstract boolean isSelected();
    public abstract Enum<COLOR> getColor();
    public abstract Enum<SHADING> getShading();
    public abstract Enum<TYPE> getType();
    public abstract Enum<SHAPECOUNT> getCount();
}
