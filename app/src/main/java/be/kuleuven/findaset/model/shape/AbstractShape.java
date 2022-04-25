package be.kuleuven.findaset.model.shape;
import be.kuleuven.findaset.model.shape.enums.Color;
import be.kuleuven.findaset.model.shape.enums.Shading;
import be.kuleuven.findaset.model.shape.enums.Type;
import be.kuleuven.findaset.test.TestableShape;

public abstract class AbstractShape implements TestableShape {
    public abstract Enum<Color> getColor();
    public abstract Enum<Shading> getShading();
    public abstract Enum<Type> getType();
}
