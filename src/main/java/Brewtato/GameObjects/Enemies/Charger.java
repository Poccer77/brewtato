package Brewtato.GameObjects.Enemies;

import Brewtato.Stats;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static Brewtato.Utilities.Tools.*;

public class Charger extends Enemy{

    private int cooldown;
    private int currentCooldown;
    private boolean charging;
    double[] localColor;

    public Charger(Position pos, int wave) {

        health = 4 + (int)(2.5 * wave);
        width = height = 100;
        speed = 8.5F;
        damage = (int) Math.max(1, 0.6 + (0.85F * wave));
        this.pos = pos;
        lootAmount = 1 + (int) ((ThreadLocalRandom.current().nextDouble( 1) < Stats.materialModifier - (int) Math.floor(Stats.materialModifier)) ? Math.ceil(Stats.materialModifier) : Math.floor(Stats.materialModifier));
        lootChance = 0.1 + Math.min(Stats.luck * 0.001, 0.1);
        currentCooldown = cooldown = 3000;
        charging = false;
        color = new double[] {0.484, 0, 0.68, 1};
        localColor = Arrays.copyOf(color, 4);
    }

    @Override
    public boolean getHit(Hitbox hitbox) {
        return false;
    }


    @Override
    public void hunt(Position playerPos, List<Enemy> enemies) {

        if (currentCooldown <= 700 && distance(playerPos, pos) <= 2000) {charging = true;}

        if (charging) {
            if (currentCooldown > 500) {
                angle = angle(pos, playerPos);
                localColor[0] += 0.01;
                localColor[1] -= 0.01;
                localColor[2] -= 0.01;
            }
            else {
                localColor = Arrays.copyOf(color, 4);
                Position moveTo = rotate(angle, new Position(speed * 4f, 0));
                move(moveTo.getX(), moveTo.getY());
            }
            currentCooldown -= 10;
            if (currentCooldown <= 0) {
                currentCooldown = cooldown;
                charging = false;
            }
        } else {
            super.huntPLayer(playerPos, enemies);
            currentCooldown = Math.max(700, currentCooldown -= 10);
        }
    }

    public void spawn(){
        super.spawnBlink(new double[]{0.78, 0, 0, 1});
        if (spawnAnimation <= 0) {color = new double[] {0.484, 0, 0.68, 1};}
        localColor = Arrays.copyOf(color, 4);
    }

    @Override
    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    @Override
    public void draw(){
        hit = Tools.drawSquare(pos, width, height, localColor, true);
    }
}
