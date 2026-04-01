package Brewtato.GameObjects;

import Brewtato.GameObjects.Weapons.Weapon;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static org.lwjgl.opengl.GL11.*;

public class Player implements Object {

    public Weapon[] weapons = new Weapon[6];
    public int health;
    public float speed;
    public int armor;
    public int luck;
    public int regen;
    public int damage;
    public int attackSpeed;
    public int collectionRadius = 400;
    public int materials;
    public float materialModifier = 1;
    public int level;
    public int exp;
    public float expModifier;
    public int dodge;
    public int invulnerability = 100;
    public Position pos;

    public Player(Position pos) {
        speed = 25F;
        this.pos = pos;

    }

    public void move(float x, float y){
        pos.setPosition(pos.getX() + x, pos.getY() + y);
    }
    public void draw(){
        glBegin(GL_QUADS);
        glColor3d(0.878, 0.667, 0);
        glVertex2d(pos.getX() - 40, pos.getY() - 40);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() - 40, pos.getY() + 40);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() + 40, pos.getY() + 40);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() + 40, pos.getY() - 40);
        glEnd();

        int weaponCount = weapons.length;
        int anglePart = (360 / weaponCount);
        float angle = 0;

        for (Weapon weapon : weapons) {
            weapon.setPos(new Position(100, 0));
            weapon.pos.rotate(angle);
            weapon.pos.changePosition(pos.getX(), pos.getY());
            weapon.draw();
            angle += (float) Math.toRadians(anglePart);
        }
    }
    public void getHit(int damage, boolean triggerInvul) {
        damage *= 100 / (100 + armor);
        if (!triggerInvul) {
            health -= damage;
            invulnerability -= 10;
        } else if (invulnerability > 0) {
            invulnerability -= 10;
        } else {
            health -= damage;
            invulnerability = 100;
        }
    }
    public boolean collect(Position pos) {
        return Tools.distance(this.pos, pos) < 60;
    }
}
