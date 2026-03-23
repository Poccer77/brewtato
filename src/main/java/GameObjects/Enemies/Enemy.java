package GameObjects.Enemies;

import GameObjects.Object;
import Utilities.Position;

public abstract class Enemy implements Object {

    public int health;
    public int damage;
    public float speed;
    public Position pos;
    public int spawnAnimation = 70;

    public abstract boolean getHit(Position pos);
    public abstract int attack();
    public abstract void hunt(Position pos);

}
