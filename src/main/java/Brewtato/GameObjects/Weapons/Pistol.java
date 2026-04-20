package Brewtato.GameObjects.Weapons;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Pistol extends Weapon {

    boolean inRange;

    public Pistol(int damage, int attackSpeed, float length, float width, int range) {
        super(damage, attackSpeed, length, width, range);
    }

    @Override
    public void draw() {

        Position pos1 = Tools.rotate(angle, new Position(0, width / 2));
        Position pos2 = Tools.rotate(angle, new Position(length, width / 2));
        Position pos3 = Tools.rotate(angle, new Position(length, -width / 2));
        Position pos4 = Tools.rotate(angle, new Position(0, -width / 2));


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
            projectiles.add(new PistolProjectile(angle, 5, pos, range));
            delay = attackSpeed;
        } else {
            delay -= 10;
        }
    }

    public void aim(List<Enemy> enemies, Position playerMove) {

        if (!enemies.isEmpty()) {
            Enemy closestEnemy = enemies.get(0);
            for (Enemy enemy : enemies) {
                if (Tools.distance(pos, enemy.pos) < Tools.distance(pos, closestEnemy.pos)) {
                    closestEnemy = enemy;
                }
            }
            if (Tools.distance(pos, closestEnemy.pos) < range) {
                angle = Tools.angle(new Position(pos.getX(), pos.getY()), closestEnemy.pos);
                inRange = true;
                return;
            }
        }
        if (playerMove.getX() < 0) angle = (float) Math.toRadians(180);
        else if (playerMove.getX() > 0) angle = 0;
        inRange = false;
    }
}

