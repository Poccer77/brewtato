package GameObjects.Enemies;

import Utilities.Position;
import Utilities.Tools;

import static org.lwjgl.opengl.GL11.*;

public class Grunt extends Enemy{

    public Grunt(Position pos) {
        health = 10;
        speed = 0.01F;
        this.pos = pos;
    }

    @Override
    public boolean getHit(Position pos) {
        return pos.getX() < this.pos.getX() + 0.05 && pos.getX() > this.pos.getX() - 0.05
                && pos.getY() < this.pos.getY() + 0.05 && pos.getY() > this.pos.getY() - 0.05;
    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position playerPos) {
        float angle = Tools.angle(pos, playerPos);
        Position moveTo = Tools.rotate(angle, new Position(0, speed));
        move(moveTo.getX(), moveTo.getY());
    }

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    @Override
    public void draw(){
        glBegin(GL_QUADS);
        glColor3d(0.659, 0, 0.878);
        glVertex2d(pos.getX() - 0.05, pos.getY() - 0.05);
        glColor3d(0.659, 0, 0.878);
        glVertex2d(pos.getX() - 0.05, pos.getY() + 0.05);
        glColor3d(0.659, 0, 0.878);
        glVertex2d(pos.getX() + 0.05, pos.getY() + 0.05);
        glColor3d(0.659, 0, 0.878);
        glVertex2d(pos.getX() + 0.05, pos.getY() - 0.05);
        glEnd();
    }
}
