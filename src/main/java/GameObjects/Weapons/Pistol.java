package GameObjects.Weapons;

import GameObjects.Enemies.Enemy;
import Utilities.Position;

import java.util.List;

import static Utilities.Tools.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Pistol extends Weapon{

    boolean inRange;

    public Pistol(int damage, int attackSpeed, float length, float width, double range) {
        super(damage, attackSpeed, length, width, range);
    }

    @Override
    public void draw() {

        Position pos1 = rotate(angle, new Position(width / 2, 0));
        Position pos2 = rotate(angle, new Position(-width / 2, 0));
        Position pos3 = rotate(angle, new Position(-width / 2, length));
        Position pos4 = rotate(angle, new Position(width / 2, length));

        glBegin(GL_QUADS);
        glColor3d(1, 1, 1);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glColor3d(1, 1, 1);
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glColor3d(1, 1, 1);
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glColor3d(1, 1, 1);
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glEnd();
    }

    @Override
    public void move(float x, float y) {

    }

    public void shoot() {
        if (delay <= 0 && inRange) {
            projectiles.add(new PistolProjectile(angle, 5, pos));
            delay = attackSpeed;
        } else {delay -= 10;}
    }

    public void aim(List<Enemy> enemies) {

        if (enemies.isEmpty()) {
            angle = 0;
            return;
        }

        Enemy closestEnemy = enemies.get(0);

        for (Enemy enemy : enemies) {
            if (distance(pos, enemy.pos) < distance(pos, closestEnemy.pos)) {
                closestEnemy = enemy;
            }
        }
        if (distance(pos, closestEnemy.pos) < range) {
            angle = angle(pos, closestEnemy.pos);
            inRange = true;
        } else inRange = false;

    }
}

