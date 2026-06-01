package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Main;
import Brewtato.Phases.Game;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class ClusterBomber extends Shooter{

    private int pellets = 25;
    private int projectileRange;
    private int projectileSpeed;

    public ClusterBomber() {
        super("Cluster Bomber", 25, 2500, 120, 50, 3000);
        rarity = 3;
        damageMod = 1.2F;
        pierceDamageModifier = 1;
    }

    public Enemy aim(List<Enemy> enemies, Position playerMove) {
        Enemy closest = super.aim(enemies, playerMove);
        if (closest == null) return null;
        Position target = new Position();

        target.rotate(Tools.angle(closest.pos, Game.player.pos));
        target.changePosition(closest.pos.getX() + closest.speed * 15, closest.pos.getY());
        angle = Tools.angle(this.pos, target);
        projectileRange = (int) Tools.distance(Game.player.pos, closest.pos);
        projectileSpeed = projectileRange / 15;
        return null;
    }

    @Override
    public void attack() {
        if (delay <= 0 && inRange) {
            Projectile tempPro = new Projectile(projectileSpeed, angle, damage, pos, projectileRange, this, getDrawFunc(), pierce, true);
            tempPro.length = tempPro.width = 75;
            Game.projectiles.add(tempPro);
            delay = attackSpeed;
        } else {
            delay -= (float) (Main.tickTime);
        }
    }

    public void upgrade() {
        pellets += 5;
        damage += 10;
        attackSpeed -= 500;
    }

    @Override
    public Draw<Position, Float, Hitbox> getDrawFunc() {
        return drawFunc;
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
        Position pos2 = Tools.rotate(angle, new Position(length / 2, width / 2));
        Position pos3 = Tools.rotate(angle, new Position(length / 2, -width / 2));
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

        pos1 = Tools.rotate(angle, new Position(length / 2, width / 2));
        pos2 = Tools.rotate(angle, new Position(length, width / 2));
        pos3 = Tools.rotate(angle, new Position(length, width / 10));
        pos4 = Tools.rotate(angle, new Position(length / 2, width / 10));

        glBegin(GL_QUADS);
        glColor3dv(color);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glEnd();

        pos1 = Tools.rotate(angle, new Position(length / 2, -width / 10));
        pos2 = Tools.rotate(angle, new Position(length, -width / 10));
        pos3 = Tools.rotate(angle, new Position(length, -width / 2));
        pos4 = Tools.rotate(angle, new Position(length / 2, -width / 2));

        glBegin(GL_QUADS);
        glColor3dv(color);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glEnd();
    }

    public void triggerEffects(Enemy enemy, Projectile projectile) {
        if (projectile.damageMod == this.getDamageMod()) {
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
            Random rand = ThreadLocalRandom.current();
            for (int i = 0; i < pellets; i++) {
                Projectile tempPro = new Projectile(rand.nextInt(20, 51),
                        rand.nextFloat(-3.14f, 3.14f),
                        damage,
                        projectile.pos,
                        rand.nextInt(150, 500),
                        this,
                        getDrawFunc(),
                        10000,
                        true
                );
                tempPro.hitEnemies.addAll(Game.enemies);
                tempPro.hitEnemies.addAll(Game.spawningEnemies);
                tempPro.damageMod = 1f;
                Game.subProjectiles.add(tempPro);
            }
        } else {
            Projectile expl = new Projectile(1, projectile.angle, projectile.damage * (1 + Stats.explosionDamage / 100), projectile.pos, 3, this, (pos, angle, length, width, hit) -> {

                float size = 150f * (1 + (float) Stats.explosionSize / 100);

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

    public Draw<Position, Float, Hitbox> drawFunc = (pos, angle, height, width, hit) -> {

        float size = height;

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

        hit.x1.setPosition(pos.getX() + (size / 2), pos.getY() + (size / 2));
        hit.x2.setPosition(pos.getX() + (size / 2), pos.getY() - (size / 2));
        hit.x3.setPosition(pos.getX() - (size / 2), pos.getY() - (size / 2));
        hit.x4.setPosition(pos.getX() - (size / 2), pos.getY() + (size / 2));

        return hit;
    };
}
