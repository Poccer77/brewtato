package GameObjects.Weapons;

import Utilities.Position;
import Utilities.Tools;

import static Utilities.Tools.rotate;
import static org.lwjgl.opengl.GL11.*;

public class PistolProjectile extends Projectile{

    private final float width;
    private final float length;

    public PistolProjectile(float angle, int damage, Position pos) {
        this.pos = pos;
        this.angle = angle;
        this.damage = damage;
        speed = 0.1F;
        width = 0.01F;
        length = 0.03F;
    }

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    @Override
    public void draw() {

        Position newPosition = Tools.rotate(angle, new Position(0, speed));

        move(newPosition.getX(), newPosition.getY());

        Position pos1 = rotate(angle, new Position(width / 2, 0));
        Position pos2 = rotate(angle, new Position(-width / 2, 0));
        Position pos3 = rotate(angle, new Position(-width / 2, length));
        Position pos4 = rotate(angle, new Position(width / 2, length));

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
    }
}
