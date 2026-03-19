package GameObjects.Enemies;

import GameObjects.Object;
import Utilities.Position;

public abstract class Enemy implements Object {

    public int health;
    public int damage;
    public int speed;
    public Position position;

    public abstract void getHit(int damage);
    public abstract int attack();

}
