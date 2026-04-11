package Brewtato.GameObjects.Weapons;

import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;

public class PistolProjectile extends Projectile{

    private final float width;
    private final float length;

    public PistolProjectile(float angle, int damage, Position pos) {
        this.pos = new Position(pos.getX(), pos.getY());
        this.angle = angle;
        this.damage = damage;
        speed = 50;
        width = 15F;
        length = 75F;
        hit = new Hitbox();
    }

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    public void move() {
        Position newPosition = Tools.rotate(angle, new Position(speed, 0));
        pos.changePosition(newPosition);
    }

    @Override
    public void draw() {

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
    }
}
