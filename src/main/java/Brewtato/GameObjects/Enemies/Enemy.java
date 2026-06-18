package Brewtato.GameObjects.Enemies;

import Brewtato.Effects.Debuff;
import Brewtato.GameObjects.Object;
import Brewtato.Phases.Game;
import Brewtato.Utilities.*;

import java.util.ArrayList;
import java.util.List;

import static Brewtato.Utilities.Tools.*;
import static Brewtato.Utilities.Tools.distance;

public abstract class Enemy implements Object {

    public Hitbox hit;
    public int health;
    public int damage;
    public float width;
    public float height;
    public float angle;
    public float speed;
    public Position pos;
    public int spawnAnimation = 70;
    public int deathAnimation = 60;
    boolean blink = false;
    double[] color;
    public int lootAmount;
    public double lootChance;
    public List<Debuff> debuffs = new ArrayList<Debuff>();

    public boolean getHit(Hitbox hitbox) {
        return overlap(hit, hitbox);
    }
    public abstract void draw();
    public abstract void hunt(Position playerPos, List<Enemy> enemies);
    public void huntPLayer(Position playerPos, List<Enemy> enemies) {
        float angle = angle(pos, playerPos);
        Position moveTo = rotate(angle, new Position(speed, 0));
        int count = 0;
        Position steer = new Position(0, 0);
        for (Enemy enemy : enemies) {
            if (enemy == this) continue;
            float dist = distance(pos, enemy.pos);
            if (dist < 80 && dist > 0) {
                count++;
                Position tempSteer = new Position(pos.getX() - enemy.pos.getX(), pos.getY() - enemy.pos.getY());
                tempSteer.normalize();
                float strength = 80 - dist;
                steer.changePosition(tempSteer.getX() * strength, tempSteer.getY() * strength);
            }
        }
        if (count > 0) {
            steer.setPosition(steer.getX() / count, steer.getY() / count);
        }

        move(steer.getX(), steer.getY());
        move(moveTo.getX(), moveTo.getY());
    }

    public abstract void spawn();

    public void spawnBlink(double[] blinkColor) {
        if (spawnAnimation % 10 == 0) {
            blink = !blink;
        }
        if (blink) {
            spawnAnimation--;
            color = blinkColor;
        } else {
            spawnAnimation--;
            color = new double[]{1, 0, 0, 0};
        }
    }

    public boolean die() {
        if (deathAnimation > 0) {
            width *= ((float) deathAnimation / 50);
            height *= ((float) deathAnimation / 50);
            deathAnimation -= 10;
            return false;
        } else return true;
    }
}
