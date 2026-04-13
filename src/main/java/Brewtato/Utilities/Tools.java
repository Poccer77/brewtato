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

    public static void dim() {
        glBegin(GL_QUADS);
        glColor4d(0, 0, 0, 0.5);
        glVertex2d(0, 0);
        glColor4d(0, 0, 0, 0.5);
        glVertex2d(vidmode.width(), 0);
        glColor4d(0, 0, 0, 0.5);
        glVertex2d(vidmode.width(), vidmode.height());
        glColor4d(0, 0, 0, 0.5);
        glVertex2d(0, vidmode.height());
        glEnd();
    }

    public static boolean overlap(Hitbox h1, Hitbox h2) {
        return h1.isWithin(h2.x1) || h1.isWithin(h2.x2) || h1.isWithin(h2.x3) || h1.isWithin(h2.x4);
    }

    public static Position getMousePos() {
        DoubleBuffer posX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer posY = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(Main.window, posX, posY);
        return new Position((float) posX.get(0), vidmode.height() - (float) posY.get(0));
    }
}
