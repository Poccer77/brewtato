package Brewtato.GameObjects.Enemies;

import Brewtato.GameObjects.Weapons.Shooter.MonsterGun;
import Brewtato.GameObjects.Weapons.Shooter.Pistol;
import Brewtato.GameObjects.Weapons.Shooter.Projectile;
import Brewtato.GameObjects.Weapons.Shooter.Shooter;
import Brewtato.Phases.Game;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import static Brewtato.Utilities.Tools.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Sniper extends Enemy {

    MonsterGun shooter;
    double[] localColor;

    public Sniper(Position pos, int wave) {
        health = 4 + (int)(1.5 * wave);
        width = height = 150;
        speed = 4.5F;
        damage = (int) Math.max(1, 0.6 + (0.85F * wave));
        this.pos = pos;
        lootAmount = 1 + (int) ((ThreadLocalRandom.current().nextDouble( 1) < Stats.materialModifier - (int) Math.floor(Stats.materialModifier)) ? Math.ceil(Stats.materialModifier) : Math.floor(Stats.materialModifier));
        lootChance = 0.1 + Math.min(Stats.luck * 0.001, 0.1);
        shooter = new MonsterGun(damage + 1, 3500, pos);
        color = new double[] {0.684, 0, 0.88, 1};
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


    @Override
    public void hunt(Position playerPos, List<Enemy> enemies) {
        if (distance(pos, playerPos) <= 1500 && speed > 0) speed *= -1;
        else if (distance(pos, playerPos) > 1500 && speed <= 0) speed *= -1;
        super.huntPLayer(playerPos, enemies);

        shooter.pos = pos;
        shooter.aim(playerPos);
        shooter.attack();

        if (shooter.delay <= 300) {
            localColor[0] += 0.02;
            localColor[2] -= 0.02;
        } else localColor = Arrays.copyOf(color, 4);
    }

    @Override
    public void spawn() {
        super.spawnBlink(new double[]{0.78, 0, 0, 1});
        localColor = Arrays.copyOf(color, 4);
        if (spawnAnimation <= 0) {color = new double[] {0.684, 0, 0.88, 1};}
    }
}
