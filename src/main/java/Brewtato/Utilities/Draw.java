package Brewtato.Utilities;

public interface Draw <Position, Float, Hitbox> {

    public Hitbox apply(Position position, Float angle, Float height, Float width, Hitbox hit);

}
