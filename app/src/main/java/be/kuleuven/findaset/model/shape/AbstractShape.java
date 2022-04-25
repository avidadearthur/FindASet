package be.kuleuven.findaset.model.shape;
import be.kuleuven.findaset.test.TestableShape;

public abstract class AbstractShape implements TestableShape {
    public abstract Enum<Color> getColor();
    public abstract Enum<Shading> getShading();
    public abstract Enum<Type> getType();
}
