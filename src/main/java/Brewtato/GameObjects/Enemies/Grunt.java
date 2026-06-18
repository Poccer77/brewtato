package Brewtato.GameObjects.Enemies;

import Brewtato.Stats;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Utilities.Tools.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class Grunt extends Enemy {

    public Grunt(Position pos, int wave) {
        health = 3 + 2 * wave;
        speed = 7F;
        this.pos = pos;
        color = new double[]{0.78, 0, 0, 1};
        hit = new Hitbox();
        damage = 1 + (int) (wave * 0.6);
        width = height = 120;
        lootAmount = 1 + (int) ((ThreadLocalRandom.current().nextDouble( 1) < Stats.materialModifier - (int) Math.floor(Stats.materialModifier)) ? Math.ceil(Stats.materialModifier) : Math.floor(Stats.materialModifier));
        lootChance = 0.09 + Math.min(Stats.luck * 0.001, 0.1);
    }

    @Override
    public void hunt(Position playerpos, List<Enemy> enemies){super.huntPLayer(playerpos, enemies);}

    public void spawn(){super.spawnBlink(new double[]{0.78, 0, 0, 1});}

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }



    @Override
    public void draw(){

        if (spawnAnimation <= 0) color = new double[] {0.584, 0, 0.78, 1};

        hit = Tools.drawSquare(pos, width, height, color, true);
    }
}
