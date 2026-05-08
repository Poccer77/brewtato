package Brewtato.GameObjects.Enemies;

import Brewtato.Effects.Debuff;
import Brewtato.GameObjects.Object;
import Brewtato.Phases.Game;
import Brewtato.Utilities.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemy implements Object {

    public Hitbox hit;
    public int health;
    public int damage;
    public float speed;
    public Position pos;
    public int spawnAnimation = 70;
    public int deathAnimation = 60;
    boolean blink = false;
    double[] color;
    public List<Debuff> debuffs = new ArrayList<Debuff>();

    public abstract boolean getHit(Hitbox hitbox);
    public abstract int attack();
    public abstract void hunt(Position pos, List<Enemy> enemies);
    public abstract void spawn();
    public abstract boolean die();
}
