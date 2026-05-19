package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Main;
import Brewtato.Phases.Game;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;

public class RocketLauncher extends Shooter{

    public RocketLauncher() {
        super("RocketLauncher", 10, 1500, 120, 30, 2000);
        damageMod = 1;
        pierceDamageModifier = 1;
        rarity = 2;
        pierce = 1;
    }

    public Draw<Position, Float, Hitbox> getDrawFunc() {
        return drawFunc;
    }

    @Override
    public void upgrade() {
        damage += 5;
        damageMod += 0.1F;
        attackSpeed -= 100;
        super.upgrade();
    }

    @Override
    public void shoot() {
        if (delay <= 0 && inRange) {
            Game.projectiles.add(new Projectile(70, angle, damage, pos, range, this, getDrawFunc(), pierce, true));
            delay = attackSpeed;
        } else {
            delay -= (float) (Main.tickTime);
        }
    }

    @Override
    public int getDamageMod() {
        return (int) (damageMod * Stats.rangedDamage);
    }

    @Override
    public void move(float x, float y) {

    }

    @Override
    public void draw() {

        Position pos1 = Tools.rotate(angle, new Position(0, width / 2));
        Position pos2 = Tools.rotate(angle, new Position(length, width / 2));
        Position pos3 = Tools.rotate(angle, new Position(length, -width / 2));
        Position pos4 = Tools.rotate(angle, new Position(0, -width / 2));

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

    public void triggerEffects(Enemy enemy, Projectile projectile) {

        if (projectile.speed == 1) return;

        Projectile expl = new Projectile(1, projectile.angle, projectile.damage * (1 + Stats.explosionDamage / 100), projectile.pos, 3, this, (pos, angle, length, width, hit) -> {

            float size = 500f * (1 + (float) Stats.explosionSize / 100);

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

    public Draw<Position, Float, Hitbox> drawFunc = (pos, angle, length, width, hit) -> {
        Position pos1 = rotate(angle, new Position(- length / 2, width / 2));
        Position pos2 = rotate(angle, new Position(length / 2, 0));
        Position pos3 = rotate(angle, new Position(- length / 2, -width / 2));

        glBegin(GL_TRIANGLES);
        glColor3d(1, 1, 1);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glColor3d(1, 1, 1);
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glColor3d(1, 1, 1);
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glColor3d(1, 1, 1);
        glEnd();

        hit.x1.setPosition(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        hit.x2.setPosition(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        hit.x3.setPosition(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        hit.x4.setPosition(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());

        return hit;
    };
}
