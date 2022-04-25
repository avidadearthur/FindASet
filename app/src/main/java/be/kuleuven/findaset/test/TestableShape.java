package be.kuleuven.findaset.test;

import be.kuleuven.findaset.model.shape.Color;
import be.kuleuven.findaset.model.shape.Shading;
import be.kuleuven.findaset.model.shape.Type;

public interface TestableShape {
    Enum<Color> getColor();
    Enum<Shading> getShading();
    Enum<Type> getType();
}
