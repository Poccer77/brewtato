package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.Effects.DamageNumber;
import Brewtato.Effects.Debuff;
import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Main;
import Brewtato.Phases.Game;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.Random;

import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;

public class Flamethrower extends Shooter{

    float burnDamage;

    public Flamethrower() {
        super("Flamethrower", 1, 100, 120, 40, 1000);
        pierce += 99;
        damageMod = 0.5f;
        rarity = 2;
        burnDamage = 0.3f;
    }

    @Override
    public void attack() {
        if (delay <= 0 && inRange) {
            Projectile pro = new Projectile(40, angle  + (float) Math.toRadians(new Random().nextInt(60) - 30), damage, pos, range, this, drawFunc, 99, true);
            pro.length = pro.width = 70;
            Game.projectiles.add(pro);
            delay = attackSpeed;
        } else {
            delay -= (float) (Main.tickTime);
        }
    }

    @Override
    public void move(float x, float y) {

    }

    @Override
    public int getDamageMod() {
        return (int) (damageMod * Stats.elementalDamage);
    }

    @Override
    public void draw() {

        Position pos1 = Tools.rotate(angle, new Position(0, width / 1.5F));
        Position pos2 = Tools.rotate(angle, new Position(length, width / 2.5F));
        Position pos3 = Tools.rotate(angle, new Position(length, -width / 2.5F));
        Position pos4 = Tools.rotate(angle, new Position(0, -width / 1.5F));

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

    @Override
    public void triggerEffects(Enemy enemy, Projectile projectile) {

        Debuff debuff = new Debuff(2000, 10, Stats::getBurnFrequency, (tempEnemy) -> {
            tempEnemy.health -= Math.max(1, (int) (burnDamage * Stats.elementalDamage));
            Game.damageNumbers.add(new DamageNumber(Math.max(1, (int) (burnDamage * Stats.elementalDamage)), new double[]{1, 1, 1}, new Position(enemy.pos)));
        });

        enemy.debuffs.add(debuff);
    }

    @Override
    public Draw<Position, Float, Hitbox> getDrawFunc() {
        return drawFunc;
    }

    public Draw<Position, Float, Hitbox> drawFunc = (pos, angle, length, width, hit) -> {

        Position pos1 = rotate((float) (angle + Math.toRadians(45)), new Position(- length / 2, width / 2));
        Position pos2 = rotate((float) (angle + Math.toRadians(45)), new Position(length / 2, width / 2));
        Position pos3 = rotate((float) (angle + Math.toRadians(45)), new Position(length / 2, -width / 2));
        Position pos4 = rotate((float) (angle + Math.toRadians(45)), new Position(- length / 2, -width / 2));

        glBegin(GL_QUADS);
        glColor3d(1, 0.7, 0.4);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glEnd();

        hit.x1.setPosition(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        hit.x2.setPosition(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        hit.x3.setPosition(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        hit.x4.setPosition(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());

        return hit;
    };
}
