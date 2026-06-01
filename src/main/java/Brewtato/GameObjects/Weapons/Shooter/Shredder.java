package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Main;
import Brewtato.Phases.Game;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.concurrent.ThreadLocalRandom;

import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class Shredder extends Shooter{

    private double explChance;

    public Shredder() {
        super("Shredder", 3, 700, 50, 30, 1000);
        pierce += 2;
        pierceDamageModifier = 1;
        damageMod = 0.5F;
        rarity = 1;
        explChance = 0.5F;
    }

    @Override
    public void attack() {
        if (delay <= 0 && inRange) {
            Position pos1 = new Position(0, width / 6);
            Position pos2 = new Position(0, -width / 6);
            pos1.rotate(angle);
            pos2.rotate(angle);
            pos1.changePosition(pos.getX(), pos.getY());
            pos2.changePosition(pos.getX(), pos.getY());
            Game.projectiles.add(new Projectile(70, angle, damage, pos1, range, this, drawFunc, pierce, true));
            Game.projectiles.add(new Projectile(70, angle, damage, pos2, range, this, drawFunc, pierce, true));
            delay = attackSpeed;
        } else {
            delay -= (float) (Main.tickTime);
        }
    }

    public Draw<Position, Float, Hitbox> drawFunc = (pos, angle, length, width, hit) -> {
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

    public void triggerEffects(Enemy enemy, Projectile projectile) {

        if (ThreadLocalRandom.current().nextDouble(1) < explChance) {

            Projectile expl = new Projectile(1, projectile.angle, projectile.damage * (1 + Stats.explosionDamage / 100), projectile.pos, 3, this, (pos, angle, length, width, hit) -> {

                float size = 300f * (1 + (float) Stats.explosionSize / 100);

                glBegin(GL_QUADS);
                glColor3d(1, 1, 1);
                glVertex2d(pos.getX() - (size / 2), pos.getY() - (size / 2));
                glColor3d(1, 1, 1);
                glVertex2d(pos.getX() + (size / 2), pos.getY() - (size / 2));
                glColor3d(1, 1, 1);
                glVertex2d(pos.getX() + (size / 2), pos.getY() + (size / 2));
                glColor3d(1, 1, 1);
                glVertex2d(pos.getX() - (size / 2), pos.getY() + (size / 2));
                glEnd();

                hit.x1.setPosition(pos.getX() - (size / 2), pos.getY() - (size / 2));
                hit.x2.setPosition(pos.getX() - (size / 2), pos.getY() + (size / 2));
                hit.x3.setPosition(pos.getX() + (size / 2), pos.getY() + (size / 2));
                hit.x4.setPosition(pos.getX() + (size / 2), pos.getY() - (size / 2));

                return hit;
            }, 99, false);
            expl.hitEnemies.add(enemy);
            Game.subProjectiles.add(expl);
        }
    }

    @Override
    public Draw<Position, Float, Hitbox> getDrawFunc() {
        return drawFunc;
    }

    private void Upgrade() {
        damage += 3;
        explChance += 0.125;
        attackSpeed -= 100;
    }

    @Override
    public int getDamageMod() {
        return (int) (Stats.rangedDamage * damageMod);
    }

    @Override
    public void move(float x, float y) {

    }

    @Override
    public void draw() {

        Position pos1 = Tools.rotate(angle, new Position(0, width / 2));
        Position pos2 = Tools.rotate(angle, new Position(length, width / 2));
        Position pos3 = Tools.rotate(angle, new Position(length, width / 10));
        Position pos4 = Tools.rotate(angle, new Position(0, width / 10));

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

        pos1 = Tools.rotate(angle, new Position(0, -width / 10));
        pos2 = Tools.rotate(angle, new Position(length, -width / 10));
        pos3 = Tools.rotate(angle, new Position(length, -width / 2));
        pos4 = Tools.rotate(angle, new Position(0, -width / 2));

        glBegin(GL_QUADS);
        glColor3dv(color);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glEnd();
    }
}
