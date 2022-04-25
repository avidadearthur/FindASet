package be.kuleuven.findaset.test;

import be.kuleuven.findaset.model.card.enums.COLOR;
import be.kuleuven.findaset.model.card.enums.SHADING;
import be.kuleuven.findaset.model.card.enums.SHAPECOUNT;
import be.kuleuven.findaset.model.card.enums.TYPE;

public interface TestableCard {
    void toggle();
    boolean isSelected();
    Enum<SHAPECOUNT> getCount();
    Enum<SHADING> getShading();
    Enum<COLOR> getColor();
    Enum<TYPE> getType();
}
