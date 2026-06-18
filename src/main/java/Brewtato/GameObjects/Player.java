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
        playerSpeed = 15;
        this.pos = pos;
        hit = new Hitbox();
    }

    public void move(float x, float y){
        pos.changePosition(x, y);
    }

    public void draw(){

        hit = Tools.drawSquare(pos,120, 120, new double[]{0.878, 0.667, 0}, true);

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
        playerCurrentHealth = Math.min(playerMaxHealth, playerCurrentHealth + amount);
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
