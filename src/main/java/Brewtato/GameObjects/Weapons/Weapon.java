package Brewtato.GameObjects.Weapons;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.GameObjects.Object;
import Brewtato.Utilities.*;

import java.util.ArrayList;
import java.util.List;


public abstract class Weapon implements Object {

    public int damage;
    public String name;
    public float attackSpeed;
    protected float delay;
    protected float angle;
    public int range;
    public float damageMod;
    public int pierce;
    public int rarity;
    protected boolean inRange;

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Position pos;

    protected final float length;
    protected final float width;


    protected Weapon(String name, int damage, int attackSpeed, float length, float width, int range) {
        this.name = name;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.length = length;
        this.width = width;
        this.range = range;
        delay = 0;
    }

    public void aim(List<Enemy> enemies, Position playerMove) {

    }

    public void upgrade() {
        rarity++;
    };

    public abstract void draw();

}
