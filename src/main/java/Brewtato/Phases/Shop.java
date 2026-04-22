package Brewtato.Phases;

import Brewtato.Items.Item;
import Brewtato.Main;
import Brewtato.Stats;
import Brewtato.Utilities.Button;
import Brewtato.Utilities.Position;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static Brewtato.Main.vidmode;
import static Brewtato.Main.window;
import static Brewtato.Stats.levelsGained;
import static org.lwjgl.opengl.GL11.*;

public class Shop implements Phase{

    float width = ((float) Main.vidmode.width()) * ((1F/5) - (1.5F/10));
    float window = ((float) Main.vidmode.width()) / 6F;
    float margin = ((float) Main.vidmode.width()) / 50F;
    float heightMargin = ((float) Main.vidmode.width()) / 6F;
    boolean finished = false;
    LevelUps[] statsDisplay = LevelUps.values();
    Item[] items = new Item[4];
    boolean[] locked = new boolean[4];
    Button[] buyButton = new Button[4];
    Button[] lockButton = new Button[4];
    Button[] otherButtons = new Button[2];

    @Override
    public void draw() {

        float pos = width / 2;

        double[] color;

        for (int i = 0; i < 4; i++) {

            color = new double[]{0.15, 0.15, 0.15};
            if (items[i].rarity == 2) color = new double[]{0.453, 0.529, 0.8};
            if (items[i].rarity == 3) color = new double[]{0.686, 0.435, 0.8};
            if (items[i].rarity == 4) color = new double[]{0.8, 0.38, 0.38};

            glBegin(GL_QUADS);
            glColor3dv(color);
            glVertex2f(pos, heightMargin);
            glColor3dv(color);
            glVertex2f(pos + window, heightMargin);
            glColor3dv(color);
            glVertex2f(pos + window, Main.vidmode.height() - (heightMargin / 2));
            glColor3dv(color);
            glVertex2f(pos, Main.vidmode.height() - (heightMargin / 2));
            glColor3dv(new double[]{1.0, 1.0, 1.0});
            glEnd();

            Main.ttf.drawText(items[i].name, pos - ((float) Main.ttf.stringWidth(items[i].name, 50) / 2) + window / 2, (float) Main.vidmode.height() - (heightMargin / 2) - 120, 30);
            float currenHeight = (float) Main.vidmode.height() - (heightMargin / 2) - 250;
            for (Map.Entry<String, Integer> entry : items[i].desc.entrySet()) {

                String j = (entry.getValue() >= 0) ? "+" + entry.getValue() : String.valueOf(entry.getValue());
                String d = entry.getKey();

                glColor3dv((entry.getValue() >= 0) ? new double[]{0, 0.8, 0} : new double[]{0.8, 0, 0});
                Main.ttf.drawText(j, pos + 20, currenHeight, 30);
                glColor3dv(new double[]{1, 1, 1});
                Main.ttf.drawText(d, pos + 20 + Main.ttf.stringWidth(j + " ", 30), currenHeight, 30);
                currenHeight -= 60;
            }


            buyButton[i].draw();
            lockButton[i].draw();

            pos += window + margin;
        }

        for(Button button : otherButtons) {
            button.draw();
        }

        color = new double[]{0.15, 0.15, 0.15};
        glBegin(GL_QUADS);
        glColor3dv(color);
        glVertex2d(pos, Main.vidmode.height() - (heightMargin / 2));
        glVertex2d(Main.vidmode.width() - margin, Main.vidmode.height() - (heightMargin / 2));
        glVertex2d(Main.vidmode.width() - margin, (heightMargin / 2));
        glVertex2d(pos, (heightMargin / 2));
        Arrays.fill(color, 1);
        glColor3dv(color);
        glEnd();

        Main.ttf.drawText("STATS", pos + ((Main.vidmode.width() - margin - pos) / 2) - ((float) Main.ttf.stringWidth("STATS", 40) / 2), Main.vidmode.height() - (heightMargin / 2) - 90, 40);
        for (LevelUps lvlup : statsDisplay) {
            Arrays.fill(color, 0);

            if (lvlup.getStat.get() > 0) {
                color[1] = 0.8;
            } else if (lvlup.getStat.get() < 0) {
                color[0] = 0.8;
            } else Arrays.fill(color, 1);

            glColor3dv(color);

            Main.ttf.drawText(lvlup.string, pos + 10, Main.vidmode.height() - heightMargin, 30);
            Main.ttf.drawText(lvlup.getStat.get().toString(), (Main.vidmode.width() - margin - 10) - ((float) Main.ttf.stringWidth(lvlup.getStat.get().toString(), 30)), Main.vidmode.height() - heightMargin, 30);
            heightMargin += 60;
        }
        heightMargin = ((float) Main.vidmode.width()) / 6F;
    }

    @Override
    public boolean finished() {
        return finished;
    }

    @Override
    public void frameForward() {

        for (int i = 0; i < 4; i++) {
            if (lockButton[i].isPressed()) {
                if (!locked[i]) {
                    locked[i] = true;
                    lockButton[i].text = "Locked";
                } else {
                    locked[i] = false;
                    lockButton[i].text = "Lock";
                }
            }

            if (buyButton[i].isPressed()) {
                items[i].apply.run();
                locked[i] = false;
                chooseItems();
            }


        }

        if (otherButtons[0].isPressed()) chooseItems();

        if (otherButtons[1].isPressed()) finished = true;

        draw();
    }

    @Override
    public void init() {

        finished = false;

        chooseItems();

        double[] buttonColor = new double[]{0.15, 0.15, 0.15, 1};

        float pos = width / 2;
        for (int i = 0; i < lockButton.length; i++) {
            buyButton[i] = new Button(new Position(pos + 20, (float) (heightMargin - (vidmode.height() * (1.25/20F)))), String.valueOf(items[i].price), (Main.vidmode.height() * (1/20F)), window - 40, buttonColor);
            buyButton[i].textSize = 30;
            lockButton[i] = new Button(new Position(pos + 20, (float) (heightMargin - (vidmode.height() * (2.5/20F)))), "Lock", (Main.vidmode.height() * (1/20F)), window - 40, buttonColor);
            lockButton[i].textSize = 30;
            pos += window + margin;
        }

        otherButtons[0] = new Button(new Position(pos - (window + margin) + 20, vidmode.height() + 20 - heightMargin / 2), "Reroll", (Main.vidmode.height() * (1/20F)), window - 40, buttonColor);
        otherButtons[1] = new Button(new Position(vidmode.width() - window, 40), "Next Wave", (Main.vidmode.height() * (1/20F)), window - 40, buttonColor);
        otherButtons[0].textSize = 30;
        otherButtons[1].textSize = 30;
    }

    public void chooseItems() {

        List<Item> currentSelection = new ArrayList<>(Stats.items);

        Collections.shuffle(currentSelection);

        for (int i = 0; i < 4; i++) {
            if (locked[i]) continue;

            float rarity = (float) (ThreadLocalRandom.current().nextFloat(0.85F) + Math.min(Stats.luck * 0.001, 0.1) + Math.min(Stats.waveRarityScaling, 0.2F));

            int itemRarity = 1;

            items[i] = currentSelection.stream().filter((item) -> item.rarity == itemRarity).findAny().get();
            currentSelection.remove(items[i]);
        }
    }
}
