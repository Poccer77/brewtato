package GameObjects;

import GameObjects.Weapons.Weapon;
import Utilities.Position;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Player implements Object {

    public static Weapon[] weapons = new Weapon[6];
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
    }

}
