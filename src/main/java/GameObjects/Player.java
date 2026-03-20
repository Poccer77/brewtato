package GameObjects;

import GameObjects.Weapons.Weapon;
import Utilities.Position;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Player implements Object {

    public Weapon[] weapons = new Weapon[6];
    public int health;
    public float speed;
    public int armor;
    public int regen;
    public int damage;
    public int attackSpeed;
    public Position pos;

    public Player() {
        speed = 0.02F;
        pos = new Position(0, 0);

    }

    public void move(float x, float y){
        pos.setPosition(pos.getX() + x, pos.getY() + y);
    }
    public void draw(){
        glBegin(GL_QUADS);
        glColor3d(0.878, 0.667, 0);
        glVertex2d(pos.getX() - 0.05, pos.getY() - 0.05);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() - 0.05, pos.getY() + 0.05);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() + 0.05, pos.getY() + 0.05);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() + 0.05, pos.getY() - 0.05);
        glEnd();

        if (weapons[0] != null) {
            weapons[0].setPos(new Position(pos.getX() - 0.06F, pos.getY() + 0.14F));
            weapons[0].draw();
        }
        if (weapons[1] != null) {
            weapons[1].setPos(new Position(pos.getX() + 0.06F, pos.getY() + 0.14F));
            weapons[1].draw();
        }
        if (weapons[2] != null) {
            weapons[2].setPos(new Position(pos.getX() + 0.1F, pos.getY()));
            weapons[2].draw();
        }
        if (weapons[3] != null) {
            weapons[3].setPos(new Position(pos.getX() + 0.06F, pos.getY() - 0.14F));
            weapons[3].draw();
        }
        if (weapons[4] != null) {
            weapons[4].setPos(new Position(pos.getX() - 0.06F, pos.getY() - 0.14F));
            weapons[4].draw();
        }
        if (weapons[5] != null) {
            weapons[5].setPos(new Position(pos.getX() - 0.1F, pos.getY()));
            weapons[5].draw();
        }
    }

}
