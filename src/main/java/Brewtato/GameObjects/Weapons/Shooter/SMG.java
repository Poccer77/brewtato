package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.Main;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class SMG extends Shooter{

    public SMG() {
        super("SMG", 3, 200, 80, 30, 1000);
        rarity = 1;
        damageMod = 0.5f;
        pierceDamageModifier = 0.5F;
    }

    @Override
    public void shoot() {
        if (delay <= 0 && inRange) {
            projectiles.add(new Projectile(angle, damage, pos, range, this, drawFunc));
            delay = attackSpeed;
        } else {
            delay -= (float) (Main.tickTime);
        }
    }

    @Override
    public void move(float x, float y) {

    }

    public void upgrade() {
        damage += 3;
        attackSpeed -= 20;
        damageMod += 0.05f;
        super.upgrade();
    }

    @Override
    public void draw() {

        int swap = (angle < Math.PI / 2 && angle > - Math.PI / 2) ? -1 : 1;

        Position pos1 = Tools.rotate(angle, new Position(0, width / 2));
        Position pos2 = Tools.rotate(angle, new Position(length, width / 2));
        Position pos3 = Tools.rotate(angle, new Position(length, -width / 2));
        Position pos4 = Tools.rotate(angle, new Position(0, -width / 2));
        Position pos5 = Tools.rotate(angle, new Position(length / 4, swap * width / 2));
        Position pos6 = Tools.rotate(angle, new Position(length / 3, swap * width * 1.3f));
        Position pos7 = Tools.rotate(angle, new Position(length / 2, swap * width * 1.3f));
        Position pos8 = Tools.rotate(angle, new Position(length / 2.2f, swap * width / 2));

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
