package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.Main;
import Brewtato.Phases.Game;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;

public class Slingshot extends Shooter{

    public Slingshot() {
        super("Slingshot", 10, 1200, 100, 30, 900);
        rarity = 1;
        bounce = 1;
        damageMod = 0.8f;
        pierceDamageModifier = 0.75f;
    }

    public void shoot() {
        if (delay <= 0 && inRange) {
            Projectile pro = new Projectile(70, angle, damage, pos, range, this, drawFunc, pierce, true);
            pro.length = pro.width = 30;
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
        return (int) (damageMod * Stats.rangedDamage);
    }

    public void upgrade() {
        damage += 10;
        damageMod += 0.1f;
        bounce += 1;
        super.upgrade();
    }

    @Override
    public void draw() {

        int swap = (angle < Math.PI / 2 && angle > - Math.PI / 2) ? -1 : 1;



        Position pos1 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(-length * swap / 2, - width / 4));
        Position pos2 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(0,- width / 4));
        Position pos3 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(0,width / 4));
        Position pos4 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(-length * swap / 2, width / 4));
        Position pos5 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(0, - width / 4));
        Position pos6 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(length * swap / 2, - width));
        Position pos7 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(length * swap / 2, - width / 2));
        Position pos8 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(0, 0));
        Position pos9 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(0, width / 4));
        Position pos10 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(length * swap / 2, width));
        Position pos11 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(length * swap / 2, width / 2));
        Position pos12 = Tools.rotate(angle - (float) Math.toRadians(90), new Position(0, 0));



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
        glVertex2d(pos.getX() + pos5.getX(), pos.getY() + pos5.getY());
        glVertex2d(pos.getX() + pos6.getX(), pos.getY() + pos6.getY());
        glVertex2d(pos.getX() + pos7.getX(), pos.getY() + pos7.getY());
        glVertex2d(pos.getX() + pos8.getX(), pos.getY() + pos8.getY());
        glVertex2d(pos.getX() + pos9.getX(), pos.getY() + pos9.getY());
        glVertex2d(pos.getX() + pos10.getX(), pos.getY() + pos10.getY());
        glVertex2d(pos.getX() + pos11.getX(), pos.getY() + pos11.getY());
        glVertex2d(pos.getX() + pos12.getX(), pos.getY() + pos12.getY());
        glEnd();
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
