package be.kuleuven.findaset.model.shape;

import be.kuleuven.findaset.model.shape.enums.Color;
import be.kuleuven.findaset.model.shape.enums.Shading;
import be.kuleuven.findaset.model.shape.enums.Type;

public class Shape extends AbstractShape{
    public Enum<Color> colorEnum;
    public Enum<Shading> shadingEnum;
    public Enum<Type> typeEnum;

    public Shape(Enum<Color> colorEnum, Enum<Shading> shadingEnum, Enum<Type> typeEnum) {
        this.colorEnum = colorEnum;
        this.shadingEnum = shadingEnum;
        this.typeEnum = typeEnum;
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
}
