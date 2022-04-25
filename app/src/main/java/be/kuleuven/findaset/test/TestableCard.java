package be.kuleuven.findaset.test;

import be.kuleuven.findaset.model.card.enums.Color;
import be.kuleuven.findaset.model.card.enums.Shading;
import be.kuleuven.findaset.model.card.enums.ShapeCount;
import be.kuleuven.findaset.model.card.enums.Type;

public interface TestableCard {
    void toggle();
    boolean isSelected();
    Enum<ShapeCount> getCount();
    Enum<Shading> getShading();
    Enum<Color> getColor();
    Enum<Type> getType();
}
