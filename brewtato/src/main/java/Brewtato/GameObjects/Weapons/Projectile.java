package Brewtato.GameObjects.Weapons;

import Brewtato.GameObjects.Object;
import Brewtato.Utilities.*;

public abstract class Projectile implements Object {

    public Hitbox hit;
    public Position pos;
    public float speed;
    public int damage;
    public float angle;
    public abstract void move();

}
