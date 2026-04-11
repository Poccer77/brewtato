package Brewtato.Phases;

import Brewtato.Main;

import static Brewtato.Utilities.Tools.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import Brewtato.Stats;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.stb.STBTruetype.*;
import org.lwjgl.system.linux.Stat;

import java.awt.*;
import java.awt.font.*;
import java.util.concurrent.ThreadLocalRandom;

public class LevelUp implements Phase{

    float width = ((float) Main.vidmode.width()) * ((1F/5) - (1.5F/10));
    float window = ((float) Main.vidmode.width()) / 5F;
    float margin = ((float) Main.vidmode.width()) / 30F;
    float heightMargin = ((float) Main.vidmode.width()) / 6F;

    LevelUps[] stats = new LevelUps[4];
    int[] rarities = new int[4];

    @Override
    public void draw() {

        Main.phases.get(0).draw();

        dim();



        float pos = width;

        for (int i = 0; i < 4; i++) {

            String levelup = "+" + stats[i].amount * rarities[i] + " " + stats[i].string;

            double[] color = new double[]{0.15, 0.15, 0.15};

            if (rarities[i] == 2) color = new double[]{0.453, 0.529, 0.8};
            if (rarities[i] == 3) color = new double[]{0.686, 0.435, 0.8};
            if (rarities[i] == 4) color = new double[]{0.8, 0.38, 0.38};

            glBegin(GL_QUADS);
            glColor3dv(color);
            glVertex2f(pos, heightMargin);
            glColor3dv(color);
            glVertex2f(pos + window, heightMargin);
            glColor3dv(color);
            glVertex2f(pos + window, Main.vidmode.height() - heightMargin);
            glColor3dv(color);
            glVertex2f(pos, Main.vidmode.height() - heightMargin);
            glColor3dv(new double[]{1.0, 1.0, 1.0});
            glEnd();


            Main.ttf.drawText(levelup, pos - (Main.ttf.stringWidth(levelup, 20) / 2) + window / 2, Main.vidmode.height() / 2, 20);

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
        for (int i = 0; i < 4; i++) {
            stats[i] = chooseStat();

            float rarity = (float) (ThreadLocalRandom.current().nextFloat(0.85F) + Math.min(Stats.luck * 0.001, 0.1) + Math.min(Stats.waveRarityScaling, 0.2F));

            rarities[i] = (rarity < 0.75) ? 1 :
                          (rarity < 0.9) ? 2 :
                          (rarity < 0.99) ? 3 :
                          4;
        }
    }

    private LevelUps chooseStat() {
        return LevelUps.values()[ThreadLocalRandom.current().nextInt(LevelUps.values().length)];
    }
}
