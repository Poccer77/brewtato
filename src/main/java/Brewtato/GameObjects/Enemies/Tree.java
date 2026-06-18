package Brewtato.GameObjects.Enemies;

import Brewtato.Stats;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static Brewtato.Utilities.Tools.distance;
import static org.lwjgl.opengl.GL11.*;

public class Tree extends Enemy{

    public boolean blink = false;
    public float width, height;

    public Tree(Position pos) {
        this.pos = pos;
        health = 30;
        speed = 0;
        hit = new Hitbox();
        width = height = 150;
        lootAmount = 3 + (int) ((ThreadLocalRandom.current().nextDouble( 1) < Stats.materialModifier - (int) Math.floor(Stats.materialModifier)) ? Math.ceil(Stats.materialModifier) : Math.floor(Stats.materialModifier));
        lootChance = 1;
    }

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    public void draw(){

        if (spawnAnimation <= 0) color = new double[]{0.267, 0.6, 0, 1};

        hit = Tools.drawSquare(pos, width, height, color, true);
    }


    public void spawn(){super.spawnBlink(new double[]{0.502, 0.38, 0, 1});}

    @Override
    public void hunt(Position pos, List<Enemy> enemies) {}
}
