package Brewtato.Phases;

import Brewtato.Main;

import static Brewtato.Utilities.Tools.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.stb.STBTruetype;
import org.lwjgl.stb.STBTruetype.*;

import java.awt.*;
import java.awt.font.*;

public class LevelUp implements Phase{

    float width = ((float) Main.vidmode.width()) * ((1F/5) - (1.5F/10));
    float window = ((float) Main.vidmode.width()) / 5F;
    float margin = ((float) Main.vidmode.width()) / 30F;
    float heightMargin = ((float) Main.vidmode.width()) / 6F;

    @Override
    public void draw() {

        Main.phases.get(0).draw();

        dim();

        double[] color = new double[]{0.15, 0.15, 0.15};

        float pos = width;

        for (int i = 1; i <= 4; i++) {
            glBegin(GL_QUADS);
            glColor3dv(color);
            glVertex2f(pos, heightMargin);
            glColor3dv(color);
            glVertex2f(pos + window, heightMargin);
            glColor3dv(color);
            glVertex2f(pos + window, Main.vidmode.height() - heightMargin);
            glColor3dv(color);
            glVertex2f(pos, Main.vidmode.height() - heightMargin);
            glEnd();

            pos += window + margin;
        }
    }

    @Override
    public boolean finished() {
        return false;
    }

    @Override
    public void frameForward() {

        draw();

    }

    @Override
    public void init() {

    }
}
