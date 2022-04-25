package be.kuleuven.findaset.test;

import be.kuleuven.findaset.model.card.enums.ShapeCount;

public interface TestableCard {
    void toggle();
    boolean isSelected();
    Enum<ShapeCount> getCount();
}
