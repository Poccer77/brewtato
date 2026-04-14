package Brewtato.GameObjects.Enemies;

import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Grunt extends Enemy{

    public float width;
    public float height;

    public Grunt(Position pos) {
        health = 10;
        speed = 13F;
        this.pos = pos;
        color = new double[]{0.78, 0, 0, 1};
        hit = new Hitbox();
        damage = 1;
        width = height = 120;
    }

    @Override
    public boolean getHit(Hitbox hitbox) {

        return Tools.overlap(hit, hitbox);

    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position playerPos, List<Enemy> enemies) {
        float angle = Tools.angle(pos, playerPos);
        Position moveTo = Tools.rotate(angle, new Position(speed, 0));
        int count = 0;
        Position steer = new Position(0, 0);
        for (Enemy enemy : enemies) {
            if (enemy == this) continue;
            float dist = Tools.distance(pos, enemy.pos);
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

        glBegin(GL_QUADS);
        glColor4dv(color);
        glVertex2d(pos.getX() - (width / 2), pos.getY() - (height / 2));
        glColor4dv(color);
        glVertex2d(pos.getX() - (width / 2), pos.getY() + (height / 2));
        glColor4dv(color);
        glVertex2d(pos.getX() + (width / 2), pos.getY() + (height / 2));
        glColor4dv(color);
        glVertex2d(pos.getX() + (width / 2), pos.getY() - (height / 2));
        glEnd();

        hit.x1.setPosition(pos.getX() - 60, pos.getY() - 60);
        hit.x2.setPosition(pos.getX() - 60, pos.getY() + 60);
        hit.x3.setPosition(pos.getX() + 60, pos.getY() + 60);
        hit.x4.setPosition(pos.getX() + 60, pos.getY() - 60);
    }
}
