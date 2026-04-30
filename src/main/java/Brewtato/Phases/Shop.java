package Brewtato.Phases;

import Brewtato.Items.Buyable;
import Brewtato.Items.ItemCard;
import Brewtato.Items.WeaponCard;
import Brewtato.Main;
import Brewtato.Stats;
import Brewtato.Utilities.Button;
import Brewtato.Utilities.GlobalUI;
import Brewtato.Utilities.Position;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static Brewtato.Main.vidmode;
import static Brewtato.Stats.materials;
import static org.lwjgl.opengl.GL11.*;

public class Shop implements Phase{

    float width = ((float) Main.vidmode.width()) * ((1F/5) - (1.5F/10));
    float window = ((float) Main.vidmode.width()) / 6F;
    float margin = ((float) Main.vidmode.width()) / 50F;
    float heightMargin = ((float) Main.vidmode.width()) / 6F;
    boolean finished = false;
    LevelUps[] statsDisplay = LevelUps.values();
    Buyable[] buyables = new Buyable[4];
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

            if (buyables[i].rarity == 2) color = new double[]{0.453, 0.529, 0.8};
            if (buyables[i].rarity == 3) color = new double[]{0.686, 0.435, 0.8};
            if (buyables[i].rarity == 4) color = new double[]{0.8, 0.38, 0.38};


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

            buyables[i].draw(pos);

            if (buyables[i].price > materials) buyButton[i].textColor = new double[]{0.8, 0, 0};
            else buyButton[i].textColor = new double[]{1, 1, 1};
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

        GlobalUI.drawStats();
        GlobalUI.drawMaterials(new Position(vidmode.width() * (1F/21F), vidmode.height() * (9F/10F)));

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

            if (buyButton[i].isPressed() && buyables[i].price <= materials && buyables[i].apply.get()) {
                materials -= buyables[i].price;
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

        double[] buttonColor = new double[]{0.15, 0.15, 0.15, 1};

        float pos = width / 2;
        for (int i = 0; i < lockButton.length; i++) {
            buyButton[i] = new Button(new Position(pos + 20, (float) (heightMargin - (vidmode.height() * (1.25/20F)))), "", (Main.vidmode.height() * (1/20F)), window - 40, buttonColor);
            buyButton[i].textSize = 30;
            lockButton[i] = new Button(new Position(pos + 20, (float) (heightMargin - (vidmode.height() * (2.5/20F)))), "Lock", (Main.vidmode.height() * (1/20F)), window - 40, buttonColor);
            lockButton[i].textSize = 30;
            pos += window + margin;
        }

        chooseItems();

        otherButtons[0] = new Button(new Position(pos - (window + margin) + 20, vidmode.height() + 20 - heightMargin / 2), "Reroll", (Main.vidmode.height() * (1/20F)), window - 40, buttonColor);
        otherButtons[1] = new Button(new Position(vidmode.width() - window, 40), "Next Wave", (Main.vidmode.height() * (1/20F)), window - 40, buttonColor);
        otherButtons[0].textSize = 30;
        otherButtons[1].textSize = 30;
    }

    public void chooseItems() {

        List<Buyable> currentSelection;

        for (int i = 0; i < 4; i++) {

            if (locked[i]) continue;

            float rarity = (float) (ThreadLocalRandom.current().nextFloat(0.85F) + Math.min(Stats.luck * 0.001, 0.1) + Math.min(Stats.waveRarityScaling, 0.2F));

            int itemRarity = ThreadLocalRandom.current().nextInt(1, 3);

            //currentSelection = ((ThreadLocalRandom.current().nextFloat(1F) < 0.65F) ? new ArrayList<>(Stats.items) : new ArrayList<>(Stats.weapons));
            currentSelection = new ArrayList<>(Stats.weapons);
            Collections.shuffle(currentSelection);

            Buyable currentBuyable;

            if (currentSelection.get(0) instanceof ItemCard) {
                currentBuyable = currentSelection.stream().filter((item) -> item.rarity == itemRarity).findAny().get();
                if (currentSelection.contains(currentBuyable)) {
                    ItemCard temptempItem = (ItemCard) currentBuyable;
                    ItemCard tempItem = Stats.items.stream().filter((item) -> item.equals(temptempItem)).findFirst().get();
                    tempItem.amount -= 1;
                    if (tempItem.amount == 0) Stats.items.remove(tempItem);
                }
            } else {
                currentBuyable = currentSelection.stream().filter((item) -> item.rarity == itemRarity).findAny().get();
            }
            buyables[i] = currentBuyable;
            buyButton[i].text = String.valueOf(buyables[i].price);
        }
    }
}
