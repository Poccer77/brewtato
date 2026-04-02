package Brewtato.Utilities;

import Brewtato.Main;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;

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

    public static void dim() {
        glBegin(GL_QUADS);
        glColor4d(0, 0, 0, 0.5);
        glVertex2d(0, 0);
        glColor4d(0, 0, 0, 0.5);
        glVertex2d(Main.vidmode.width(), 0);
        glColor4d(0, 0, 0, 0.5);
        glVertex2d(Main.vidmode.width(), Main.vidmode.height());
        glColor4d(0, 0, 0, 0.5);
        glVertex2d(0, Main.vidmode.height());
        glEnd();
    }
}
