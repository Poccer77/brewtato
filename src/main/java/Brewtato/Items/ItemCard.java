package Brewtato.Items;

import Brewtato.Main;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glColor3dv;

public class ItemCard extends Buyable{

    public Integer amount;

    public ItemCard(String name, LinkedHashMap<String, Integer> desc, Runnable apply, Integer amount, Integer rarity, Integer price) {
        super(name, desc, () -> {apply.run(); return true;}, rarity, price);
        this.amount = amount;
    }

    public String toString(){
        return name + ", " + amount + ", " + rarity + ", " + price;
    }

    public void draw(float x) {

        float heightMargin = ((float) Main.vidmode.width()) / 6F;
        float window = ((float) Main.vidmode.width()) / 6F;

        Main.ttf.drawText(name, x - ((float) Main.ttf.stringWidth(name, 50) / 2) + window / 2, (float) Main.vidmode.height() - (heightMargin / 2) - 120, 30);
        float currentHeight = (float) Main.vidmode.height() - (heightMargin / 2) - 250;
        for (Map.Entry<String, Integer> entry : desc.entrySet()) {

            String j = (entry.getValue() >= 0) ? "+" + entry.getValue() : String.valueOf(entry.getValue());
            String d = entry.getKey();

            glColor3dv((entry.getValue() >= 0) ? new double[]{0, 0.8, 0} : new double[]{0.8, 0, 0});
            Main.ttf.drawText(j, x + 20, currentHeight, 30);
            glColor3dv(new double[]{1, 1, 1});
            Main.ttf.drawText(d, x + 20 + Main.ttf.stringWidth(j + " ", 30), currentHeight, 30);
            currentHeight -= 60;
        }
    }
}
