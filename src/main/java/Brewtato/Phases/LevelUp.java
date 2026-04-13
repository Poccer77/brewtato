package Brewtato.Phases;

import Brewtato.Main;

import static Brewtato.Stats.levelsGained;
import static Brewtato.Utilities.Tools.*;

import Brewtato.Utilities.Button;
import Brewtato.Utilities.Button.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import Brewtato.Stats;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.stb.STBTruetype.*;
import org.lwjgl.system.linux.Stat;

import java.awt.*;
import java.awt.font.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LevelUp implements Phase{

    float width = ((float) Main.vidmode.width()) * ((1F/5) - (1.5F/10));
    float window = ((float) Main.vidmode.width()) / 5F;
    float margin = ((float) Main.vidmode.width()) / 30F;
    float heightMargin = ((float) Main.vidmode.width()) / 6F;
    boolean selected = true;

    LevelUps[] stats = new LevelUps[4];
    int[] rarities = new int[4];
    Brewtato.Utilities.Button[] buttons = new Button[4];

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

            Main.ttf.drawText(levelup, pos - ((float) Main.ttf.stringWidth(levelup, 30) / 2) + window / 2, (float) Main.vidmode.height() / 2, 30);

            buttons[i].color = new double[]{color[0] + 0.1, color[1] + 0.1, color[2] + 0.1, 1};
            buttons[i].draw();

            pos += window + margin;
        }
    }

    @Override
    public boolean finished() {
        return levelsGained == 0 && selected;
    }

    @Override
    public void frameForward() {
        if (levelsGained == 0 && selected) return;

        for (int i = 0; i < 4; i++) {
            buttons[i].hover();
            if (buttons[i].isPressed()) {
                stats[i].applyStat.accept(rarities[i]);
                selected = true;
                select();
            };
        }

        draw();
    }

    public void select() {
        if (levelsGained > 0) {
            levelsGained--;
            chooseStats();
            selected = false;
        }
    }

    @Override
    public void init() {
        float pos = width;
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(new Position(pos + 20, heightMargin + 20), "Choose", (Main.vidmode.height() * (1/20F)), window - 40, new double[]{0, 0, 0, 0,});
            pos += window + margin;
        }
        select();
    }

    private void chooseStats() {
        selected = false;
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < LevelUps.values().length; i++) values.add(i);
        Collections.shuffle(values);
        for (int i = 0; i < 4; i++) stats[i] = LevelUps.values()[values.get(i)];
        for (int i = 0; i < 4; i++) {

            float rarity = (float) (ThreadLocalRandom.current().nextFloat(0.85F) + Math.min(Stats.luck * 0.001, 0.1) + Math.min(Stats.waveRarityScaling, 0.2F));

            rarities[i] = (rarity < 0.75) ? 1 :
                    (rarity < 0.9) ? 2 :
                            (rarity < 0.99) ? 3 :
                                    4;
        }
    }
}
