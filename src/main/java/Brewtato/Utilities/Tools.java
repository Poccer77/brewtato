package Brewtato.Utilities;

import Brewtato.Main;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTruetype;

import javax.swing.text.AttributeSet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import static Brewtato.Main.vidmode;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.*;

public class Tools {

    public static float angle(Position pos1, Position pos2) {
        return (float) Math.atan2(pos2.getY() - pos1.getY(), pos2.getX() - pos1.getX());
    }

    public static float distance(Position pos1, Position pos2) {
        return (float) sqrt(pow(pos2.getX() - pos1.getX(), 2) + pow(pos2.getY() - pos1.getY(), 2));
    }

    public static float distance(Position pos1) {
        return (float) sqrt(pow(0 - pos1.getX(), 2) + pow(0 - pos1.getY(), 2));
    }

    public static Position rotate(float angle, Position pos) {
        pos.rotate(angle);
        return pos;
    }

    public static Hitbox drawSquare(Position pos, double width, double length, double[] color, boolean outline) {

        double[] localColor = Arrays.copyOf(color, 4);

        if (color.length < localColor.length) localColor[3] = 1;

        if (outline) {
            glBegin(GL_QUADS);
            glColor4dv(new double[]{0, 0, 0, localColor[3]});
            glVertex2d(pos.getX() - width / 2 - (width / 10), pos.getY() - length / 2 - (length / 10));
            glVertex2d(pos.getX() - width / 2 - (width / 10), pos.getY() + length / 2 + (length / 10));
            glVertex2d(pos.getX() + width / 2 + (width / 10), pos.getY() + length / 2 + (length / 10));
            glVertex2d(pos.getX() + width / 2 + (width / 10), pos.getY() - length / 2 - (length / 10));
            glEnd();
        }

        glBegin(GL_QUADS);
        glColor4dv(localColor);
        glVertex2d(pos.getX() - width / 2, pos.getY() - length / 2);
        glVertex2d(pos.getX() - width / 2, pos.getY() + length / 2);
        glVertex2d(pos.getX() + width / 2, pos.getY() + length / 2);
        glVertex2d(pos.getX() + width / 2, pos.getY() - length / 2);
        glEnd();

        return new Hitbox(
                new Position((float) (pos.getX() - width / 2), (float) (pos.getY() - length / 2)),
                new Position((float) (pos.getX() - width / 2), (float) (pos.getY() + length / 2)),
                new Position((float) (pos.getX() + width / 2), (float) (pos.getY() + length / 2)),
                new Position((float) (pos.getX() + width / 2), (float) (pos.getY() - length / 2))
        );
    }

    public static void dim() {
        glBegin(GL_QUADS);
        glColor4d(0, 0, 0, 0.6);
        glVertex2d(0, 0);
        glColor4d(0, 0, 0, 0.6);
        glVertex2d(vidmode.width(), 0);
        glColor4d(0, 0, 0, 0.6);
        glVertex2d(vidmode.width(), vidmode.height());
        glColor4d(0, 0, 0, 0.6);
        glVertex2d(0, vidmode.height());
        glEnd();
    }

    public static boolean overlap(Hitbox h1, Hitbox h2) {
        return (h1.isWithin(h2.x1) || h1.isWithin(h2.x2) || h1.isWithin(h2.x3) || h1.isWithin(h2.x4)) ||
               (h2.isWithin(h1.x1) || h2.isWithin(h1.x2) || h2.isWithin(h1.x3) || h2.isWithin(h1.x4));
    }

    public static Position getMousePos() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Main.window, posX, posY);
        return new Position((float) posX.get(0), vidmode.height() - (float) posY.get(0));
    }
}
