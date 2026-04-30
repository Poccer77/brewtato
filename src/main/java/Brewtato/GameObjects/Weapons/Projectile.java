package Brewtato.GameObjects.Weapons;

import Brewtato.GameObjects.Enemies.Enemy;
import Brewtato.GameObjects.Object;
import Brewtato.Utilities.*;

import java.util.List;

public abstract class Projectile implements Object {

    public Hitbox hit;
    public Position pos;
    public Shooter originWeapon;
    public List<Enemy> piercedEnemies;
    public float speed;
    public int damage;
    public float angle;
    public float range;
    public abstract void move();

    public abstract float getDamage();
}
