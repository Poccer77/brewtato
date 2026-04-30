package Brewtato.GameObjects.Collectibles;

import Brewtato.GameObjects.Player;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;
import static Brewtato.Stats.*;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class Material extends Collectible {

    private float angle;
    public float size;

    public Material(Position pos) {
        super(pos);
        Random rand = new Random();
        this.pos.changePosition(rand.nextFloat(-30, 31), rand.nextFloat(-30, 31));
        angle = (float) Math.toRadians(rand.nextInt(361));
        size = rand.nextInt(20, 30);
    }

    @Override
    public void draw() {

        Position pos1 = Tools.rotate(angle, new Position(-size / 2, -size / 2));
        Position pos2 = Tools.rotate(angle, new Position(-size / 2, size / 2));
        Position pos3 = Tools.rotate(angle, new Position(size / 2, size / 2));
        Position pos4 = Tools.rotate(angle, new Position(size / 2, -size / 2));

        glBegin(GL_QUADS);
        glColor3d(0, 1, 0.482);
        glVertex2d(pos.getX() + pos1.getX(), pos.getY() + pos1.getY());
        glColor3d(0, 1, 0.482);
        glVertex2d(pos.getX() + pos2.getX(), pos.getY() + pos2.getY());
        glColor3d(0, 1, 0.482);
        glVertex2d(pos.getX() + pos3.getX(), pos.getY() + pos3.getY());
        glColor3d(0, 1, 0.482);
        glVertex2d(pos.getX() + pos4.getX(), pos.getY() + pos4.getY());
        glEnd();

    }

    @Override
    public void buff(Player player) {
        materials += 1;
        exp += 1 + expModifier;
    }
}
