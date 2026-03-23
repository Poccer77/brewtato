package GameObjects.Weapons;

import GameObjects.Enemies.Enemy;
import GameObjects.Object;
import Utilities.Position;

import java.util.ArrayList;
import java.util.List;


public abstract class Weapon implements Object {

    public final int damage;
    public final int attackSpeed;
    int delay;
    public float angle;
    public double range;

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public List<Projectile> projectiles = new ArrayList<>();

    public Position pos;

    final float length;
    final float width;


    protected Weapon(int damage, int attackSpeed, float length, float width, double range) {
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.length = length;
        this.width = width;
        this.range = range;
        delay = 0;
    }

    public void aim(List<Enemy> enemies) {

    }

    public abstract void shoot();

    public abstract void draw();

}
