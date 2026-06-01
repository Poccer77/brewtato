package Brewtato.GameObjects.Enemies;

import Brewtato.Stats;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Utilities.Tools.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class Grunt extends Enemy {

    public float width;
    public float height;
    public float angle;

    public Grunt(Position pos, int wave) {
        health = 3 + 2 * wave;
        speed = 10F;
        this.pos = pos;
        color = new double[]{0.78, 0, 0, 1};
        hit = new Hitbox();
        damage = 1 + (int) (wave * 0.6);
        width = height = 120;
        lootAmount = 1 + (int) ((ThreadLocalRandom.current().nextDouble( 1) < Stats.materialModifier - (int) Math.floor(Stats.materialModifier)) ? Math.ceil(Stats.materialModifier) : Math.floor(Stats.materialModifier));
        lootChance = Math.clamp(Stats.luck * 0.001, 0.09, 0.1);
    }

    @Override
    public boolean getHit(Hitbox hitbox) {
        return overlap(hit, hitbox);
    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position playerPos, List<Enemy> enemies) {
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

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    public void spawn() {
        if (spawnAnimation % 10 == 0) {
            blink = !blink;
        }
        if (blink) {
            spawnAnimation--;
            color = new double[]{0.78, 0, 0, 1};
        } else {
            spawnAnimation--;
            color = new double[]{1, 0, 0, 0};
        }
    }

    @Override
    public boolean die() {
        if (deathAnimation > 0) {
            width *= ((float) deathAnimation / 50);
            height *= ((float) deathAnimation / 50);
            deathAnimation -= 10;
            return false;
        } else return true;
    }

    @Override
    public void draw(){

        if (spawnAnimation <= 0) color = new double[] {0.584, 0, 0.78, 1};

        hit = Tools.drawSquare(pos, width, height, color);
    }
}
