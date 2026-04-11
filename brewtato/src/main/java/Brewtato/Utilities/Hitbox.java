package Brewtato.Utilities;

public class Hitbox {

    public Position x1, x2, x3, x4;

    public Hitbox() {
        x1 = new Position(0, 0);
        x2 = new Position(0, 0);
        x3 = new Position(0, 0);
        x4 = new Position(0, 0);
    }

    public boolean isWithin(Position pos) {

        return pos.getX() > x2.getX() && pos.getX() < x3.getX() && pos.getX() > x1.getX() && pos.getX() < x4.getX()
                && pos.getY() < x3.getY() && pos.getY() > x4.getY() && pos.getY() > x1.getY() && pos.getY() < x2.getY();


    }

}
