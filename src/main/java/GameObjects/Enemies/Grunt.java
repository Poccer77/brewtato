package GameObjects.Enemies;

import Utilities.Position;

import static org.lwjgl.opengl.GL11.*;

public class Grunt extends Enemy{

    public Grunt(Position pos) {
        health = 10;
        speed = 0.01F;
        this.pos = pos;
    }

    @Override
    public void getHit(int damage) {
        health -= damage;
    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position playerPos) {
        double angle = - Math.atan2(pos.getX() - playerPos.getX(), pos.getY() - playerPos.getY());
        move((float) Math.cos(angle) * speed, (float) Math.sin(angle) * speed);
        System.out.println(angle);
    }

    @Override
    public void move(float x, float y) {
        pos.setPosition(pos.getX() + x, pos.getY() + y);
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
