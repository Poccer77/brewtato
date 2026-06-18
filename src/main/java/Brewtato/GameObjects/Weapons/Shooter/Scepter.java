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
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class Scepter extends Shooter{

    private int counterMax;
    private int counter;

    public Scepter() {
        super("Scepter", 10, 1300, 100, 20, 1100);
        counterMax = 20;
        counter = counterMax;
    }

    @Override
    public void attack() {
        if (delay <= 0 && inRange) {
            Game.projectiles.add(new Projectile(90, angle, damage, pos, range, this, drawFunc, pierce, true));
            delay = attackSpeed;
        } else {
            delay -= (float) (Main.tickTime);
        }
    }

    @Override
    public int getDamageMod() {
        return Stats.rangedDamage;
    }

    public void upgrade() {
        counterMax -= 2;
        attackSpeed -= 100;
        damage += 5;
        super.upgrade();
    }

    @Override
    public void move(float x, float y) {

    }

    public void triggerEffects(Enemy enemy, Projectile projectile) {
        if (enemy.health <= 0) counter--;
        if (counter <= 0) {
            Stats.playerMaxHealth++;
            Stats.playerCurrentHealth++;
            counter = counterMax;
        }
    }

    @Override
    public Draw<Position, Float, Hitbox> getDrawFunc() {
        return drawFunc;
    }

    @Override
    public void draw() {

        Position pos1 = Tools.rotate(angle, new Position(0, width / 2));
        Position pos2 = Tools.rotate(angle, new Position(length, width / 2));
        Position pos3 = Tools.rotate(angle, new Position(length, width));
        Position pos4 = Tools.rotate(angle, new Position(length * 1.4f, width));
        Position pos5 = Tools.rotate(angle, new Position(length * 1.4f, -width));
        Position pos6 = Tools.rotate(angle, new Position(length, -width));
        Position pos7 = Tools.rotate(angle, new Position(length, -width / 2));
        Position pos8 = Tools.rotate(angle, new Position(0, -width / 2));

        double[] color = new double[]{1, 1, 1};

        if (rarity == 2) color = new double[]{0.7, 0.7, 1};
        if (rarity == 3) color = new double[]{0.8, 0.5, 0.9};
        if (rarity == 4) color = new double[]{1, 0.5, 0.5};


        glBegin(GL_QUADS);
        glColor3dv(color);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glVertex2d(pos.getX() + pos7.getX(), pos.getY() + pos7.getY());
        glVertex2d(pos.getX() + pos8.getX(), pos.getY() + pos8.getY());
        glEnd();

        glBegin(GL_QUADS);
        glColor3dv(color);
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glVertex2d(pos.getX() + pos5.getX(), pos.getY() + pos5.getY());
        glVertex2d(pos.getX() + pos6.getX(), pos.getY() + pos6.getY());
        glEnd();
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
}
