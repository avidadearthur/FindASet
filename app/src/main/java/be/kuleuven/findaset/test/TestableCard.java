package be.kuleuven.findaset.test;

import be.kuleuven.findaset.model.card.ShapeCount;

public interface TestableCard {
    void toggle();
    boolean isSelected();
    Enum<ShapeCount> getCount();
}
