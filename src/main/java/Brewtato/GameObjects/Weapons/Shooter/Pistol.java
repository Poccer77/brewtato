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

import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;

public class Pistol extends Shooter {

    public Pistol() {
        super("Pistol", 10, 800, 80, 30, 1300);
        pierceDamageModifier = 0.75F;
        pierce += 1;
        rarity = 1;
    }

    @Override
    public Draw<Position, Float, Hitbox> getDrawFunc() {
        return drawFunc;
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

    @Override
    public int getDamageMod() {
        return (int) (damageMod * Stats.rangedDamage);
    }

    public void upgrade() {
        damage += 10;
        attackSpeed -= 50F;
        damageMod += 0.1F;
        super.upgrade();
    }

    @Override
    public void move(float x, float y) {

    }

    public void shoot() {
        if (delay <= 0 && inRange) {
            Game.projectiles.add(new Projectile(90, angle, damage, pos, range, this, drawFunc, pierce, true));
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
}

