package be.kuleuven.findaset.test;

import be.kuleuven.findaset.model.shape.enums.Color;
import be.kuleuven.findaset.model.shape.enums.Shading;
import be.kuleuven.findaset.model.shape.enums.Type;

public interface TestableShape {
    Enum<Color> getColor();
    Enum<Shading> getShading();
    Enum<Type> getType();
}
