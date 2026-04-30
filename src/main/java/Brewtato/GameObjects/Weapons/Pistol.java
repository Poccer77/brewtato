package Brewtato.GameObjects.Weapons;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Main;
import Brewtato.Stats;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Pistol extends Shooter {

    public Pistol() {
        super("Pistol", 10, 800, 80, 30, 1000 + Stats.range);
        pierceDamageModifier = 0.75F;
        damageMod = 1;
        pierce += 1;
        rarity = 1;
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

    public void upgrade() {
        damage += 5;
        attackSpeed -= 50F;
        damageMod += 0.1F;
        super.upgrade();
    }

    @Override
    public void move(float x, float y) {

    }

    public void shoot() {
        if (delay <= 0 && inRange) {
            projectiles.add(new PistolProjectile(angle, 5, pos, range, this));
            delay = attackSpeed;
        } else {
            delay -= (float) (Main.tickTime);
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

