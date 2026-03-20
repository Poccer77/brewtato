package Utilities;

import static java.lang.Math.*;

public class Tools {

    public static float angle(Position pos1, Position pos2) {
        return (float) -Math.atan2(pos2.getX() - pos1.getX(), pos2.getY() - pos1.getY());
    }

    public static float distance(Position pos1, Position pos2) {
        return (float) sqrt(pow(pos2.getX() - pos1.getX(), 2) + pow(pos2.getY() + pos1.getY(), 2));
    }

    public static Position rotate(float angle, Position pos) {
        return new Position((float) (pos.getX() * cos(angle) - pos.getY() * sin(angle)), (float) (pos.getX() * sin(angle) + pos.getY() * cos(angle)));
    }
}
