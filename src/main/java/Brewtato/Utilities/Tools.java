package Brewtato.Utilities;

import static java.lang.Math.*;

public class Tools {

    public static float angle(Position pos1, Position pos2) {
        return (float) Math.atan2(pos2.getY() - pos1.getY(), pos2.getX() - pos1.getX());
    }

    public static float distance(Position pos1, Position pos2) {
        return (float) sqrt(pow(pos2.getX() - pos1.getX(), 2) + pow(pos2.getY() - pos1.getY(), 2));
    }

    public static float distance(Position pos1) {
        return (float) sqrt(pow(0 - pos1.getX(), 2) + pow(0 - pos1.getY(), 2));
    }

    public static Position rotate(float angle, Position pos) {
        pos.rotate(angle);
        return pos;
    }
}
