package Brewtato.Phases;

import Brewtato.GameObjects.Collectibles.Chest;
import Brewtato.Items.ItemCard;
import Brewtato.Main;
import Brewtato.Stats;
import Brewtato.Utilities.Button;
import Brewtato.Utilities.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static Brewtato.Stats.*;
import static Brewtato.Utilities.Tools.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Chests implements Phase{

    private static List<Chest> chests = new ArrayList<>();
    float width = ((float) Main.vidmode.width() / 6);
    float height = ((float) Main.vidmode.height()) * (4/6F);
    int rarity = 1;
    Button[] buttons = new Button[2];
    ItemCard currentItem;
    boolean selected = true;
    Position pos = new Position((float) Main.vidmode.width() / 2, (float) Main.vidmode.height() / 2);

    public static void insertChest(Chest chest) {
        chests.add(chest);
    }

    @Override
    public void draw() {

        double[] color = switch(rarity) {
            case 1 -> new double[]{0.15, 0.15, 0.15};
            case 2 -> new double[]{0.453, 0.529, 0.8};
            case 3 -> new double[]{0.686, 0.435, 0.8};
            case 4 -> new double[]{0.8, 0.38, 0.38};
            default -> throw new IllegalStateException("Invalid rarity");
        };

        drawSquare(pos, width, height, color);

        currentItem.draw(pos.getX());

        for (Button button : buttons) button.draw();
    }

    @Override
    public boolean finished() {
        return selected && chests.isEmpty();
    }

    @Override
    public void frameForward() {

        draw();

        if (buttons[0].isPressed()) {
            currentItem.apply.get();
            selected = true;
            select();
        } else if (buttons[1].isPressed()) {
            materials += currentItem.price;
            selected = true;
            select();
        }

    }

    @Override
    public void init() {

        if (chests.isEmpty()) return;

        buttons[0] = new Button(new Position(pos.getX(), pos.getY() - (height / 2) + 2 * (height / 6)), "Get", width - (width / 10), height / 6, new double[]{0.15, 0.15, 0.15, 1});
        buttons[1] = new Button(new Position(pos.getX(), pos.getY() - (height / 2) + height / 6), "", width - (width / 10), height / 6, new double[]{0.15, 0.15, 0.15, 1});

        select();
        draw();
    }

    private void select() {

        if (chests.isEmpty()) return;

        rarity = chests.getLast().rarity;

        List<ItemCard> currentSelection = new ArrayList<>(items);
        currentSelection.stream().filter(item -> item.rarity == rarity).toList();
        Collections.shuffle(currentSelection);

        currentItem = currentSelection.getFirst();
        buttons[1].text = "sell (+" + currentItem.price + ")";

        chests.removeLast();

        selected = false;
    }
}
