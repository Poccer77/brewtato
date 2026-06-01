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

    public void spawn(){
        if (spawnAnimation % 10 == 0) {
            blink = !blink;
        }
        if (blink) {
            spawnAnimation--;
            color = new double[]{0.502, 0.38, 0, 1};
        } else {
            spawnAnimation--;
            color = new double[]{1, 0, 0, 0};
        }
    }

    @Override
    public boolean die() {
        if (deathAnimation > 0) {
            width *= ((float) deathAnimation / 50);
            height *= ((float) deathAnimation / 50);
            deathAnimation -= 10;
            return false;
        } else return true;
    }

    public void draw(){

        double[] bgcolor = new double[]{0, 0, 0, color[3]};

        Tools.drawSquare(pos, width * 1.1, height * 1.1, bgcolor);

        if (spawnAnimation <= 0) color = new double[]{0.267, 0.6, 0, 1};

        hit = Tools.drawSquare(pos, width, height, color);
    }

    @Override
    public boolean getHit(Hitbox hitbox) {
        return Tools.overlap(hit, hitbox);
    }

    @Override
    public int attack() {
        return 0;
    }

    @Override
    public void hunt(Position pos, List<Enemy> enemies) {
    }
}
