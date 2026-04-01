package Brewtato.GameObjects.Enemies;

import Brewtato.GameObjects.Object;
import Brewtato.Utilities.Position;

import java.util.List;

public abstract class Enemy implements Object {

    public int health;
    public int damage;
    public float speed;
    public Position pos;
    public int spawnAnimation = 70;
    public int materialModifier;

    public abstract boolean getHit(Position pos);
    public abstract int attack();
    public abstract void hunt(Position pos, List<Enemy> enemies);

}
