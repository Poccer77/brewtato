package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.List;
import java.util.Random;

import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;
public class Shotgun extends Shooter {

    boolean inRange;

    public Shotgun() {
        super("Shotgun", 4, 1000, 60, 30, 800 + Stats.range);
        pierce += 2;
        pierceDamageModifier = 0.5F;
        damageMod = 0.5F;
        super.upgrade();
    }

    @Override
    public Draw<Position, Float, Hitbox> getDrawFunc() {
        return drawFunc;
    }

    @Override
    public void shoot() {
        if (delay <= 0 && inRange) {
            for (int i = 0; i < 5; i++) {
                projectiles.add(new Projectile(80, angle + (float) Math.toRadians(new Random().nextInt(61) - 30), damage, pos, range, this, drawFunc, pierce));
            }
            delay = attackSpeed;
        } else {
            delay -= 10;
        }
    }

    @Override
    public void move(float x, float y) {

    }

    @Override
    public void draw() {

        Position pos1 = Tools.rotate(angle, new Position(0, width / 2.5F));
        Position pos2 = Tools.rotate(angle, new Position(length, width / 1.5F));
        Position pos3 = Tools.rotate(angle, new Position(length, -width / 1.5F));
        Position pos4 = Tools.rotate(angle, new Position(0, -width / 2.5F));

        double[] color = new double[]{1, 1, 1};

        if (rarity == 2) color = new double[]{0.7, 0.7, 1};
        if (rarity == 3) color = new double[]{0.8, 0.5, 0.9};
        if (rarity == 4) color = new double[]{1, 0.5, 0.5};

        glBegin(GL_QUADS);
        glColor3dv(color);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glEnd();
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

    @Override
    public int getDamageMod() {
        return (int) (damageMod * Stats.rangedDamage);
    }

    @Override
    public void upgrade() {
        range += 25;
        damage += 2;
        pierceDamageModifier += 0.1F;
        damageMod += 0.1F;
        attackSpeed -= 50F;
    }

    private Draw<Position, Float, Hitbox> drawFunc = (pos, angle,length, width, hit) -> {
        Position pos1 = rotate(angle, new Position(- length / 2, width / 2));
        Position pos2 = rotate(angle, new Position(length / 2, width / 2));
        Position pos3 = rotate(angle, new Position(length / 2, -width / 2));
        Position pos4 = rotate(angle, new Position(- length / 2, -width / 2));

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

        hit.x1.setPosition(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        hit.x2.setPosition(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        hit.x3.setPosition(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        hit.x4.setPosition(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());

        return hit;
    };
}
