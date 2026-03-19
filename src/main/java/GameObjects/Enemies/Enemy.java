package GameObjects.Enemies;

import GameObjects.Object;
import Utilities.Position;

public abstract class Enemy implements Object {

    public int health;
    public int damage;
    public float speed;
    public Position pos;

    public abstract void getHit(int damage);
    public abstract int attack();
    public abstract void hunt(Position pos);

}
