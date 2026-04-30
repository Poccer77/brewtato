package Brewtato.GameObjects;

import Brewtato.GameObjects.Weapons.Weapon;
import Brewtato.Stats;
import Brewtato.Utilities.*;

import java.util.ArrayList;
import java.util.List;

import static Brewtato.Stats.*;

import static org.lwjgl.opengl.GL11.*;

public class Player implements Object {

    public Hitbox hit;

    public int invul = invulnerability;
    public Position pos;

    public Player(Position pos) {
        playerSpeed = 19;
        this.pos = pos;
        hit = new Hitbox();
    }

    public void move(float x, float y){
        pos.changePosition(x, y);
    }

    public void draw(){
        glBegin(GL_QUADS);
        glColor3d(0.878, 0.667, 0);
        glVertex2d(pos.getX() - 60, pos.getY() - 60);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() - 60, pos.getY() + 60);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() + 60, pos.getY() + 60);
        glColor3f(0.878F, 0.667F, 0);
        glVertex2d(pos.getX() + 60, pos.getY() - 60);
        glEnd();

        hit.x1.setPosition(pos.getX() - 80, pos.getY() - 80);
        hit.x2.setPosition(pos.getX() - 80, pos.getY() + 80);
        hit.x3.setPosition(pos.getX() + 80, pos.getY() + 80);
        hit.x4.setPosition(pos.getX() + 80, pos.getY() - 80);

        int weaponCount = ownedWeapons.size();
        if (weaponCount == 0) return;
        int anglePart = (360 / weaponCount);
        float angle = 0;

        for (Weapon weapon : ownedWeapons) {
            weapon.setPos(new Position(150, 0));
            weapon.pos.rotate(angle);
            weapon.pos.changePosition(pos.getX(), pos.getY());
            angle += (float) Math.toRadians(anglePart);
        }
    }

    public void heal(int amount) {
        playerCurrentHealth += amount;
    }

    public void getHit(int damage, boolean triggerInvul) {
        damage = Math.round(damage * ((float) 100 / (100 + armor)));
        if (!triggerInvul) {
            playerCurrentHealth -= damage;
        } else if (invul <= 0) {
            playerCurrentHealth -= damage;
            invul = invulnerability;
        }
    }
    public boolean collect(Position pos) {
        return Tools.distance(this.pos, pos) < 60;
    }
}
