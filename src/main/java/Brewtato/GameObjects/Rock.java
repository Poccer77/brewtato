package Brewtato.GameObjects;

import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Rock implements Object {

    Position pos;
    float angle;
    float size;

    public Rock(Position pos) {
        this.pos = pos;
        Random rand = new Random();

        angle = (float) Math.toRadians(rand.nextInt(361));
        size = rand.nextInt(20, 61);
    }

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    @Override
    public void draw() {


        Position pos1 = Tools.rotate(angle, new Position(-size / 2, -size / 2));
        Position pos2 = Tools.rotate(angle, new Position(-size / 2, size / 2));
        Position pos3 = Tools.rotate(angle, new Position(size / 2, size / 2));
        Position pos4 = Tools.rotate(angle, new Position(size / 2, -size / 2));

        glBegin(GL_QUADS);
        glColor3d(0.5, 0.5, 0.5);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glColor3d(0.5, 0.5, 0.5);
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glColor3d(0.5, 0.5, 0.5);
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glColor3d(0.5, 0.5, 0.5);
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glEnd();
    }
}
