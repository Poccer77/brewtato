package Brewtato.GameObjects.Weapons.Shooter;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.Stats;
import Brewtato.Utilities.Draw;
import Brewtato.Utilities.Hitbox;
import Brewtato.Utilities.Position;
import Brewtato.Utilities.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static Brewtato.Utilities.Tools.distance;

public class Projectile {

    public float width;
    public float length;
    public int pierces;
    public Draw<Position, Float, Hitbox> drawFunc;
    public Hitbox hit;
    public Position pos;
    public Shooter originWeapon;
    public List<Enemy> hitEnemies;
    public float speed;
    public int damage;
    public float angle;
    public float range;

    public Projectile(int speed, float angle, int damage, Position pos, int range, Shooter originWeapon, Draw<Position, Float, Hitbox> drawFunc, int pierces) {
        this.pos = new Position(pos.getX(), pos.getY());
        this.angle = angle;
        this.damage = damage;
        this.speed = speed;
        width = 15F;
        length = 75F;
        this.range = range;
        hit = new Hitbox();
        this.originWeapon = originWeapon;
        hitEnemies = new ArrayList<>();
        this.drawFunc = drawFunc;
    }

    public float getDamage() {
        return (float) Math.max(1, (1 + ((double) Stats.damage / 100)) * (damage + originWeapon.getDamageMod() * (Math.pow(originWeapon.pierceDamageModifier, Math.max(hitEnemies.size() - (originWeapon.bounce + Stats.bounce), 0)))));
    }

    public void triggerEffects(Enemy enemy) {
        originWeapon.triggerEffects(enemy, this);
    }

    public void aim(List<Enemy> enemies) {

        if (!enemies.isEmpty()) {
            Enemy closestEnemy = enemies.get(0);
            for (Enemy enemy : enemies) {
                if (Tools.distance(pos, enemy.pos) < Tools.distance(pos, closestEnemy.pos)) {
                    closestEnemy = enemy;
                }
            }
            angle = Tools.angle(new Position(pos.getX(), pos.getY()), closestEnemy.pos);
        } else angle = (float) ThreadLocalRandom.current().nextDouble(-Math.PI, Math.PI);

    }

    public void move(float x, float y) {
        pos.changePosition(x, y);
    }

    public void move() {
        Position newPosition = Tools.rotate(angle, new Position(speed, 0));
        pos.changePosition(newPosition);

        range -= distance(newPosition);
    }

    public void draw() {
        hit = drawFunc.apply(pos, angle, length, width, hit);
    }
}
