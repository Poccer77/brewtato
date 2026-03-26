package GameObjects.Enemies;

import Utilities.Position;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Tree extends Enemy{

    public boolean blink = false;

    public Tree(Position pos) {
        this.pos = pos;
        health = 30;
        speed = 0;
    }

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    public void draw(){
        double [] color = {0.267, 0.6, 0, 1};

        if (spawnAnimation > 0) {
            if (spawnAnimation % 10 == 0) {
                blink = !blink;
            }
            if (blink) {
                spawnAnimation--;
                return;
            } else spawnAnimation--;
        } else {color = new double[]{0.502, 0.38, 0, 1};}

        glBegin(GL_QUADS);
        glColor4dv(color);
        glVertex2d(pos.getX() - 50, pos.getY() - 50);
        glColor4dv(color);
        glVertex2d(pos.getX() - 50, pos.getY() + 50);
        glColor4dv(color);
        glVertex2d(pos.getX() + 50, pos.getY() + 50);
        glColor4dv(color);
        glVertex2d(pos.getX() + 50, pos.getY() - 50);
        glEnd();
    }

    @Override
    public boolean getHit(Position pos) {
        return pos.getX() < this.pos.getX() + 50 && pos.getX() > this.pos.getX() - 50
                && pos.getY() < this.pos.getY() + 50 && pos.getY() > this.pos.getY() - 50;
    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position pos, List<Enemy> enemies) {
    }
}
